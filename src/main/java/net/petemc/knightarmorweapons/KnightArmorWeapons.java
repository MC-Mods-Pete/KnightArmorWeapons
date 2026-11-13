package net.petemc.knightarmorweapons;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.petemc.knightarmorweapons.armor.ModArmorMaterials;
import net.petemc.knightarmorweapons.entity.ModEntities;
import net.petemc.knightarmorweapons.items.ModItems;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
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

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("knightarmorweapons",
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

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public KnightArmorWeapons(IEventBus modEventBus, ModContainer modContainer)
    {
        CREATIVE_MODE_TABS.register(modEventBus);
        ModArmorMaterials.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Starting the mod " + NAME);
    }
}
