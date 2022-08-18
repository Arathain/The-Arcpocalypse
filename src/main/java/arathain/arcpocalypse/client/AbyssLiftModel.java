package arathain.arcpocalypse.client;

import arathain.arcpocalypse.common.AbyssLiftEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class AbyssLiftModel extends EntityModel<AbyssLiftEntity> {
	private final ModelPart root;
	private final ModelPart door;
	public AbyssLiftModel(ModelPart root) {
		this.root = root.getChild("root");
		this.door = this.root.getChild("door");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create().uv(0, 51).cuboid(-8.0F, -2.0F, -8.0F, 16.0F, 2.0F, 16.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-7.5F, -32.0F, -7.5F, 15.0F, 30.0F, 15.0F, new Dilation(0.0F))
				.uv(45, 0).cuboid(-1.0F, -41.0F, -4.0F, 2.0F, 3.0F, 8.0F, new Dilation(0.01F))
				.uv(0, 45).cuboid(-4.0F, -41.0F, -1.0F, 8.0F, 3.0F, 2.0F, new Dilation(0.0F))
				.uv(44, 29).cuboid(-8.0F, -38.0F, -8.0F, 16.0F, 6.0F, 16.0F, new Dilation(0.0F))
				.uv(64, 51).cuboid(-8.0F, -38.0F, -8.0F, 16.0F, 6.0F, 16.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData door = root.addChild("door", ModelPartBuilder.create().uv(65, 0).cuboid(-11.0F, -12.0F, 0.5F, 11.0F, 26.0F, 1.0F, new Dilation(0.01F)), ModelTransform.of(5.5F, -16.0F, -8.0F, 0.0F, -0.8727F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(AbyssLiftEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if(entity.hasPassengers()) {
			this.door.yaw = 0;
		} else {
			this.door.yaw = -0.8727F;
		}
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}
