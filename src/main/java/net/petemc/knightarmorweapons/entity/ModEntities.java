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
     * Called from the main mod class (KnightArmorWeapons.java) to ensure
     * all registrations occur within the correct Fabric lifecycle.
     */
    public static void registerEntities() {
        // Registration is already handled via the static field initialization above.
        // This method serves as a clean hook for ModInitializer.onInitialize().
    }
}
