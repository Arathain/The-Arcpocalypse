package arathain.arcpocalypse.client;

import arathain.arcpocalypse.Arcpocalypse;
import arathain.arcpocalypse.ArcpocalypseClient;
import arathain.arcpocalypse.common.AbyssLiftEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AbyssLiftRenderer extends EntityRenderer<AbyssLiftEntity> {
	private static final Identifier TEXTURE = new Identifier(Arcpocalypse.MODID, "textures/entity/abyss_lift.png");
	private static final Identifier CHAIN_TEXTURE = new Identifier(Arcpocalypse.MODID, "textures/entity/lift_chain.png");
	public AbyssLiftModel model;
	private Vector3f pos_x = new Vector3f(1, 0, 0);

	public AbyssLiftRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.shadowRadius = 0F;
		this.shadowOpacity = 0F;
		this.model = new AbyssLiftModel(context.getPart(ArcpocalypseClient.LIFT_MODEL_LAYER));
	}

	@Override
	public Identifier getTexture(AbyssLiftEntity entity) {
		return TEXTURE;
	}

	@Override
	public void render(AbyssLiftEntity entity, float yaw, float tickDelta, MatrixStack stack, VertexConsumerProvider vertexConsumers, int light) {
		model.setAngles(entity, tickDelta, 0F, entity.age + tickDelta, entity.getYaw(), entity.getPitch());
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE));
		stack.multiply(Axis.X_POSITIVE.rotationDegrees(180));
		stack.multiply(Axis.Y_POSITIVE.rotationDegrees(yaw));
		stack.translate(0, -1.4, 0);
		model.render(stack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		stack.multiply(Axis.X_POSITIVE.rotationDegrees(180));
		stack.push();
		Vec3d vec = Vec3d.ofBottomCenter(entity.getTargetPos());
		float distanceX = (float) (vec.x - entity.getX());
		float distanceY = (float) (vec.y - (entity.getY() + 2));
		float distanceZ = (float) (vec.z - entity.getZ());
		stack.translate(0, 1.0, 0);

		renderChain(distanceX, distanceY, distanceZ, tickDelta, entity.age, stack, vertexConsumers, light);
		stack.pop();
		super.render(entity, yaw, tickDelta, stack, vertexConsumers, light);
	}
	public void renderChain(float x, float y, float z, float tickDelta, int age, MatrixStack stack, VertexConsumerProvider provider, int light)
	{
		float lengthXY = MathHelper.sqrt(x * x + z * z);
		float squaredLength = x * x + y * y + z * z;
		float length = MathHelper.sqrt(squaredLength);

		stack.push();
		stack.multiply(Axis.Y_POSITIVE.rotation((float) (-Math.atan2(z, x)) - 1.5707964F));
		stack.multiply(Axis.X_POSITIVE.rotation((float) (-Math.atan2(lengthXY, y)) - 1.5707964F));
		stack.multiply(Axis.Z_POSITIVE.rotationDegrees(25));
		stack.push();
		stack.translate(0.015, -0.2, 0);

		VertexConsumer vertexConsumer = provider.getBuffer(RenderLayer.getEntityCutout(CHAIN_TEXTURE));
		float vertX1 = 0F;
		float vertY1 = 0.25F;
		float vertX2 = MathHelper.sin(6.2831855F) * 0.125F;
		float vertY2 = MathHelper.cos(6.2831855F) * 0.125F;
		float minU = 0F;
		float maxU = 0.1875F;
		float minV = 0.0F;
		float maxV = MathHelper.sqrt(squaredLength) / 8F;
		MatrixStack.Entry entry = stack.peek();
		Matrix4f matrix4f = entry.getModel();
		Matrix3f matrix3f = entry.getNormal();

		vertexConsumer.vertex(matrix4f, vertX1, vertY1, 0F).color(0, 0, 0, 255).uv(minU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, vertX1, vertY1, length).color(255, 255, 255, 255).uv(minU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, vertX2, vertY2, length).color(255, 255, 255, 255).uv(maxU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, vertX2, vertY2, 0F).color(0, 0, 0, 255).uv(maxU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();

		stack.pop();
		stack.multiply(Axis.Z_POSITIVE.rotationDegrees(90));
		stack.translate(-0.015, -0.2, 0);

		entry = stack.peek();
		matrix4f = entry.getModel();
		matrix3f = entry.getNormal();

		vertexConsumer.vertex(matrix4f, vertX1, vertY1, 0F).color(0, 0, 0, 255).uv(minU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, vertX1, vertY1, length).color(255, 255, 255, 255).uv(minU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, vertX2, vertY2, length).color(255, 255, 255, 255).uv(maxU, maxV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
		vertexConsumer.vertex(matrix4f, vertX2, vertY2, 0F).color(0, 0, 0, 255).uv(maxU, minV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();

		stack.pop();
	}
}
