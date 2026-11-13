package net.petemc.knightarmorweapons;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.entity.ModEntities;
import net.petemc.knightarmorweapons.events.SpikedMaceCombatEvents;
import net.petemc.knightarmorweapons.items.ModItems;
import org.slf4j.Logger;

public class KnightArmorWeapons implements ModInitializer {
    public static final String MOD_ID = "knightarmorweapons";
    public static final String NAME = "Knight Armor and Weapons";
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Set to true to print per-hit damage breakdowns (in hearts) to the
     * attacking player's chat for all custom weapons (SpikedMace, HandScythe,
     * KnightLongSword). Flip to false before shipping a release build.
     */
    public static final boolean DAMAGE_DEBUG = false;

    /** Creative Mode Tab for Knight Armor and Weapons items. */
    public static CreativeModeTab TAB;

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModItems.register();
        ModEntities.register();

        TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id("knight_armor_weapons_tab"),
                FabricItemGroup.builder()
                        .title(Component.translatable("itemGroup." + MOD_ID))
                        .icon(() -> new ItemStack(ModItems.KNIGHT_HELMET))
                        .displayItems((parameters, output) -> {
                            output.accept(ModItems.KNIGHT_HELMET);
                            output.accept(ModItems.KNIGHT_CHESTPLATE);
                            output.accept(ModItems.KNIGHT_LEGGINGS);
                            output.accept(ModItems.KNIGHT_BOOTS);
                            output.accept(ModItems.KNIGHT_LONG_SWORD);
                            output.accept(ModItems.KNIGHT_WAR_SICKLE);
                            output.accept(ModItems.SPIKED_MACE);
                            output.accept(ModItems.THROWING_AXE);
                            output.accept(ModItems.THROWING_AXE_BROKEN);
                            output.accept(ModItems.KNIGHT_LONG_SWORD_HANDLE);
                            output.accept(ModItems.KNIGHT_LONG_SWORD_BLADE);
                        })
                        .build());

        // Spiked Mace shield obliteration (Forge: ShieldBlockEvent)
        SpikedMaceCombatEvents.register();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> LOGGER.info("Starting the mod " + NAME));
    }
}
