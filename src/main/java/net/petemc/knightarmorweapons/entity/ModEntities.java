package net.petemc.knightarmorweapons.entity;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModEntities {
    public static final EntityType<ThrowableAxeEntity> THROWABLE_AXE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(KnightArmorWeapons.MOD_ID, "throwable_axe"),
            // Same tracking settings as Forge (tracking range 64, update every tick, velocity updates).
            // FabricEntityTypeBuilder also avoids the "No data fixer registered" log error.
            FabricEntityTypeBuilder.<ThrowableAxeEntity>create(SpawnGroup.MISC, ThrowableAxeEntity::new)
                    .forceTrackedVelocityUpdates(true)
                    .trackRangeChunks(64)
                    .trackedUpdateRate(1)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .build()
    );

    /**
     * Called from the main mod class (KnightArmorWeapons.java) to ensure
     * all registrations occur within the correct Fabric lifecycle.
     */
    public static void registerEntities() {
        // Registration is already handled via the static field initialization above.
        // This method serves as a clean hook for ModInitializer.onInitialize().
    }
}
