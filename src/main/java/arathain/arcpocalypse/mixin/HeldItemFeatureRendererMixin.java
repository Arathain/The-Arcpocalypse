package arathain.arcpocalypse.mixin;

import arathain.arcpocalypse.ArcpocalypseClient;
import arathain.arcpocalypse.ArcpocalypseComponents;
import arathain.arcpocalypse.client.ModelHaver;
import arathain.arcpocalypse.client.NekoArcModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Axis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.util.ScaleUtils;

@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithArms> extends FeatureRenderer<T, M> {
	@Shadow
	@Final
	private HeldItemRenderer heldItemRenderer;

	@Unique
	private FeatureRendererContext ctx;


	public HeldItemFeatureRendererMixin(FeatureRendererContext<T, M> featureRendererContext) {
		super(featureRendererContext);
	}
	@Inject(method = "<init>", at = @At("TAIL"))
	private void neko$init(FeatureRendererContext featureRendererContext, HeldItemRenderer heldItemRenderer, CallbackInfo ci) {
		ctx = featureRendererContext;
	}


	@Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
	private void neko$modelHijackery(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if(entity instanceof PlayerEntity && entity.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc() && ctx instanceof PlayerEntityRenderer rend) {
			matrices.push();
			matrices.translate(0, -0.6, 0);
			((ModelHaver)rend).getArcModel(entity.getComponent(ArcpocalypseComponents.ARC_COMPONENT).getNecoType()).setArmAngle(arm, matrices);
			matrices.translate(0, 0.6, 0);
			matrices.multiply(Axis.X_POSITIVE.rotationDegrees(-90.0F));
			matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(180.0F));
			boolean bl = arm == Arm.LEFT;
			matrices.translate((double)((float)(bl ? -8 : 8) / 16.0F), 0.425, 0.25);
			this.heldItemRenderer.renderItem(entity, stack, transformationMode, bl, matrices, vertexConsumers, light);
			matrices.pop();
			ci.cancel();
		}
	}
}
