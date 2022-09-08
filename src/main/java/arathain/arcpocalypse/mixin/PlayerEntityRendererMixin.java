package arathain.arcpocalypse.mixin;

import arathain.arcpocalypse.Arcpocalypse;
import arathain.arcpocalypse.ArcpocalypseClient;
import arathain.arcpocalypse.ArcpocalypseComponents;
import arathain.arcpocalypse.client.ModelHaver;
import arathain.arcpocalypse.client.NekoArcModel;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.feature.StuckObjectsFeatureRenderer;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.util.ScaleUtils;

import java.util.Iterator;


@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> implements ModelHaver {
	@Unique
	private static final Identifier TEXTURE = new Identifier(Arcpocalypse.MODID, "textures/entity/neko_arc.png");
	@Unique

	private NekoArcModel MODEL;

	public PlayerEntityRendererMixin(EntityRendererFactory.Context context, PlayerEntityModel<AbstractClientPlayerEntity> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Override
	public NekoArcModel getArcModel() {
		return MODEL;
	}

	@Shadow
	protected abstract void renderLabelIfPresent(AbstractClientPlayerEntity abstractClientPlayerEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i);

	@Shadow
	private static BipedEntityModel.ArmPose getArmPose(AbstractClientPlayerEntity player, Hand hand) {
		return null;
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void neko$init(EntityRendererFactory.Context context, boolean bl, CallbackInfo ci) {
		MODEL = new NekoArcModel(context.getPart(ArcpocalypseClient.ARC_MODEL_LAYER));
	}
	@Inject(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/PlayerEntityModel;setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER), cancellable = true)
	private void neko$renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
		if(player.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			NekoArcModel playerEntityModel = this.MODEL;
			arm = arm == this.model.rightArm ? playerEntityModel.rightArm : playerEntityModel.leftArm;
			BipedEntityModel.ArmPose armPose = getArmPose(player, Hand.MAIN_HAND);
			BipedEntityModel.ArmPose armPose2 = getArmPose(player, Hand.OFF_HAND);
			if (armPose.isTwoHanded()) {
				armPose2 = player.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
			}

			if (player.getMainArm() == Arm.RIGHT) {
				playerEntityModel.rightArmPose = armPose;
				playerEntityModel.leftArmPose = armPose2;
			} else {
				playerEntityModel.rightArmPose = armPose2;
				playerEntityModel.leftArmPose = armPose;
			}
			playerEntityModel.handSwingProgress = 0.0F;
			playerEntityModel.sneaking = false;
			playerEntityModel.leaningPitch = 0.0F;
			playerEntityModel.setAngles(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
			playerEntityModel.rightArm.roll -= 0.533;
			playerEntityModel.leftArm.roll += 0.533;
			matrices.scale(1.5F, 1.5F, 1.5F);
			arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, OverlayTexture.DEFAULT_UV);
			ci.cancel();
		}
	}

	@Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	private void neko$render(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo callbackInfo) {
		if(player.getComponent(ArcpocalypseComponents.ARC_COMPONENT).isArc()) {
			BipedEntityModel.ArmPose armPose = getArmPose(player, Hand.MAIN_HAND);
			BipedEntityModel.ArmPose armPose2 = getArmPose(player, Hand.OFF_HAND);
			if (armPose.isTwoHanded()) {
				armPose2 = player.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
			}

			if (player.getMainArm() == Arm.RIGHT) {
				this.MODEL.rightArmPose = armPose;
				this.MODEL.leftArmPose = armPose2;
			} else {
				this.MODEL.rightArmPose = armPose2;
				this.MODEL.leftArmPose = armPose;
			}
			matrixStack.push();
			this.MODEL.handSwingProgress = this.getHandSwingProgress(player, tickDelta);
			this.MODEL.riding = player.hasVehicle();
			this.MODEL.child = player.isBaby();
			float h = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
			float j = MathHelper.lerpAngleDegrees(tickDelta, player.prevHeadYaw, player.headYaw);
			float k = j - h;
			float l;
			if (player.hasVehicle() && player.getVehicle() instanceof LivingEntity) {
				LivingEntity livingEntity2 = (LivingEntity)player.getVehicle();
				h = MathHelper.lerpAngleDegrees(tickDelta, livingEntity2.prevBodyYaw, livingEntity2.bodyYaw);
				k = j - h;
				l = MathHelper.wrapDegrees(k);
				if (l < -85.0F) {
					l = -85.0F;
				}

				if (l >= 85.0F) {
					l = 85.0F;
				}

				h = j - l;
				if (l * l > 2500.0F) {
					h += l * 0.2F;
				}

				k = j - h;
			}

			float m = MathHelper.lerp(tickDelta, player.prevPitch, player.getPitch());
			if (renderFlipped(player)) {
				m *= -1.0F;
				k *= -1.0F;
			}

			float n;
			if (player.hasPose(EntityPose.SLEEPING)) {
				Direction direction = player.getSleepingDirection();
				if (direction != null) {
					n = player.getEyeHeight(EntityPose.STANDING) - 0.1F;
					matrixStack.translate((double)((float)(-direction.getOffsetX()) * n), 0.0, (double)((float)(-direction.getOffsetZ()) * n));
				}
			}

			l = this.getAnimationProgress(player, tickDelta);
			this.setupTransforms(player, matrixStack, l, h, tickDelta);
			matrixStack.scale(-1.0F, -1.0F, 1.0F);
			this.scale(player, matrixStack, tickDelta);
			matrixStack.translate(0.0, -1.1, 0.0);
			n = 0.0F;
			float o = 0.0F;
			if (!player.hasVehicle() && player.isAlive()) {
				n = MathHelper.lerp(tickDelta, player.lastLimbDistance, player.limbDistance);
				o = player.limbAngle - player.limbDistance * (1.0F - tickDelta);
				if (player.isBaby()) {
					o *= 3.0F;
				}

				if (n > 1.0F) {
					n = 1.0F;
				}
			}

			this.MODEL.animateModel(player, o, n, tickDelta);
			this.MODEL.setAngles(player, o, n, l, k, m);
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			boolean bl = this.isVisible(player);
			boolean bl2 = !bl && !player.isInvisibleTo(minecraftClient.player);
			RenderLayer renderLayer = RenderLayer.getEntityTranslucent(TEXTURE);
			if (renderLayer != null) {
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
				int p = getOverlay(player, this.getAnimationCounter(player, tickDelta));
				if((!this.MODEL.eyes.visible && player.getRandom().nextFloat() > 0.90f) && !player.isSleeping() || player.isSneaking()) {
					this.MODEL.eyes.visible = true;
				}
				if(((player.getRandom().nextFloat() > 0.997f && this.MODEL.eyes.visible) || player.isSleeping()) && !player.isSneaking()) {
					this.MODEL.eyes.visible = false;
				}
				this.MODEL.render(matrixStack, vertexConsumer, light, p, 1.0F, 1.0F, 1.0F, bl2 ? 0.15F : 1.0F);
			}
			if (!player.isSpectator()) {
				Iterator var23 = this.features.iterator();

				while(var23.hasNext()) {
					FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRenderer = (FeatureRenderer)var23.next();
					if(featureRenderer instanceof HeldItemFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> || featureRenderer instanceof StuckObjectsFeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> || featureRenderer instanceof TridentRiptideFeatureRenderer<AbstractClientPlayerEntity>) {
						featureRenderer.render(matrixStack, vertexConsumerProvider, light, player, o, n, tickDelta, l, k, m);
					}
				}
			}

			matrixStack.pop();
			if (this.hasLabel(player)) {
				this.renderLabelIfPresent(player, player.getDisplayName(), matrixStack, vertexConsumerProvider, light);
			}
			callbackInfo.cancel();
		}
	}
}
