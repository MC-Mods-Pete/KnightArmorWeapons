package net.petemc.knightarmorweapons.events;

import java.util.Map;

/**
 * Event handling class for entity and game events.
 * Migrated from Forge event bus to Fabric-compatible static methods.
 */
public class ModEvents {

    /** Called during entity attribute setup (Fabric: invoke from client initializer) */
    public static void onAttributeCreation(Map<net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.Mob>, net.minecraft.world.entity.ai.attributes.AttributeSupplier> attributes) {
        // No custom attributes needed in the current version
    }
}
