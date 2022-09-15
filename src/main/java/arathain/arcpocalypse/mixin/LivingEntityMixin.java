package arathain.arcpocalypse.mixin;

import arathain.arcpocalypse.ArcpocalypseComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V", shift = At.Shift.AFTER))
	private void neko$travel(Vec3d movementInput, CallbackInfo ci) {
		if(((LivingEntity)(Object)this) instanceof ServerPlayerEntity plr && this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			Vec3d vec = this.getPos().subtract(this.prevX, this.prevY, this.prevZ);
			plr.stopFallFlying();
			this.world.createExplosion(this, null, null, this.getX(), this.getY(), this.getZ(), (float) vec.lengthSquared() * 3, !plr.interactionManager.getGameMode().isBlockBreakingRestricted(), plr.interactionManager.getGameMode().isBlockBreakingRestricted() ? Explosion.DestructionType.NONE : Explosion.DestructionType.BREAK);
		}
	}

	@Inject(method = "damage", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;timeUntilRegen:I", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
	private void neko$hell(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if ((((LivingEntity)(Object)this)) instanceof PlayerEntity player && player.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) this.timeUntilRegen = 15;
	}

	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", ordinal = 2, shift = At.Shift.AFTER))
	private void neko$travelVertical(Vec3d movementInput, CallbackInfo ci) {
		if(this.verticalCollision && ((LivingEntity)(Object)this) instanceof ServerPlayerEntity plr && this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			Vec3d vec = this.getPos().subtract(this.prevX, this.prevY, this.prevZ);
			plr.stopFallFlying();
			this.world.createExplosion(this, DamageSource.explosion(plr).setUsesMagic(), null, this.getX(), this.getY(), this.getZ(), (float) vec.lengthSquared() * 3 * Math.abs(plr.getPitch() / 90f), !plr.interactionManager.getGameMode().isBlockBreakingRestricted(), plr.interactionManager.getGameMode().isBlockBreakingRestricted() ? Explosion.DestructionType.NONE : Explosion.DestructionType.BREAK);
		}
	}
}
