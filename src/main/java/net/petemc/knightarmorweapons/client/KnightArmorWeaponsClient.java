package net.petemc.knightarmorweapons.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.petemc.knightarmorweapons.client.model.KnightArmorModel;
import net.petemc.knightarmorweapons.client.renderer.ThrowableAxeRenderer;
import net.petemc.knightarmorweapons.client.renderer.KnightArmorRenderer;
import net.petemc.knightarmorweapons.entity.ModEntities;
import net.petemc.knightarmorweapons.items.ModItems;

/** Client entrypoint (replaces the Forge client event subscribers ModModels and ModEvents). */
public class KnightArmorWeaponsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(KnightArmorModel.LAYER_LOCATION, KnightArmorModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntities.THROWABLE_AXE, ThrowableAxeRenderer::new);
        ArmorRenderer.register(new KnightArmorRenderer(),
                ModItems.KNIGHT_HELMET, ModItems.KNIGHT_CHESTPLATE, ModItems.KNIGHT_LEGGINGS, ModItems.KNIGHT_BOOTS);
    }
}
