package net.petemc.knightarmorweapons;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.petemc.knightarmorweapons.entity.ModEntities;
import net.petemc.knightarmorweapons.items.ModItems;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KnightArmorWeapons.MOD_ID)
public class KnightArmorWeapons
{
    public static final String MOD_ID = "knightarmorweapons";
    public static final String NAME = "Knight Armor and Weapons";
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Set to true to print per-hit damage breakdowns (in hearts) to the
     * attacking player's chat for all custom weapons (SpikedMace, HandScythe,
     * KnightLongSword). Flip to false before shipping a release build.
     */
    public static final boolean DAMAGE_DEBUG = false;

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("knightarmorweapons",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + MOD_ID))
                    .icon(() -> new ItemStack(ModItems.KNIGHT_HELMET.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.KNIGHT_HELMET.get());
                        output.accept(ModItems.KNIGHT_CHESTPLATE.get());
                        output.accept(ModItems.KNIGHT_LEGGINGS.get());
                        output.accept(ModItems.KNIGHT_BOOTS.get());
                        output.accept(ModItems.KNIGHT_LONG_SWORD.get());
                        output.accept(ModItems.KNIGHT_WAR_SICKLE.get());
                        output.accept(ModItems.SPIKED_MACE.get());
                        output.accept(ModItems.THROWING_AXE.get());
                        output.accept(ModItems.THROWING_AXE_BROKEN.get());
                        output.accept(ModItems.KNIGHT_LONG_SWORD_HANDLE.get());
                        output.accept(ModItems.KNIGHT_LONG_SWORD_BLADE.get());
                    })
                    .build());

    public KnightArmorWeapons(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        CREATIVE_MODE_TABS.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        // Register client-side render events (only on client dist)
        if (net.minecraftforge.fml.loading.FMLEnvironment.dist.isClient()) {
            //MinecraftForge.EVENT_BUS.register(new ClientRenderEvents());
        }

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        //context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Starting the mod " + NAME);
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
        }
    }
}
