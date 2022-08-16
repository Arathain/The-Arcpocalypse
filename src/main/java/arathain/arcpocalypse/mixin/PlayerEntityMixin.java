package arathain.arcpocalypse.mixin;

import arathain.arcpocalypse.ArcpocalypseComponents;
import com.sammy.ortus.setup.OrtusParticles;
import com.sammy.ortus.systems.rendering.particle.Easing;
import com.sammy.ortus.systems.rendering.particle.ParticleBuilders;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void neko$tick(CallbackInfo info) {
		if(this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc() && this.isFallFlying()) {
			Vec3d rotation = this.getRotationVector();
			Vec3d velocity = this.getVelocity();
			float speed = (0.15F * (this.getPitch() < -75 && this.getPitch() > -105 ? 2.75F : 1F));

			this.setVelocity(velocity.add(rotation.x * speed + (rotation.x * 1.5D - velocity.x) * speed,
					rotation.y * speed + (rotation.y * 1.5D - velocity.y) * speed,
					rotation.z * speed + (rotation.z * 1.5D - velocity.z) * speed));
			if(world.isClient) {
				Vec3d vector = new Vec3d(this.getX() + this.getRotationVector().negate().multiply(0.3).x, this.getY() + this.getRotationVector().negate().multiply(0.3).y, this.getZ() + this.getRotationVector().negate().multiply(0.3).z);
				ParticleBuilders.create(OrtusParticles.SMOKE_PARTICLE)
						.setScale(0.5f + random.nextFloat() * 0.1f, 0)
						.setLifetime(40 + random.nextInt(10))
						.setAlpha(0.8f, 0.0f)
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
						.setLifetime(12 + random.nextInt(6))
						.setAlpha(0.8f, 0.0f)
						.setColor(Color.YELLOW, Color.YELLOW)
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
