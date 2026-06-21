package net.petemc.knightarmorweapons.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModEntities {
    public static final EntityType<ThrowableAxeEntity> THROWABLE_AXE;

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        return Registry.register(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(KnightArmorWeapons.MOD_ID, id), builder.build(id));
    }

    static {
        THROWABLE_AXE = register("projectile_throwable_axe",
                EntityType.Builder.<ThrowableAxeEntity>of(ThrowableAxeEntity::new, MobCategory.MISC)
                        .setCustomClientFactory(ThrowableAxeEntity::new)
                        .setShouldReceiveVelocityUpdates(true)
                        .setTrackingRange(64)
                        .setUpdateInterval(1)
                        .noSummon()
                        .sized(0.5F, 0.5F));
    }

    public static void registerAll() {
        // Entities werden über den statischen Block registriert.
        // In Fabric wird diese Methode typischerweise in der onInitialize()-Methode 
        // deiner Hauptmod-Klasse aufgerufen, um die Lifecycle-Hooks von Fabric zu respektieren.
    }
}
