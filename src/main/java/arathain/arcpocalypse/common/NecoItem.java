package arathain.arcpocalypse.common;

import arathain.arcpocalypse.ArcpocalypseComponents;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class NecoItem extends Item {
	private final NekoArcComponent.TypeNeco neco;

	public NecoItem(Settings settings, NekoArcComponent.TypeNeco neco) {
		super(settings);
		this.neco = neco;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (user.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			user.getComponent(ArcpocalypseComponents.ARC_COMPONENT).setNecoType(neco);
			//Explosion explosion = new Explosion(world, user, DamageSource.MAGIC, new ExplosionBehavior(), user.getX(), user.getY(), user.getZ(), 2, false, Explosion.DestructionType.NONE);
			return TypedActionResult.success(user.getStackInHand(hand));
		} else {
			return super.use(world, user, hand);
		}
	}

	public class NecoExplosionBehavior extends ExplosionBehavior {
		@Override
		public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
			return false;
		}
	}
}
