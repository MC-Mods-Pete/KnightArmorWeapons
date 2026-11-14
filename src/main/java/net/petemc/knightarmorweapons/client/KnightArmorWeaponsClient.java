package net.petemc.knightarmorweapons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.petemc.knightarmorweapons.client.model.KnightArmorModel;
import net.petemc.knightarmorweapons.client.renderer.ThrowableAxeRenderer;
import net.petemc.knightarmorweapons.client.renderer.KnightArmorRenderer;
import net.petemc.knightarmorweapons.entity.ModEntities;
import net.petemc.knightarmorweapons.items.ModItems;

/**
 * Client entrypoint (replaces the Forge client event subscribers ModModels and ModEvents).
 * The flat inventory icons of battle axe, hammer and broken throwing axe no longer need code: since 1.21.4 the item
 * model definitions (assets/knightarmorweapons/items/*.json) pick the "_gui" model for the GUI display context.
 */
public class KnightArmorWeaponsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(KnightArmorModel.LAYER_LOCATION, KnightArmorModel::createBodyLayer);
        // Vanilla registry, made public by Fabric (EntityRendererRegistry is deprecated)
        EntityRenderers.register(ModEntities.THROWABLE_AXE, ThrowableAxeRenderer::new);
        ArmorRenderer.register(KnightArmorRenderer::new,
                ModItems.KNIGHT_HELMET, ModItems.KNIGHT_CHESTPLATE, ModItems.KNIGHT_LEGGINGS, ModItems.KNIGHT_BOOTS);
    }
}
