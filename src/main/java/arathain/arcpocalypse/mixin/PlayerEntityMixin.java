package arathain.arcpocalypse.mixin;

import arathain.arcpocalypse.Arcpocalypse;
import arathain.arcpocalypse.ArcpocalypseComponents;
import com.sammy.ortus.setup.OrtusParticles;
import com.sammy.ortus.systems.rendering.particle.Easing;
import com.sammy.ortus.systems.rendering.particle.ParticleBuilders;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow
	protected abstract void damageShield(float amount);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	@Unique
	private void neko$burn(PlayerEntity playerEntity, BlockPos blockPos, Direction side) {
		if(playerEntity instanceof ServerPlayerEntity sr && !sr.interactionManager.getGameMode().isBlockBreakingRestricted()) {
			BlockState blockState = world.getBlockState(blockPos);
			if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
				BlockPos blockPos2 = blockPos.offset(side);
				if (AbstractFireBlock.canPlaceAt(world, blockPos2, side.getOpposite())) {
					world.playSound(playerEntity, blockPos2, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
					BlockState blockState2 = AbstractFireBlock.getState(world, blockPos2);
					world.setBlockState(blockPos2, blockState2, 11);
					world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
				}
			} else {
				world.playSound(playerEntity, blockPos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
				world.setBlockState(blockPos, blockState.with(Properties.LIT, true), 11);
				world.emitGameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
			}
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void neko$tick(CallbackInfo info) {
		if(this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			if(this.isSneaking() && this.isSprinting()) {
				Vec3d rotation = this.getRotationVector();
				if(world.isClient) {
					ParticleBuilders.WorldParticleBuilder builder = ParticleBuilders.create(OrtusParticles.SMOKE_PARTICLE)
							.setScale(0.2f + random.nextFloat() * 0.1f, 0)
							.setLifetime(20 + random.nextInt(10))
							.setAlpha(1.0f, 0.0f)
							.setColor(Color.RED, Arcpocalypse.BURUNYUU_LASER)
							.setColorCoefficient(1.0f)
							.setColorEasing(Easing.CIRC_OUT)
							.setSpinOffset((world.getTime() * 0.2f) % 6.28f)
							.setSpin(0, 0.4f)
							.setSpinEasing(Easing.QUARTIC_IN)
							.enableNoClip();
					Vec3d rEye = this.getEyePos().subtract(0, 0.12, 0).add(new Vec3d(-0.1, 0.2, 0.27).rotateX((float) (this.getPitch() * -Math.PI / 180f)).rotateY((float) (this.getHeadYaw() * -Math.PI / 180)));
					Vec3d lEye = this.getEyePos().subtract(0, 0.12, 0).add(new Vec3d(0.1, 0.2, 0.27).rotateX((float) (this.getPitch() * -Math.PI / 180f)).rotateY((float) (this.getHeadYaw() * -Math.PI / 180)));
					for(int i = 0; i < 65; i++) {
						HitResult hitResult = Arcpocalypse.hitscanBlock(world, this, 64, RaycastContext.FluidHandling.WATER, block -> !block.equals(Blocks.AIR));
						if(hitResult != null && this.getPos().relativize(hitResult.getPos()).length() < i) {
							break;
						}
						hitResult = Arcpocalypse.hitscanEntity(world, this, 64, entity -> true);
						if(hitResult != null && this.getPos().relativize(hitResult.getPos()).length() < i) {
							break;
						}
						Vec3d rot = rotation.multiply(i);
						builder.spawn(world, rEye.x + rot.x, rEye.y + rot.y, rEye.z + rot.z);
						builder.spawn(world, lEye.x + rot.x, lEye.y + rot.y, lEye.z + rot.z);
					}
				} else {
					EntityHitResult hit = Arcpocalypse.hitscanEntity(world, this, 64, entity -> !entity.isFireImmune());
					if(hit != null) {
						if(hit.getEntity() instanceof LivingEntity living && living.isBlocking()) {
							Vec3d vec3d2 = living.getRotationVec(1.0F);
							Vec3d vec3d3 = this.getPos().relativize(living.getPos()).normalize();
							vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z);
							if (vec3d3.dotProduct(vec3d2) < 0.0) {
								living.getActiveItem().damage(4, this, (playerEntity) -> {
									playerEntity.sendToolBreakStatus(living.getActiveHand());
								});
							} else {
								hit.getEntity().setOnFireFor(4);
								hit.getEntity().damage(DamageSource.ON_FIRE, 1);
								hit.getEntity().timeUntilRegen = 0;
							}
						} else {
							hit.getEntity().setOnFireFor(4);
							hit.getEntity().damage(DamageSource.ON_FIRE, 1);
							hit.getEntity().timeUntilRegen = 0;
						}
					} else {
						BlockHitResult hitResult = Arcpocalypse.hitscanBlock(world, this, 64, RaycastContext.FluidHandling.WATER, block -> !block.equals(Blocks.AIR));
						if(hitResult != null) {
							neko$burn(((PlayerEntity)(Object)this), hitResult.getBlockPos(), hitResult.getSide());
						}
					}
				}
			}
			if (this.isFallFlying()) {
				Vec3d rotation = this.getRotationVector();
				Vec3d velocity = this.getVelocity();
				float speed = (0.15F * (this.getPitch() < -75 && this.getPitch() > -105 ? 2.75F : 1F));

				this.setVelocity(velocity.add(rotation.x * speed + (rotation.x * 1.5D - velocity.x) * speed,
						rotation.y * speed + (rotation.y * 1.5D - velocity.y) * speed,
						rotation.z * speed + (rotation.z * 1.5D - velocity.z) * speed));
				if (world.isClient) {
					Vec3d vector = new Vec3d(this.getX() + this.getRotationVector().negate().multiply(0.3).x, this.getY() + this.getRotationVector().negate().multiply(0.3).y, this.getZ() + this.getRotationVector().negate().multiply(0.3).z);
					ParticleBuilders.create(OrtusParticles.SMOKE_PARTICLE)
							.setScale(0.5f + random.nextFloat() * 0.1f, 0)
							.setLifetime(50 + random.nextInt(10))
							.setAlpha(0.9f, 0.0f)
							.setColor(Color.ORANGE, Color.RED)
							.setColorCoefficient(0.8f)
							.setColorEasing(Easing.CIRC_OUT)
							.setSpinOffset((world.getTime() * 0.2f) % 6.28f)
							.setSpin(0, 0.4f)
							.setSpinEasing(Easing.QUARTIC_IN)
							.addMotion(0, 0f - random.nextFloat() * 0.01f, 0)
							.enableNoClip()
							.spawn(world, vector.x, vector.y, vector.z);
					ParticleBuilders.create(OrtusParticles.STAR_PARTICLE)
							.setScale(0.7f + random.nextFloat() * 0.2f, 0)
							.setLifetime(20 + random.nextInt(6))
							.setAlpha(0.9f, 0.0f)
							.setColor(Color.YELLOW, Color.ORANGE)
							.setColorCoefficient(0.8f)
							.setColorEasing(Easing.CIRC_OUT)
							.setSpinOffset((world.getTime() * 0.2f) % 6.28f)
							.setSpin(0, 0.4f)
							.setSpinEasing(Easing.QUARTIC_IN)
							.addMotion(0, 0f - random.nextFloat() * 0.01f, 0)
							.enableNoClip()
							.spawn(world, vector.x, vector.y, vector.z);
				}
			}
		}
	}
}
