package arathain.arcpocalypse.mixin;

import arathain.arcpocalypse.ArcpocalypseComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	public abstract UUID getUuid();

	@Inject(method = "isInvulnerableTo", at = @At(value = "RETURN"), cancellable = true)
	private void neko$lumiarc(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if(((Entity)(Object)this) instanceof ServerPlayerEntity plr && plr.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			if (damageSource.isExplosive() && damageSource.isMagic() && damageSource.getAttacker() != null && damageSource.getAttacker().getUuid().equals(this.getUuid())) {
				cir.setReturnValue(true);
			} else if (damageSource == DamageSource.FLY_INTO_WALL) cir.setReturnValue(true);
			//else if (damageSource.isFromFalling()) cir.setReturnValue(true);
			else cir.setReturnValue(cir.getReturnValue());
		}
	}
}
