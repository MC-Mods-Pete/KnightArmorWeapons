package net.petemc.knightarmorweapons.entity;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModEntities {
    public static final EntityType<ThrowableAxeEntity> THROWABLE_AXE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(KnightArmorWeapons.MOD_ID, "throwable_axe"),
            EntityType.Builder.<ThrowableAxeEntity>create(ThrowableAxeEntity::new, SpawnGroup.MISC)
                    .sized(0.5f, 0.5f)
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
