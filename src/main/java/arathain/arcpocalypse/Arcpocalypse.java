package arathain.arcpocalypse;

import arathain.arcpocalypse.common.NekoArcScaleType;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.Items;
import net.minecraft.util.TypedActionResult;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Arcpocalypse implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Arcpocalypse");
	public static final String MODID = "arcpocalypse";

	@Override
	public void onInitialize(ModContainer mod) {
		NekoArcScaleType.init();
		UseItemCallback.EVENT.register((player, world, hand) -> {
			if(player.getStackInHand(hand).getItem().equals(Items.MILK_BUCKET) && player.isSneaking()) {
				System.out.println(!player.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc());
				player.getComponent(ArcpocalypseComponents.ARC_COMPONENT).setArc(!player.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc());
				return TypedActionResult.success(player.getStackInHand(hand));
			}
			return TypedActionResult.pass(player.getStackInHand(hand));
		});
	}
}
