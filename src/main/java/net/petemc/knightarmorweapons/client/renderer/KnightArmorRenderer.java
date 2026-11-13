package net.petemc.knightarmorweapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.client.model.KnightArmorModels;

/**
 * Renders the Knight armor with its own 3D model (Fabric replacement for Forge's IClientItemExtensions
 * getHumanoidArmorModel + ArmorItem.getArmorTexture).
 */
public class KnightArmorRenderer implements ArmorRenderer {
    private static final ResourceLocation ARMOR_TEXTURE = KnightArmorWeapons.id("textures/entities/knight_armor.png");

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity,
                       EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
        HumanoidModel<LivingEntity> model = KnightArmorModels.get(slot);
        // Pose, crouching, riding, baby etc. of the wearer
        contextModel.copyPropertiesTo(model);
        ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model,
                ARMOR_TEXTURE);
    }
}
