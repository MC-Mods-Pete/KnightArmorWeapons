package net.petemc.knightarmorweapons.client.model;

/**
 * Fabric-compatible model layer registration placeholder.
 * In a real Fabric mod, call: EntityModelLayers.register(...) from your client entrypoint.
 */
public final class ModModels {
    private ModModels() {}

    /** Called during Fabric client initialization to register entity model layers. */
    public static void registerModelLayers(EntityLayerRegistrationEvent event) {
        // Register layer definitions for custom entities (if any)
        // Example: event.registerLayerDefinition(KnightArmorModel.LAYER_LOCATION, KnightArmorModel::createBodyLayer);
    }
}
