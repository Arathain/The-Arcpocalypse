package arathain.arcpocalypse;

import arathain.arcpocalypse.client.AbyssLiftModel;
import arathain.arcpocalypse.client.AbyssLiftRenderer;
import arathain.arcpocalypse.client.NekoArcModel;
import arathain.arcpocalypse.common.ArcpocalypseEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class ArcpocalypseClient implements ClientModInitializer {
	public static final EntityModelLayer ARC_MODEL_LAYER = new EntityModelLayer(new Identifier(Arcpocalypse.MODID, "neko_arc"), "main");
	public static final EntityModelLayer MAIDEN_ARC_MODEL_LAYER = new EntityModelLayer(new Identifier(Arcpocalypse.MODID, "maid_arc"), "main");
	public static final EntityModelLayer LIFT_MODEL_LAYER = new EntityModelLayer(new Identifier(Arcpocalypse.MODID, "lift"), "main");
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityModelLayerRegistry.registerModelLayer(ARC_MODEL_LAYER, NekoArcModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MAIDEN_ARC_MODEL_LAYER, NekoArcModel::getTexturedMaidModelData);
		EntityModelLayerRegistry.registerModelLayer(LIFT_MODEL_LAYER, AbyssLiftModel::getTexturedModelData);
		EntityRendererRegistry.register(ArcpocalypseEntities.ABYSS_LIFT, AbyssLiftRenderer::new);
	}
}
