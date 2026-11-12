package net.petemc.knightarmorweapons;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
    public static ItemGroup TAB;

    @Override
    public void onInitialize() {
        // Register all items
        ModItems.onInitializeItems();

        // Register entities
        ModEntities.registerEntities();

        // Register creative tab (Fabric API FabricItemGroup)
        TAB = FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup." + MOD_ID))
                .icon(() -> new ItemStack(ModItems.KNIGHT_HELMET))
                .entries((context, entries) -> {
                    entries.add(ModItems.KNIGHT_HELMET);
                    entries.add(ModItems.KNIGHT_CHESTPLATE);
                    entries.add(ModItems.KNIGHT_LEGGINGS);
                    entries.add(ModItems.KNIGHT_BOOTS);
                    entries.add(ModItems.KNIGHT_LONG_SWORD);
                    entries.add(ModItems.KNIGHT_WAR_SICKLE);
                    entries.add(ModItems.SPIKED_MACE);
                    entries.add(ModItems.THROWING_AXE);
                    entries.add(ModItems.THROWING_AXE_BROKEN);
                    entries.add(ModItems.KNIGHT_LONG_SWORD_HANDLE);
                    entries.add(ModItems.KNIGHT_LONG_SWORD_BLADE);
                })
                .build();

        Registry.register(Registries.ITEM_GROUP,
                new Identifier(MOD_ID, "knight_armor_weapons_tab"), TAB);

        // Register Fabric combat event callbacks (SpikedMace shield bypass via AttackEntityCallback)
        SpikedMaceCombatEvents.register();

        LOGGER.info("Knight Armor and Weapons initialized!");
    }
}
