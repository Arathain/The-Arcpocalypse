package arathain.arcpocalypse.mixin;

import arathain.arcpocalypse.Arcpocalypse;
import arathain.arcpocalypse.ArcpocalypseComponents;
import arathain.arcpocalypse.ArcpocalypseConfig;
import arathain.arcpocalypse.client.NecoArcNoiseClientCode;
import arathain.arcpocalypse.common.ArcpocalypseSoundEvents;
import arathain.arcpocalypse.common.NekoArcComponent;
import net.fabricmc.api.EnvType;
import net.minecraft.block.*;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
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
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.lodestone.systems.rendering.particle.Easing;
import team.lodestar.lodestone.systems.rendering.particle.WorldParticleBuilder;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.SpinParticleData;

import java.awt.*;

import static team.lodestar.lodestone.setup.LodestoneParticles.SMOKE_PARTICLE;
import static team.lodestar.lodestone.setup.LodestoneParticles.STAR_PARTICLE;
//import static com.sammy.ortus.setup.OrtusParticles.*;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Unique
	private int ambientSoundChance;

	@Shadow
	public abstract void damageShield(float amount);

	@Shadow public abstract void stopFallFlying();

	@Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	@Inject(method = "getActiveEyeHeight", at = @At("HEAD"), cancellable = true)
	private void necoEyes(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> cir) {
		try {
			if (ArcpocalypseComponents.ARC_COMPONENT != null && this.getComponent(ArcpocalypseComponents.ARC_COMPONENT) != null && this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
				cir.setReturnValue(dimensions.height - 0.3f);
			}
		} catch(Throwable ignored) {

		}
	}

	@Unique
	private void neko$burn(PlayerEntity playerEntity, BlockPos blockPos, Direction side) {
		if(playerEntity instanceof ServerPlayerEntity sr && !sr.interactionManager.getGameMode().isBlockBreakingRestricted()) {
			BlockState blockState = getWorld().getBlockState(blockPos);
			if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
				BlockPos blockPos2 = blockPos.offset(side);
				if (AbstractFireBlock.canPlaceAt(getWorld(), blockPos2, side.getOpposite())) {
					getWorld().playSound(playerEntity, blockPos2, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, getWorld().getRandom().nextFloat() * 0.4F + 0.8F);
					BlockState blockState2 = AbstractFireBlock.getState(getWorld(), blockPos2);
					getWorld().setBlockState(blockPos2, blockState2, 11);
					getWorld().emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
				}
			} else {
				getWorld().playSound(playerEntity, blockPos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, getWorld().getRandom().nextFloat() * 0.4F + 0.8F);
				getWorld().setBlockState(blockPos, blockState.with(Properties.LIT, true), 11);
				getWorld().emitGameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
			}
		}
	}
	@Inject(method = "getDeathSound()Lnet/minecraft/sound/SoundEvent;", at = @At("HEAD"), cancellable = true)
	private void neko$death(CallbackInfoReturnable<SoundEvent> cir) {
		if(this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			cir.setReturnValue(ArcpocalypseSoundEvents.getNecoDeath(this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).getNecoType()));
		}
	}
	@Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
	private void neko$hurt(CallbackInfoReturnable<SoundEvent> cir) {
		if(this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			cir.setReturnValue(ArcpocalypseSoundEvents.getNecoHurt(this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).getNecoType()));
		}
	}

	@Override
	public float getSoundPitch() {
		if (this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			return 1;
		} else return super.getSoundPitch();
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void neko$tick(CallbackInfo info) {
		if(this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			NekoArcComponent.TypeNeco necoType = this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).getNecoType();
			if (this.isAlive() && this.random.nextInt(8000) < this.ambientSoundChance++) {
				this.ambientSoundChance = -100;
				if (MinecraftQuiltLoader.getEnvironmentType() == EnvType.CLIENT) {
					NecoArcNoiseClientCode.packetProxy(((PlayerEntity) (Object) this), this.getLastAttackTime());
				}
			}

			if(this.isSneaking() && this.isSprinting() && ArcpocalypseConfig.getCurrentNetworkSyncableConfig().enableLasers() != ArcpocalypseConfig.ArcAbilitySettings.DISABLED) {
				Vec3d rotation = this.getRotationVector();
				if(getWorld().getTime() % 14 == 0) {
					this.playSound(ArcpocalypseSoundEvents.ENTITY_NECO_BEAM, this.getSoundVolume(), 1);
				}
				if(getWorld().isClient) {
					WorldParticleBuilder builder = WorldParticleBuilder.create(SMOKE_PARTICLE)
						.setScaleData(GenericParticleData.create(0.2f + random.nextFloat() * 0.1f, 0).build())
						.setLifetime(20 + random.nextInt(10))
						.setTransparencyData(GenericParticleData.create(1.0f, 0.0f).build())
						.setColorData(ColorParticleData.create(Color.RED, Arcpocalypse.BURUNYUU_LASER).setCoefficient(1.0f).setEasing(Easing.CIRC_OUT).build())
						.setSpinData(SpinParticleData.create(0, 0.4f).setSpinOffset((getWorld().getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
						.enableNoClip();
					Vec3d rEye = this.getEyePos().subtract(0, 0.12, 0).add(new Vec3d(-0.1, 0.2, 0.27).rotateX((float) (this.getPitch() * -Math.PI / 180f)).rotateY((float) (this.getHeadYaw() * -Math.PI / 180)));
					Vec3d lEye = this.getEyePos().subtract(0, 0.12, 0).add(new Vec3d(0.1, 0.2, 0.27).rotateX((float) (this.getPitch() * -Math.PI / 180f)).rotateY((float) (this.getHeadYaw() * -Math.PI / 180)));
					for(int i = 0; i < 65; i++) {
						HitResult hitResult = Arcpocalypse.hitscanBlock(getWorld(), this, 64, RaycastContext.FluidHandling.WATER, block -> !block.equals(Blocks.AIR));
						if(hitResult != null && this.getPos().relativize(hitResult.getPos()).length() < i) {
							break;
						}
						hitResult = Arcpocalypse.hitscanEntity(getWorld(), this, 64, entity -> true);
						if(hitResult != null && this.getPos().relativize(hitResult.getPos()).length() < i) {
							break;
						}
						Vec3d rot = rotation.multiply(i);
						builder.spawn(getWorld(), rEye.x + rot.x, rEye.y + rot.y, rEye.z + rot.z);
						builder.spawn(getWorld(), lEye.x + rot.x, lEye.y + rot.y, lEye.z + rot.z);
					}
				} else {
					EntityHitResult hit = Arcpocalypse.hitscanEntity(getWorld(), this, 64, entity -> !entity.isFireImmune());
					if(hit != null && ArcpocalypseConfig.getCurrentNetworkSyncableConfig().enableLasers() != ArcpocalypseConfig.ArcAbilitySettings.FULL_SAFE) {
						if(hit.getEntity() instanceof LivingEntity living && living.isBlocking()) {
							Vec3d vec3d2 = living.getRotationVec(1.0F);
							Vec3d vec3d3 = this.getPos().relativize(living.getPos()).normalize();
							vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z);
							if (vec3d3.dotProduct(vec3d2) < 0.0) {
								hit.getEntity().setOnFireFor(4);
								hit.getEntity().damage(this.getDamageSources().onFire(), 1);
								hit.getEntity().timeUntilRegen = 0;
							}
						} else {
							hit.getEntity().setOnFireFor(4);
							hit.getEntity().damage(this.getDamageSources().onFire(), 1);
							hit.getEntity().timeUntilRegen = 0;
						}
					} else {
						if (ArcpocalypseConfig.getCurrentNetworkSyncableConfig().enableLasers() != ArcpocalypseConfig.ArcAbilitySettings.WORLD_SAFE && ArcpocalypseConfig.getCurrentNetworkSyncableConfig().enableLasers() != ArcpocalypseConfig.ArcAbilitySettings.FULL_SAFE) {
							BlockHitResult hitResult = Arcpocalypse.hitscanBlock(getWorld(), this, 64, RaycastContext.FluidHandling.WATER, block -> !block.equals(Blocks.AIR));
							if (hitResult != null) {
								neko$burn(((PlayerEntity) (Object) this), hitResult.getBlockPos(), hitResult.getSide());
							}
						}
					}
				}
			}
			if (this.isFallFlying()) {
				//this.stopFallFlying();
				Vec3d rotation = this.getRotationVector();
				Vec3d velocity = this.getVelocity();
				float speed = (0.15F * (this.getPitch() < -75 && this.getPitch() > -105 ? 2.75F : 1F));

				this.setVelocity(velocity.add(rotation.x * speed + (rotation.x * 1.5D - velocity.x) * speed,
						rotation.y * speed + (rotation.y * 1.5D - velocity.y) * speed,
						rotation.z * speed + (rotation.z * 1.5D - velocity.z) * speed));
				if (getWorld().isClient) {
					Vec3d vector = new Vec3d(this.getX() + this.getRotationVector().negate().multiply(0.3).x, this.getY() + this.getRotationVector().negate().multiply(0.3).y, this.getZ() + this.getRotationVector().negate().multiply(0.3).z);
					WorldParticleBuilder.create(SMOKE_PARTICLE)
							.setScaleData(GenericParticleData.create(0.5f + random.nextFloat() * 0.1f, 0).build())
							.setLifetime(50 + random.nextInt(10))
							.setTransparencyData(GenericParticleData.create(0.9f, 0.0f).build())
							.setColorData(ColorParticleData.create(Color.ORANGE, Color.RED).setCoefficient(0.8f).setEasing(Easing.CIRC_OUT).build())
							.setSpinData(SpinParticleData.create(0, 0.4f).setSpinOffset((getWorld().getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
							.addMotion(0, 0f - random.nextFloat() * 0.01f, 0)
							.enableNoClip()
							.spawn(getWorld(), vector.x, vector.y, vector.z);
					WorldParticleBuilder.create(STAR_PARTICLE)
							.setScaleData(GenericParticleData.create(0.7f + random.nextFloat() * 0.2f, 0).build())
							.setLifetime(20 + random.nextInt(6))
							.setTransparencyData(GenericParticleData.create(0.9f, 0.0f).build())
							.setColorData(ColorParticleData.create(Color.YELLOW, Color.ORANGE).setCoefficient(0.8f).setEasing(Easing.CIRC_OUT).build())
							.setSpinData(SpinParticleData.create(0, 0.4f).setSpinOffset((getWorld().getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
							.addMotion(0, 0f - random.nextFloat() * 0.01f, 0)
							.enableNoClip()
							.spawn(getWorld(), vector.x, vector.y, vector.z);
				}
			}
		}
	}
}
