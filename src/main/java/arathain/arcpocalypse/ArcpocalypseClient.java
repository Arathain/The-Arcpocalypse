package arathain.arcpocalypse;

import arathain.arcpocalypse.client.NekoArcModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ArcpocalypseClient implements ClientModInitializer {
	public static final EntityModelLayer ARC_MODEL_LAYER = new EntityModelLayer(new Identifier(Arcpocalypse.MODID, "neko_arc"), "main");
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityModelLayerRegistry.registerModelLayer(ARC_MODEL_LAYER, NekoArcModel::getTexturedModelData);
	}
}
