package arathain.arcpocalypse.mixin;

import arathain.arcpocalypse.ArcpocalypseComponents;
import arathain.arcpocalypse.common.NekoArcComponent;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	public ServerPlayerEntityMixin(World world, BlockPos blockPos, float f, GameProfile gameProfile) {
		super(world, blockPos, f, gameProfile);
	}

	@Inject(method = "onDeath", at = @At("TAIL"))
	public void neko$onDeath(DamageSource source, CallbackInfo info) {
		if(this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc() && !this.getWorld().getGameRules().get(GameRules.KEEP_INVENTORY).get()) {
			System.out.println("yeah");
			this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).setArc(false);
			this.getComponent(ArcpocalypseComponents.ARC_COMPONENT).setNecoType(NekoArcComponent.TypeNeco.ARC);
		}
	}
}
