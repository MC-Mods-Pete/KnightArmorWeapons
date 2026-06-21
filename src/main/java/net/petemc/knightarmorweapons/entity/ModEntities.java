package net.petemc.knightarmorweapons.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModEntities {
    public static final EntityType<ThrowableAxeEntity> THROWABLE_AXE = Registry.register(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(KnightArmorWeapons.MOD_ID, "throwable_axe"),
            EntityType.Builder.<ThrowableAxeEntity>create(ThrowableAxeEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .build()
    );

    /**
     * Wird in der Hauptmod-Klasse (KnightArmorWeapons.java) aufgerufen,
     * um sicherzustellen, dass alle Registrierungen im korrekten Fabric-Lifecycle erfolgen.
     */
    public static void registerEntities() {
        // Die Registrierung erfolgt bereits über die statische Feldinitialisierung oben.
        // Diese Methode dient als sauberer Hook für ModInitializer.onInitialize().
    }
}
