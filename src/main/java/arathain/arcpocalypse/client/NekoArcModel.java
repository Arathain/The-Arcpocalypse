package arathain.arcpocalypse.client;

import arathain.arcpocalypse.Arcpocalypse;
import arathain.arcpocalypse.ArcpocalypseComponents;
import arathain.arcpocalypse.common.AbyssLiftEntity;
import arathain.arcpocalypse.common.NekoArcComponent;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class NekoArcModel extends BipedEntityModel<PlayerEntity> {
	public final ModelPart eyes;
	private final ModelPart tail;
	public NekoArcModel(ModelPart root) {
		super(root);
		this.eyes = this.head.getChild("eyes");
		this.tail = root.getChild("tail");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData hat = modelPartData.addChild("hat", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 10.0F, 0.0F));

		ModelPartData ear_r1 = hat.addChild("ear_r1", ModelPartBuilder.create().uv(43, 12).cuboid(3.0F, -1.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData ear_r2 = hat.addChild("ear_r2", ModelPartBuilder.create().uv(45, 22).cuboid(-6.0F, -1.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 13).cuboid(-4.0F, -7.0F, -3.0F, 8.0F, 7.0F, 6.0F, new Dilation(0.0F))
				.uv(0, 51).cuboid(-3.5F, -3.5F, -4.01F, 7.0F, 4.0F, 1.0F, new Dilation(-1.0F)), ModelTransform.pivot(0.0F, 10.0F, 0.0F));

		ModelPartData eyes = head.addChild("eyes", ModelPartBuilder.create().uv(45, 0).cuboid(-3.8F, -4.0F, -3.5F, 3.0F, 2.0F, 1.0F, new Dilation(0.3F))
				.uv(54, 0).cuboid(-3.8F, -4.0F, -3.6F, 3.0F, 2.0F, 1.0F, new Dilation(0.4F))
				.uv(22, 13).cuboid(0.8F, -4.0F, -3.5F, 3.0F, 2.0F, 1.0F, new Dilation(0.3F))
				.uv(31, 0).cuboid(0.8F, -4.0F, -3.6F, 3.0F, 2.0F, 1.0F, new Dilation(0.4F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData hair_r1 = head.addChild("hair_r1", ModelPartBuilder.create().uv(47, 44).cuboid(-2.0F, -1.0F, -3.0F, 2.0F, 8.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(4.0F, -7.0F, 0.0F, 0.0F, 0.0F, -0.3054F));

		ModelPartData hair_r2 = head.addChild("hair_r2", ModelPartBuilder.create().uv(47, 20).cuboid(0.0F, -1.0F, -3.0F, 2.0F, 8.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(-4.0F, -7.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

		ModelPartData morehair_r1 = head.addChild("morehair_r1", ModelPartBuilder.create().uv(25, 23).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 8.0F, 3.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, -7.0F, 3.0F, 0.3491F, 0.0F, 0.0F));

		ModelPartData HeadLayer_r1 = head.addChild("HeadLayer_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -5.0F, -3.0F, 8.0F, 7.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, -2.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(28, 13).cuboid(-3.0F, -6.0F, -1.5F, 6.0F, 7.0F, 3.0F, new Dilation(0.0F))
				.uv(46, 16).cuboid(-1.0F, -7.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
				.uv(54, 16).cuboid(-1.0F, -6.4F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.2F)), ModelTransform.pivot(0.0F, 7.0F, 0.0F));

		ModelPartData skirfront_r1 = body.addChild("skirfront_r1", ModelPartBuilder.create().uv(0, 59).cuboid(-3.0F, 0.0F, -1.5F, 6.0F, 3.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, -1.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		ModelPartData skirtleft_r1 = body.addChild("skirtleft_r1", ModelPartBuilder.create().uv(38, 0).cuboid(-0.975F, -0.1F, -1.5F, 2.0F, 3.0F, 3.0F, new Dilation(-0.001F)), ModelTransform.of(2.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		ModelPartData skirtright_r1 = body.addChild("skirtright_r1", ModelPartBuilder.create().uv(13, 38).cuboid(-1.025F, -0.1F, -1.5F, 2.0F, 3.0F, 3.0F, new Dilation(-0.001F)), ModelTransform.of(-2.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		ModelPartData skirtback_r1 = body.addChild("skirtback_r1", ModelPartBuilder.create().uv(16, 59).cuboid(-3.0F, 0.0F, -0.5F, 6.0F, 3.0F, 2.0F, new Dilation(-0.0001F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(45, 44).cuboid(-1.0F, -1.2F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(16, 44).cuboid(-1.0F, -1.2F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.2F))
				.uv(10, 26).cuboid(-1.5F, 2.8F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 13.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(39, 38).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
				.uv(0, 3).cuboid(-1.0F, 4.0F, -2.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 39).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.2F)), ModelTransform.pivot(-1.9F, 18.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(31, 38).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-1.0F, 4.0F, -2.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(23, 38).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.2F)), ModelTransform.pivot(1.9F, 18.0F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(8, 44).cuboid(-1.0F, -1.2F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(44, 6).cuboid(-1.0F, -1.2F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.2F))
				.uv(22, 0).cuboid(-1.5F, 2.8F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 13.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		ModelPartData tail = modelPartData.addChild("tail", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 17.0F, 1.0F));

		ModelPartData cube_r1 = tail.addChild("cube_r1", ModelPartBuilder.create().uv(32, 56).cuboid(-0.5F, -1.0F, -0.7F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 4.2F, 0.0F, -0.7418F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	public static TexturedModelData getTexturedMaidModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData hat = modelPartData.addChild("hat", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 10.0F, 0.0F));

		ModelPartData leftear_r1 = hat.addChild("leftear_r1", ModelPartBuilder.create().uv(28, 37).cuboid(3.0F, -1.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData ear_r1 = hat.addChild("ear_r1", ModelPartBuilder.create().uv(28, 33).cuboid(-6.0F, -1.0F, -0.5F, 3.0F, 3.0F, 1.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, -7.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 9).cuboid(-4.0F, -7.0F, -3.0F, 8.0F, 7.0F, 6.0F, new Dilation(0.0F))
				.uv(0, 44).cuboid(-3.5F, -3.5F, -4.01F, 7.0F, 4.0F, 1.0F, new Dilation(-1.0F))
				.uv(34, 49).cuboid(-5.0F, -10.0F, -2.0F, 10.0F, 5.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 10.0F, 0.0F));

		ModelPartData hair_r1 = head.addChild("hair_r1", ModelPartBuilder.create().uv(44, 26).cuboid(-2.0F, -1.0F, -3.0F, 2.0F, 8.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(4.0F, -7.0F, 0.0F, 0.0F, 0.0F, -0.3054F));

		ModelPartData otherhair_r1 = head.addChild("otherhair_r1", ModelPartBuilder.create().uv(46, 12).cuboid(0.0F, -1.0F, -3.0F, 2.0F, 8.0F, 6.0F, new Dilation(0.1F)), ModelTransform.of(-4.0F, -7.0F, 0.0F, 0.0F, 0.0F, 0.3054F));

		ModelPartData bowtie_r1 = head.addChild("bowtie_r1", ModelPartBuilder.create().uv(8, 22).cuboid(-3.0F, 1.0F, -0.8F, 6.0F, 4.0F, 1.0F, new Dilation(0.25F))
				.uv(22, 23).cuboid(-4.0F, 0.0F, -3.0F, 8.0F, 7.0F, 3.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, -7.0F, 3.0F, 0.3491F, 0.0F, 0.0F));

		ModelPartData HeadLayer_r1 = head.addChild("HeadLayer_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -5.0F, -3.0F, 8.0F, 3.0F, 6.0F, new Dilation(0.3F)), ModelTransform.of(0.0F, -2.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

		ModelPartData eyes = head.addChild("eyes", ModelPartBuilder.create().uv(48, 3).cuboid(-3.8F, -4.0F, -3.5F, 3.0F, 2.0F, 1.0F, new Dilation(0.3F))
				.uv(56, 0).cuboid(-3.8F, -4.0F, -3.6F, 3.0F, 2.0F, 1.0F, new Dilation(0.4F))
				.uv(56, 3).cuboid(0.8F, -4.0F, -3.5F, 3.0F, 2.0F, 1.0F, new Dilation(0.3F))
				.uv(48, 0).cuboid(0.8F, -4.0F, -3.6F, 3.0F, 2.0F, 1.0F, new Dilation(0.4F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(28, 13).cuboid(-3.0F, -6.0F, -1.5F, 6.0F, 7.0F, 3.0F, new Dilation(0.0F))
				.uv(47, 6).cuboid(-1.0F, -7.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
				.uv(31, 0).cuboid(-1.0F, -6.4F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.2F))
				.uv(16, 41).cuboid(-3.0F, -6.0F, -1.5F, 6.0F, 7.0F, 3.0F, new Dilation(0.1F))
				.uv(1, 58).cuboid(-3.0F, -6.2F, -2.2F, 6.0F, 3.0F, 1.0F, new Dilation(-0.5F)), ModelTransform.pivot(0.0F, 17.0F, 0.0F));

		ModelPartData skirtoverlayfront_r1 = body.addChild("skirtoverlayfront_r1", ModelPartBuilder.create().uv(0, 37).cuboid(-3.0F, 0.0F, -1.5F, 6.0F, 5.0F, 2.0F, new Dilation(0.05F))
				.uv(0, 50).cuboid(-3.0F, 0.0F, -1.5F, 6.0F, 6.0F, 2.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, -1.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		ModelPartData skirtoverlayleft_r1 = body.addChild("skirtoverlayleft_r1", ModelPartBuilder.create().uv(34, 41).cuboid(-0.975F, -0.1F, -1.5F, 2.0F, 5.0F, 3.0F, new Dilation(0.01F))
				.uv(0, 28).cuboid(-0.975F, -0.1F, -1.5F, 2.0F, 6.0F, 3.0F, new Dilation(-0.001F)), ModelTransform.of(2.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		ModelPartData skirtoverlayright_r1 = body.addChild("skirtoverlayright_r1", ModelPartBuilder.create().uv(44, 41).cuboid(-1.025F, -0.1F, -1.5F, 2.0F, 5.0F, 3.0F, new Dilation(0.01F))
				.uv(10, 28).cuboid(-1.025F, -0.1F, -1.5F, 2.0F, 6.0F, 3.0F, new Dilation(-0.001F)), ModelTransform.of(-2.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

		ModelPartData skirtback_r1 = body.addChild("skirtback_r1", ModelPartBuilder.create().uv(16, 51).cuboid(-3.0F, 0.0F, -0.5F, 6.0F, 6.0F, 2.0F, new Dilation(-0.0001F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(0, 22).cuboid(-1.0F, -1.2F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(39, 0).cuboid(-1.0F, -1.2F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.2F))
				.uv(52, 6).cuboid(-1.5F, 2.8F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-3.0F, 13.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		ModelPartData right_leg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(36, 33).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
				.uv(29, 6).cuboid(-1.0F, 4.0F, -2.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.9F, 18.0F, 0.0F));

		ModelPartData left_leg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(20, 33).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new Dilation(0.0F))
				.uv(29, 9).cuboid(-1.0F, 4.0F, -2.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.9F, 18.0F, 0.0F));

		ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(37, 6).cuboid(-1.0F, -1.2F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(44, 10).cuboid(-1.0F, -1.2F, -1.0F, 2.0F, 4.0F, 2.0F, new Dilation(0.2F))
				.uv(22, 0).cuboid(-1.5F, 2.8F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 13.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		ModelPartData tail = modelPartData.addChild("tail", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 17.0F, 1.0F));

		ModelPartData tail_r1 = tail.addChild("tail_r1", ModelPartBuilder.create().uv(28, 5).cuboid(-0.5F, -1.0F, -0.7F, 1.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 4.1F, 0, -0.7418F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	@Override
	public void setAngles(PlayerEntity livingEntity, float f, float g, float h, float i, float j) {
		if (livingEntity.hasVehicle() && livingEntity.getVehicle() instanceof AbyssLiftEntity) {
			this.riding = false;
		}
		boolean bl = livingEntity.getRoll() > 4;
		boolean bl2 = livingEntity.isInSwimmingPose();
		this.head.yaw = i * 0.017453292F;
		if (bl) {
			this.head.pitch = -0.7853982F;
		} else if (this.leaningPitch > 0.0F) {
			if (bl2) {
				this.head.pitch = this.lerpAngle(this.leaningPitch, this.head.pitch, -0.7853982F);
			} else {
				this.head.pitch = this.lerpAngle(this.leaningPitch, this.head.pitch, j * 0.017453292F);
			}
		} else {
			this.head.pitch = j * 0.017453292F;
		}

		this.body.yaw = 0.0F;
		this.rightArm.pivotZ = 0.0F;
		this.rightArm.pivotX = -3.0F;
		this.leftArm.pivotZ = 0.0F;
		this.leftArm.pivotX = 3.0F;
		float k = 1.0F;
		if (bl) {
			k = (float)livingEntity.getVelocity().lengthSquared();
			k /= 0.2F;
			k *= k * k;
		}

		if (k < 1.0F) {
			k = 1.0F;
		}

		this.rightArm.pitch = MathHelper.cos(f * 0.6662F + 3.1415927F) * 2.0F * g * 0.5F / k;
		this.leftArm.pitch = MathHelper.cos(f * 0.6662F) * 2.0F * g * 0.5F / k;
		this.rightArm.roll = 0.0F;
		this.leftArm.roll = 0.0F;
		this.rightLeg.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g / k;
		this.leftLeg.pitch = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * g / k;
		this.rightLeg.yaw = 0.0F;
		this.leftLeg.yaw = 0.0F;
		this.rightLeg.roll = 0.0F;
		this.leftLeg.roll = 0.0F;
		ModelPart var10000;
		if (this.riding) {
			var10000 = this.rightArm;
			var10000.pitch += -0.62831855F;
			var10000 = this.leftArm;
			var10000.pitch += -0.62831855F;
			this.rightLeg.pitch = -1.4137167F;
			this.rightLeg.yaw = 0.31415927F;
			this.rightLeg.roll = 0.07853982F;
			this.leftLeg.pitch = -1.4137167F;
			this.leftLeg.yaw = -0.31415927F;
			this.leftLeg.roll = -0.07853982F;
		}

		this.rightArm.yaw = 0.0F;
		this.leftArm.yaw = 0.0F;
		boolean bl3 = livingEntity.getMainArm() == Arm.RIGHT;
		boolean bl4;
		if (livingEntity.isUsingItem()) {
			bl4 = livingEntity.getActiveHand() == Hand.MAIN_HAND;
			if (bl4 == bl3) {
				this.positionRightArm(livingEntity);
			} else {
				this.positionLeftArm(livingEntity);
			}
		} else {
			bl4 = bl3 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
			if (bl3 != bl4) {
				this.positionLeftArm(livingEntity);
				this.positionRightArm(livingEntity);
			} else {
				this.positionRightArm(livingEntity);
				this.positionLeftArm(livingEntity);
			}
		}

		this.animateArms(livingEntity, h);
		if (this.sneaking) {
			this.body.pitch = 0.5F;
			var10000 = this.rightArm;
			var10000.pitch += 0.4F;
			var10000 = this.leftArm;
			var10000.pitch += 0.4F;
			this.rightLeg.pivotZ = 4.0F;
			this.leftLeg.pivotZ = 4.0F;
			this.rightLeg.pivotY = 12.1F;
			this.leftLeg.pivotY = 12.1F;
			this.head.pivotY = 4.2F;
			this.body.pivotY = 11.2F;
			this.leftArm.pivotY = 6.2F;
			this.rightArm.pivotY = 6.2F;
		} else {
			this.body.pitch = 0.0F;
			this.rightLeg.pivotZ = 0F;
			this.leftLeg.pivotZ = 0F;
			this.rightLeg.pivotY = 12F;
			this.leftLeg.pivotY = 12F;
			this.head.pivotY = 4.0F;
			this.body.pivotY = 11.0F;
			this.leftArm.pivotY = 6.0F;
			this.rightArm.pivotY = 6.0F;
		}

		if (this.rightArmPose != BipedEntityModel.ArmPose.SPYGLASS) {
			CrossbowPosing.swingArm(this.rightArm, h, 1.0F);
		}

		if (this.leftArmPose != BipedEntityModel.ArmPose.SPYGLASS) {
			CrossbowPosing.swingArm(this.leftArm, h, -1.0F);
		}

		if (this.leaningPitch > 0.0F) {
			float l = f % 26.0F;
			Arm arm = this.getPreferredArm(livingEntity);
			float m = arm == Arm.RIGHT && this.handSwingProgress > 0.0F ? 0.0F : this.leaningPitch;
			float n = arm == Arm.LEFT && this.handSwingProgress > 0.0F ? 0.0F : this.leaningPitch;
			float o;
			if (!livingEntity.isUsingItem()) {
				if (l < 14.0F) {
					this.leftArm.pitch = this.lerpAngle(n, this.leftArm.pitch, 0.0F);
					this.rightArm.pitch = MathHelper.lerp(m, this.rightArm.pitch, 0.0F);
					this.leftArm.yaw = this.lerpAngle(n, this.leftArm.yaw, 3.1415927F);
					this.rightArm.yaw = MathHelper.lerp(m, this.rightArm.yaw, 3.1415927F);
					this.leftArm.roll = this.lerpAngle(n, this.leftArm.roll, 3.1415927F + 1.8707964F * this.quadraticArmUpdate(l) / this.quadraticArmUpdate(14.0F));
					this.rightArm.roll = MathHelper.lerp(m, this.rightArm.roll, 3.1415927F - 1.8707964F * this.quadraticArmUpdate(l) / this.quadraticArmUpdate(14.0F));
				} else if (l >= 14.0F && l < 22.0F) {
					o = (l - 14.0F) / 8.0F;
					this.leftArm.pitch = this.lerpAngle(n, this.leftArm.pitch, 1.5707964F * o);
					this.rightArm.pitch = MathHelper.lerp(m, this.rightArm.pitch, 1.5707964F * o);
					this.leftArm.yaw = this.lerpAngle(n, this.leftArm.yaw, 3.1415927F);
					this.rightArm.yaw = MathHelper.lerp(m, this.rightArm.yaw, 3.1415927F);
					this.leftArm.roll = this.lerpAngle(n, this.leftArm.roll, 5.012389F - 1.8707964F * o);
					this.rightArm.roll = MathHelper.lerp(m, this.rightArm.roll, 1.2707963F + 1.8707964F * o);
				} else if (l >= 22.0F && l < 26.0F) {
					o = (l - 22.0F) / 4.0F;
					this.leftArm.pitch = this.lerpAngle(n, this.leftArm.pitch, 1.5707964F - 1.5707964F * o);
					this.rightArm.pitch = MathHelper.lerp(m, this.rightArm.pitch, 1.5707964F - 1.5707964F * o);
					this.leftArm.yaw = this.lerpAngle(n, this.leftArm.yaw, 3.1415927F);
					this.rightArm.yaw = MathHelper.lerp(m, this.rightArm.yaw, 3.1415927F);
					this.leftArm.roll = this.lerpAngle(n, this.leftArm.roll, 3.1415927F);
					this.rightArm.roll = MathHelper.lerp(m, this.rightArm.roll, 3.1415927F);
				}
			}

			o = 0.3F;
			float p = 0.33333334F;
			this.leftLeg.pitch = MathHelper.lerp(this.leaningPitch, this.leftLeg.pitch, 0.3F * MathHelper.cos(f * 0.33333334F + 3.1415927F));
			this.rightLeg.pitch = MathHelper.lerp(this.leaningPitch, this.rightLeg.pitch, 0.3F * MathHelper.cos(f * 0.33333334F));
		}

		if (livingEntity.getComponent(ArcpocalypseComponents.ARC_COMPONENT).getNecoType().maidModel) {
			this.rightLeg.pitch = MathHelper.clamp(rightLeg.pitch, -0.3f, 0.3f);
			this.leftLeg.pitch = MathHelper.clamp(leftLeg.pitch, -0.3f, 0.3f);

		}
		this.tail.pivotY = 7.0f;
		if(this.rightArmPose == ArmPose.ITEM || this.rightArmPose == ArmPose.EMPTY)
			this.rightArm.roll += 0.523;
		if(this.leftArmPose == ArmPose.ITEM || this.leftArmPose == ArmPose.EMPTY)
			this.leftArm.roll -= 0.523;
		this.leftLeg.visible = !livingEntity.isFallFlying();
		this.rightLeg.visible = !livingEntity.isFallFlying();

		this.hat.copyTransform(this.head);
	}
	@Override
	protected void animateArms(PlayerEntity entity, float animationProgress) {
		if (!(this.handSwingProgress <= 0.0F)) {
			Arm arm = this.getPreferredArm(entity);
			ModelPart modelPart = this.getArm(arm);
			float f = this.handSwingProgress;
			this.body.yaw = MathHelper.sin(MathHelper.sqrt(f) * 6.2831855F) * 0.2F;
			ModelPart var10000;
			if (arm == Arm.LEFT) {
				var10000 = this.body;
				var10000.yaw *= -1.0F;
			}

			this.rightArm.pivotZ = MathHelper.sin(this.body.yaw) * 3.0F;
			this.rightArm.pivotX = -MathHelper.cos(this.body.yaw) * 3.0F;
			this.leftArm.pivotZ = -MathHelper.sin(this.body.yaw) * 3.0F;
			this.leftArm.pivotX = MathHelper.cos(this.body.yaw) * 3.0F;
			var10000 = this.rightArm;
			var10000.yaw += this.body.yaw;
			var10000 = this.leftArm;
			var10000.yaw += this.body.yaw;
			var10000 = this.leftArm;
			var10000.pitch += this.body.yaw;
			f = 1.0F - this.handSwingProgress;
			f *= f;
			f *= f;
			f = 1.0F - f;
			float g = MathHelper.sin(f * 3.1415927F);
			float h = MathHelper.sin(this.handSwingProgress * 3.1415927F) * -(this.head.pitch - 0.7F) * 0.75F;
			modelPart.pitch -= g * 1.2F + h;
			modelPart.yaw += this.body.yaw * 2.0F;
			modelPart.roll += MathHelper.sin(this.handSwingProgress * 3.1415927F) * -0.4F;
		}
	}
	private float quadraticArmUpdate(float angle) {
		return -65.0F * angle + angle * angle;
	}
	private void positionRightArm(PlayerEntity entity) {
		switch (this.rightArmPose) {
			case EMPTY -> this.rightArm.yaw = 0.0F;
			case BLOCK -> {
				this.rightArm.pitch = this.rightArm.pitch * 0.5F - 0.9424779F;
				this.rightArm.yaw = -0.5235988F;
			}
			case ITEM -> {
				this.rightArm.pitch = this.rightArm.pitch * 0.5F - 0.31415927F;
				this.rightArm.yaw = 0.0F;
			}
			case THROW_SPEAR -> {
				this.rightArm.pitch = this.rightArm.pitch * 0.5F - 3.1415927F;
				this.rightArm.yaw = 0.0F;
				this.rightArm.roll = -0.523F;
			}
			case BOW_AND_ARROW -> {
				this.rightArm.yaw = -0.1F + this.head.yaw;
				this.leftArm.yaw = 0.1F + this.head.yaw + 0.4F;
				this.rightArm.pitch = -1.5707964F + this.head.pitch;
				this.leftArm.pitch = -1.5707964F + this.head.pitch;
			}
			case CROSSBOW_CHARGE -> CrossbowPosing.charge(this.rightArm, this.leftArm, entity, true);
			case CROSSBOW_HOLD -> CrossbowPosing.hold(this.rightArm, this.leftArm, this.head, true);
			case SPYGLASS -> {
				this.rightArm.pitch = MathHelper.clamp(this.head.pitch - 1.9198622F - (entity.isInSneakingPose() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
				this.rightArm.yaw = this.head.yaw - 0.2617994F;
			}
			case TOOT_HORN -> {
				this.rightArm.pitch = MathHelper.clamp(this.head.pitch, -1.2F, 1.2F) - 1.4835298F;
				this.rightArm.yaw = this.head.yaw - 0.5235988F;
			}
		}

	}

	private void positionLeftArm(PlayerEntity entity) {
		switch (this.leftArmPose) {
			case EMPTY -> this.leftArm.yaw = 0.0F;
			case BLOCK -> {
				this.leftArm.pitch = this.leftArm.pitch * 0.5F - 0.9424779F;
				this.leftArm.yaw = 0.5235988F;
			}
			case ITEM -> {
				this.leftArm.pitch = this.leftArm.pitch * 0.5F - 0.31415927F;
				this.leftArm.yaw = 0.0F;
			}
			case THROW_SPEAR -> {
				this.leftArm.pitch = this.leftArm.pitch * 0.5F - 3.1415927F;
				this.leftArm.yaw = 0.0F;
				this.leftArm.roll = 0.523F;
			}
			case BOW_AND_ARROW -> {
				this.rightArm.yaw = -0.1F + this.head.yaw - 0.4F;
				this.leftArm.yaw = 0.1F + this.head.yaw;
				this.rightArm.pitch = -1.5707964F + this.head.pitch;
				this.leftArm.pitch = -1.5707964F + this.head.pitch;
			}
			case CROSSBOW_CHARGE -> CrossbowPosing.charge(this.rightArm, this.leftArm, entity, false);
			case CROSSBOW_HOLD -> CrossbowPosing.hold(this.rightArm, this.leftArm, this.head, false);
			case SPYGLASS -> {
				this.leftArm.pitch = MathHelper.clamp(this.head.pitch - 1.9198622F - (entity.isInSneakingPose() ? 0.2617994F : 0.0F), -2.4F, 3.3F);
				this.leftArm.yaw = this.head.yaw + 0.2617994F;
			}
			case TOOT_HORN -> {
				this.leftArm.pitch = MathHelper.clamp(this.head.pitch, -1.2F, 1.2F) - 1.4835298F;
				this.leftArm.yaw = this.head.yaw + 0.5235988F;
			}
		}

	}
	private Arm getPreferredArm(PlayerEntity entity) {
		Arm arm = entity.getMainArm();
		return entity.preferredHand == Hand.MAIN_HAND ? arm : arm.getOpposite();
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		this.tail.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}
}
