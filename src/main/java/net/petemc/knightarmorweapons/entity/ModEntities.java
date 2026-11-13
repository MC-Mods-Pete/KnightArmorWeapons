package net.petemc.knightarmorweapons.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModEntities {
    // Same values as the 1.20.1 versions (tracking range 64, update interval 1, velocity updates on)
    public static final EntityType<ThrowableAxeEntity> THROWABLE_AXE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            KnightArmorWeapons.id("projectile_throwable_axe"),
            EntityType.Builder.<ThrowableAxeEntity>of(ThrowableAxeEntity::new, MobCategory.MISC)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .alwaysUpdateVelocity(true)
                    .noSummon()
                    .sized(0.5F, 0.5F)
                    .build("projectile_throwable_axe"));

    /** Loads the class, which registers all entity types (called from the mod initializer). */
    public static void register() {
    }
}
