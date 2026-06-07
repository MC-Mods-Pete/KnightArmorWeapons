package net.petemc.knightarmorweapons.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

@EventBusSubscriber(
        bus = Bus.MOD
)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY;
    public static final RegistryObject<EntityType<ThrowableAxeEntity>> THROWABLE_AXE;

    public ModEntities() {
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(registryname, () -> entityTypeBuilder.build(registryname));
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }

    static {
        REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KnightArmorWeapons.MOD_ID);
        THROWABLE_AXE = register("projectile_throwable_axe",
                EntityType.Builder.<ThrowableAxeEntity>of(ThrowableAxeEntity::new, MobCategory.MISC)
                        .setCustomClientFactory(ThrowableAxeEntity::new)
                        .setShouldReceiveVelocityUpdates(true)
                        .setTrackingRange(64)
                        .setUpdateInterval(1)
                        .noSummon()
                        .sized(0.5F, 0.5F));
    }
}
