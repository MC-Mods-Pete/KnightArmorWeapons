package net.petemc.knightarmorweapons.client.model;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

/**
 * Registers custom entity model layers with the Fabric client API.
 * Called from KnightArmorWeaponsClient (client entrypoint).
 */
public final class ModModels {
    private ModModels() {}

    /** Called during Fabric client initialization to register entity model layers. */
    public static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(
                KnightArmorModel.LAYER_LOCATION,
                KnightArmorModel::createBodyLayer);
    }
}
