package net.petemc.knightarmorweapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.client.model.KnightArmorModels;

/**
 * Renders the Knight armor with its own 3D model (Fabric replacement for Forge's IClientItemExtensions). Since 1.21.9 models are submitted and drawn later.
 */
public class KnightArmorRenderer implements ArmorRenderer {
    private static final Identifier ARMOR_TEXTURE = KnightArmorWeapons.id("textures/entities/knight_armor.png");

    private final KnightArmorModels models;

    public KnightArmorRenderer(EntityRendererProvider.Context context) {
        this.models = new KnightArmorModels(context);
    }

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack stack, HumanoidRenderState state,
                       EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
        // Same check as vanilla's HumanoidArmorLayer: small armor stands use the scaled adult model, real babies their own model
        boolean babyProportions = state.isBaby && state.entityType != EntityTypes.ARMOR_STAND;
        KnightArmorModels.Piece model = this.models.create(slot, contextModel, babyProportions);
        Identifier texture = ARMOR_TEXTURE;

        // Since 26.3 the enchantment glint is drawn in the same pass (like vanilla's EquipmentLayerRenderer)
        submitNodeCollector.order(1).submitModel(model, state, poseStack,
                stack.hasFoil() ? RenderTypes.armorCutoutNoCullGlint(texture) : RenderTypes.armorCutoutNoCull(texture),
                light, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor);
    }
}
