package arathain.arcpocalypse.common;

import arathain.arcpocalypse.ArcpocalypseComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.quiltmc.qsl.entity.multipart.api.EntityPart;
import org.quiltmc.qsl.entity.multipart.api.MultipartEntity;

public class AbyssLiftEntity extends Entity implements MultipartEntity {
	private int descentTicks = 0;
	private final AbyssLiftPart floor = new AbyssLiftPart(this, 0.99f, 0.1f);
	private static final TrackedData<BlockPos> HANGED_POS = DataTracker.registerData(AbyssLiftEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

	public AbyssLiftEntity(World world) {
		super(ArcpocalypseEntities.ABYSS_LIFT, world);
		this.inanimate = true;
	}
	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public boolean collides() {
		return !this.isRemoved();
	}

	@Override
	public void updatePassengerPosition(Entity passenger) {
		if (!this.hasPassenger(passenger)) {
			return;
		}
		passenger.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), MathHelper.clamp(passenger.getYaw(), this.getYaw() - 45f, this.getYaw() + 45f), MathHelper.clamp(passenger.getPitch(), this.getPitch() - 45f, this.getPitch() + 45f));
		passenger.setYaw(MathHelper.clamp(passenger.getYaw(), this.getYaw() - 45f, this.getYaw() + 45f));
		passenger.setPitch(MathHelper.clamp(passenger.getPitch(), this.getPitch() - 45f, this.getPitch() + 45f));
		passenger.setHeadYaw(MathHelper.clamp(passenger.getHeadYaw(), this.getYaw() - 45f, this.getYaw() + 45f));
		if(passenger instanceof LivingEntity l) {
			passenger.setBodyYaw(MathHelper.clamp(l.bodyYaw, this.getYaw() - 10f, this.getYaw() + 10f));
		}
	}

	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(HANGED_POS, BlockPos.ORIGIN);
	}

	@Override
	public void tick() {
		super.tick();
		if(this.getTargetPos().equals(BlockPos.ORIGIN)) {
			this.setTargetPos(this.getBlockPos().add(0, 3, 0));
		}
		if(!world.isClient() && canDescend()) {
			this.descendTick();
		}

	}
	private boolean canDescend() {
		boolean bl = true;
		int m = this.world.getDimension().minY();
		for(int i = this.getBlockY(); i >= m; i--) {
			bl = this.world.getBlockState(new BlockPos(this.getBlockX(), i, this.getBlockZ())).isAir();
			if(!bl) {
				break;
			}
		}
		return bl && this.hasPassengers();
	}
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		if(player.getStackInHand(hand).isEmpty() && !world.isClient()) {
			player.startRiding(this, true);
		}
		return super.interact(player, hand);
	}
	private void descendTick() {
		this.prevX = this.getX();
		this.prevY = this.getY();
		this.prevZ = this.getZ();
		if(!(this.getY() < this.world.getDimension().minY() - 10) && this.descentTicks < 100) {
			this.setVelocity(0, -0.75, 0);
		} else {
			this.setVelocity(0, 0, 0);
			this.descentTicks++;
			if(this.descentTicks >= 100) {
				this.setVelocity(0, 1, 0);
				if(this.getFirstPassenger() instanceof LivingEntity l) {
					l.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 40, 0));
					if(l instanceof PlayerEntity && random.nextFloat() > 0.98f && !l.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
						l.getComponent(ArcpocalypseComponents.ARC_COMPONENT).setArc(true);
					}
				}
			}
			if(this.verticalCollision) {
				if(this.descentTicks >= 200)
					this.descentTicks = 100;
				this.descentTicks++;
				if(this.descentTicks >= 160) {
					Entity entity = this.getFirstPassenger();
					entity.stopRiding();
					entity.setPosition(entity.getPos().add(this.getRotationVector(this.getPitch(), this.getYaw())));
					this.descentTicks = 0;
				}
			}
		}
		this.move(MovementType.SELF, this.getVelocity());
		this.floor.move(Vec3d.ZERO);
		this.velocityDirty = true;
		this.velocityModified = true;
	}
	@Override
	public boolean isLogicalSideForUpdatingMovement() {
		return !this.world.isClient();
	}

	public void setTargetPos(BlockPos pos) {
		this.getDataTracker().set(HANGED_POS,  pos);
	}
	public BlockPos getTargetPos() {
		return this.getDataTracker().get(HANGED_POS);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		setTargetPos(NbtHelper.toBlockPos(nbt.getCompound("hanged_pos")));
		descentTicks = nbt.getInt("descent");
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.put("hanged_pos", NbtHelper.fromBlockPos(this.getTargetPos()));
		nbt.putInt("descent", descentTicks);
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		this.floor.setId(1 + packet.getId());
	}

	@Override
	public EntityPart<?>[] getEntityParts() {
		return new EntityPart[] {this.floor};
	}
}
