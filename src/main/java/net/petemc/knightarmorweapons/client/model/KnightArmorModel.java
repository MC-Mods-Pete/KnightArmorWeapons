package net.petemc.knightarmorweapons.client.model;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.model.Dilation;
import net.minecraft.util.Identifier;
import net.minecraft.entity.Entity;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class KnightArmorModel<T extends Entity> extends EntityModel<T> {
    // In Fabric/Yarn: ModelLayerLocation -> EntityModelLayer, ResourceLocation -> Identifier
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(new Identifier(KnightArmorWeapons.MOD_ID, "modelknight_armor"), "main");

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

    public static TexturedModelData createBodyLayer() {
        // Yarn nutzt ModelData statt MeshDefinition
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        // CubeDeformation -> Dilation, PartPose -> ModelTransform
        ModelPartData chestplate = modelPartData.addChild("chestplate", ModelPartBuilder.create().uv(0, 47).cuboid(-4.0F, 0.0F, -2.5F, 8.0F, 12.0F, 5.0F, new Dilation(0.0F)).uv(26, 46).cuboid(-4.0F, 0.0F, -2.5F, 8.0F, 12.0F, 6.0F, new Dilation(0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        chestplate.addChild("sword", ModelPartBuilder.create().uv(50, 20).cuboid(-2.0F, -7.0F, 1.0F, 3.0F, 18.0F, 2.0F, new Dilation(0.0F)).uv(50, 17).cuboid(-4.0F, -8.0F, 1.0F, 7.0F, 1.0F, 2.0F, new Dilation(0.1F)).uv(50, 22).cuboid(-2.0F, -6.0F, -1.0F, 3.0F, 6.0F, 2.0F, new Dilation(0.0F)).uv(60, 24).cuboid(-1.5F, -13.9F, 1.0F, 2.0F, 6.0F, 2.0F, new Dilation(-0.2F)).uv(60, 20).cuboid(-1.5F, -15.7F, 1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 6.0F, 3.5F, 0.0F, 0.0F, -0.6545F));

        modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(18, 12).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false).uv(34, 12).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.2F)).mirrored(false), ModelTransform.pivot(-2.0F, 12.0F, 0.0F));
        modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(18, 12).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false).uv(34, 12).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.21F)), ModelTransform.pivot(2.0F, 12.0F, 0.0F));

        ModelPartData leggingsright = modelPartData.addChild("leggingsright", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 12.0F, 0.0F));
        leggingsright.addChild("side_r1", ModelPartBuilder.create().uv(54, 0).mirrored().cuboid(-3.5F, -0.5F, -2.5F, 4.0F, 10.0F, 5.0F, new Dilation(0.11F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));
        leggingsright.addChild("side_r2", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-3.5F, -0.5F, -2.5F, 4.0F, 10.0F, 5.0F, new Dilation(0.09F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        ModelPartData leggingsleft = modelPartData.addChild("leggingsleft", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 12.0F, 0.0F));
        leggingsleft.addChild("side_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -0.5F, -2.5F, 4.0F, 10.0F, 5.0F, new Dilation(0.09F)).uv(0, 0).cuboid(-0.5F, -0.5F, -2.5F, 4.0F, 10.0F, 5.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));
        leggingsleft.addChild("side_r4", ModelPartBuilder.create().uv(54, 0).cuboid(-0.5F, -0.5F, -2.5F, 4.0F, 10.0F, 5.0F, new Dilation(0.11F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

        ModelPartData left_arm = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(18, 28).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).uv(34, 28).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.2F)), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
        left_arm.addChild("shoulder", ModelPartBuilder.create().uv(0, 20).cuboid(4.0F, -2.0F, -2.5F, 1.0F, 2.0F, 5.0F, new Dilation(0.0F)).uv(0, 38).cuboid(0.0F, 4.0F, -2.5F, 3.0F, 4.0F, 5.0F, new Dilation(0.0F)).uv(0, 27).cuboid(0.0F, -2.0F, -2.5F, 4.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        ModelPartData right_arm = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(18, 28).mirrored().cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)).mirrored(false).uv(34, 28).mirrored().cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.2F)).mirrored(false), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        right_arm.addChild("shoulder2", ModelPartBuilder.create().uv(0, 20).mirrored().cuboid(-5.0F, -2.0F, -2.5F, 1.0F, 2.0F, 5.0F, new Dilation(0.0F)).mirrored(false).uv(0, 38).mirrored().cuboid(-3.0F, 4.0F, -2.5F, 3.0F, 4.0F, 5.0F, new Dilation(0.0F)).mirrored(false).uv(0, 27).mirrored().cuboid(-4.0F, -2.0F, -2.5F, 4.0F, 6.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-1.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        ModelPartData helmet = modelPartData.addChild("helmet", ModelPartBuilder.create().uv(62, 33).cuboid(-4.5F, -8.5F, -4.5F, 9.0F, 9.51F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        helmet.addChild("cube_r1", ModelPartBuilder.create().uv(54, 51).cuboid(-5.3571F, -0.49F, -0.3562F, 5.0F, 9.5F, 3.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, -8.0F, -4.0F, 0.0F, -0.6545F, 0.0F));
        helmet.addChild("cube_r2", ModelPartBuilder.create().uv(54, 51).mirrored().cuboid(0.3044F, -0.48F, -0.3967F, 5.0F, 9.5F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-4.0F, -8.0F, -4.0F, 0.0F, 0.6545F, 0.0F));

        return TexturedModelData.of(modelData, 128, 64);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        // In Yarn heißt setupAnim -> setAngles
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        chestplate.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        right_leg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        left_leg.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        leggingsright.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        leggingsleft.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        left_arm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        right_arm.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        helmet.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}