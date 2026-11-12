package net.petemc.knightarmorweapons.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.client.model.KnightArmorModel;

/**
 * Custom ArmorRenderer for all Knight Armor pieces.
 *
 * Replaces Forge's IClientItemExtensions / getHumanoidArmorModel approach.
 * Registered in KnightArmorWeaponsClient for all four armor items.
 *
 * Per slot, only the corresponding KnightArmorModel parts are rendered:
 *   HEAD  → helmet
 *   CHEST → chestplate, left_arm, right_arm
 *   LEGS  → leggingsleft, leggingsright
 *   FEET  → left_leg, right_leg
 *
 * Animation is copied from the contextModel (already posed by vanilla) to our parts.
 */
@Environment(EnvType.CLIENT)
public class KnightArmorRenderer implements ArmorRenderer {

    private static final Identifier TEXTURE =
            new Identifier(KnightArmorWeapons.MOD_ID, "textures/entities/knight_armor.png");

    /** Lazily initialised so we don't touch rendering before the model layer is baked. */
    private KnightArmorModel<LivingEntity> model;

    private KnightArmorModel<LivingEntity> getModel() {
        if (model == null) {
            model = new KnightArmorModel<>(
                MinecraftClient.getInstance()
                    .getEntityModelLoader()
                    .getModelPart(KnightArmorModel.LAYER_LOCATION));
        }
        return model;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       ItemStack stack, LivingEntity entity, EquipmentSlot slot,
                       int light, BipedEntityModel<LivingEntity> contextModel) {

        KnightArmorModel<LivingEntity> armorModel = getModel();

        // Copy the vanilla animation pose to our model parts
        copyTransform(contextModel.head,     armorModel.helmet);
        copyTransform(contextModel.body,     armorModel.chestplate);
        copyTransform(contextModel.rightArm, armorModel.right_arm);
        copyTransform(contextModel.leftArm,  armorModel.left_arm);
        copyTransform(contextModel.rightLeg, armorModel.right_leg);
        copyTransform(contextModel.leftLeg,  armorModel.left_leg);
        copyTransform(contextModel.rightLeg, armorModel.leggingsright);
        copyTransform(contextModel.leftLeg,  armorModel.leggingsleft);

        VertexConsumer consumer = vertexConsumers.getBuffer(
                RenderLayer.getArmorCutoutNoCull(TEXTURE));

        switch (slot) {
            case HEAD ->
                armorModel.helmet.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
            case CHEST -> {
                armorModel.chestplate.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
                armorModel.right_arm .render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
                armorModel.left_arm  .render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
            }
            case LEGS -> {
                armorModel.leggingsright.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
                armorModel.leggingsleft .render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
            }
            case FEET -> {
                armorModel.right_leg.render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
                armorModel.left_leg .render(matrices, consumer, light, OverlayTexture.DEFAULT_UV);
            }
            default -> { /* offhand — nothing to render */ }
        }
    }

    /**
     * Copies pivot and rotation from the player model part, so the armor follows
     * sneaking and riding poses. Same as Forge, where the parent model's
     * copyPropertiesTo() overwrites the armor model's part poses.
     */
    private static void copyTransform(ModelPart from, ModelPart to) {
        to.copyTransform(from);
    }
}

