package net.petemc.knightarmorweapons;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.armor.ModArmorMaterials;
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

    /**
     * Creative Mode Tab for Knight Armor and Weapons items.
     */
    public static CreativeModeTab TAB;

    @Override
    public void onInitialize() {
        // Register the creative tab using direct registry registration (Fabric 1.20.1 standard)
        TAB = CreativeModeTab.builder()
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
                .build();

        Registry.register(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(MOD_ID, "knight_armor_weapons_tab"), TAB);

        // Register all items with the vanilla registry
        ModItems.onInitializeItems();

        // Register the custom armor material
        Registry.register(Registries.ARMOR_MATERIAL, ResourceLocation.fromNamespaceAndPath(MOD_ID, "knight"), ModArmorMaterials.KNIGHT);

        LOGGER.info("Knight Armor and Weapons initialized!");
    }
}
