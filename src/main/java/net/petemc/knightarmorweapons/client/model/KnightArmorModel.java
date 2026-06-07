package net.petemc.knightarmorweapons.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class KnightArmorModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(KnightArmorWeapons.MOD_ID, "modelknight_armor"), "main");
    public final ModelPart chestplate;
    public final ModelPart right_leg;
    public final ModelPart left_leg;
    public final ModelPart leggingsright;
    public final ModelPart leggingsleft;
    public final ModelPart left_arm;
    public final ModelPart right_arm;
    public final ModelPart helmet;

    public KnightArmorModel(ModelPart root) {
        this.chestplate = root.getChild("chestplate");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");
        this.leggingsright = root.getChild("leggingsright");
        this.leggingsleft = root.getChild("leggingsleft");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.helmet = root.getChild("helmet");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition chestplate = partdefinition.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(0, 47).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(26, 46).addBox(-4.0F, 0.0F, -2.5F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        chestplate.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(50, 20).addBox(-2.0F, -7.0F, 1.0F, 3.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(50, 17).addBox(-4.0F, -8.0F, 1.0F, 7.0F, 1.0F, 2.0F, new CubeDeformation(0.1F)).texOffs(50, 22).addBox(-2.0F, -6.0F, -1.0F, 3.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(60, 24).addBox(-1.5F, -13.9F, 1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(-0.2F)).texOffs(60, 20).addBox(-1.5F, -15.7F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 6.0F, 3.5F, 0.0F, 0.0F, -0.6545F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(18, 12).mirror(true).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(34, 12).mirror(true).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offset(-2.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(18, 12).mirror(true).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(34, 12).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.21F)), PartPose.offset(2.0F, 12.0F, 0.0F));
        PartDefinition leggingsright = partdefinition.addOrReplaceChild("leggingsright", CubeListBuilder.create(), PartPose.offset(-1.0F, 12.0F, 0.0F));
        leggingsright.addOrReplaceChild("side_r1", CubeListBuilder.create().texOffs(54, 0).mirror(true).addBox(-3.5F, -0.5F, -2.5F, 4.0F, 10.0F, 5.0F, new CubeDeformation(0.11F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));
        leggingsright.addOrReplaceChild("side_r2", CubeListBuilder.create().texOffs(0, 0).mirror(true).addBox(-3.5F, -0.5F, -2.5F, 4.0F, 10.0F, 5.0F, new CubeDeformation(0.09F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));
        PartDefinition leggingsleft = partdefinition.addOrReplaceChild("leggingsleft", CubeListBuilder.create(), PartPose.offset(1.0F, 12.0F, 0.0F));
        leggingsleft.addOrReplaceChild("side_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -2.5F, 4.0F, 10.0F, 5.0F, new CubeDeformation(0.09F)).texOffs(0, 0).addBox(-0.5F, -0.5F, -2.5F, 4.0F, 10.0F, 5.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
        leggingsleft.addOrReplaceChild("side_r4", CubeListBuilder.create().texOffs(54, 0).addBox(-0.5F, -0.5F, -2.5F, 4.0F, 10.0F, 5.0F, new CubeDeformation(0.11F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));
        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(18, 28).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).texOffs(34, 28).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(5.0F, 2.0F, 0.0F));
        left_arm.addOrReplaceChild("shoulder", CubeListBuilder.create().texOffs(0, 20).addBox(4.0F, -2.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 38).addBox(0.0F, 4.0F, -2.5F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).texOffs(0, 27).addBox(0.0F, -2.0F, -2.5F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.1745F));
        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(18, 28).mirror(true).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(34, 28).mirror(true).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offset(-5.0F, 2.0F, 0.0F));
        right_arm.addOrReplaceChild("shoulder2", CubeListBuilder.create().texOffs(0, 20).mirror(true).addBox(-5.0F, -2.0F, -2.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(0, 38).mirror(true).addBox(-3.0F, 4.0F, -2.5F, 3.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false).texOffs(0, 27).mirror(true).addBox(-4.0F, -2.0F, -2.5F, 4.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.1745F));
        PartDefinition helmet = partdefinition.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(62, 33).addBox(-4.5F, -8.5F, -4.5F, 9.0F, 9.51F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        helmet.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(54, 51).addBox(-5.3571F, -0.49F, -0.3562F, 5.0F, 9.5F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -8.0F, -4.0F, 0.0F, -0.6545F, 0.0F));
        helmet.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(54, 51).mirror(true).addBox(0.3044F, -0.48F, -0.3967F, 5.0F, 9.5F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -8.0F, -4.0F, 0.0F, 0.6545F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        chestplate.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        right_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        left_leg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leggingsright.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leggingsleft.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        helmet.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}