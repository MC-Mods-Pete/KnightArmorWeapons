package net.petemc.knightarmorweapons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.petemc.knightarmorweapons.client.model.ModModels;
import net.petemc.knightarmorweapons.client.renderer.KnightArmorRenderer;
import net.petemc.knightarmorweapons.client.renderer.ThrowableAxeRenderer;
import net.petemc.knightarmorweapons.entity.ModEntities;
import net.petemc.knightarmorweapons.items.ModItems;

public class KnightArmorWeaponsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register custom armor renderer (replaces Forge IClientItemExtensions)
        KnightArmorRenderer armorRenderer = new KnightArmorRenderer();
        ArmorRenderer.register(armorRenderer,
                ModItems.KNIGHT_HELMET,
                ModItems.KNIGHT_CHESTPLATE,
                ModItems.KNIGHT_LEGGINGS,
                ModItems.KNIGHT_BOOTS);

        // Register entity renderers
        EntityRendererRegistry.register(ModEntities.THROWABLE_AXE, ThrowableAxeRenderer::new);

        // Register model layers
        ModModels.registerModelLayers();
    }
}

