package net.petemc.knightarmorweapons.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY =
            DeferredRegister.create(Registries.ENTITY_TYPE, KnightArmorWeapons.MOD_ID);

    // Same values as the 1.20.1 version (tracking range 64, update interval 1).
    // Velocity updates are on by default in vanilla for this entity type (setShouldReceiveVelocityUpdates no longer exists).
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableAxeEntity>> THROWABLE_AXE = REGISTRY.register(
            "projectile_throwable_axe",
            () -> EntityType.Builder.<ThrowableAxeEntity>of(ThrowableAxeEntity::new, MobCategory.MISC)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .noSummon()
                    .sized(0.5F, 0.5F)
                    .build("projectile_throwable_axe"));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
