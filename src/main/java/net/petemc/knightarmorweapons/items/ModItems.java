package net.petemc.knightarmorweapons.items;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModItems {

    // Armor items
    public static final Item KNIGHT_HELMET    = new KnightArmor.Helmet();
    public static final Item KNIGHT_CHESTPLATE = new KnightArmor.Chestplate();
    public static final Item KNIGHT_LEGGINGS  = new KnightArmor.Leggings();
    public static final Item KNIGHT_BOOTS     = new KnightArmor.Boots();

    // Weapon items
    public static final Item KNIGHT_LONG_SWORD   = new KnightLongSword();
    public static final Item SPIKED_MACE         = new SpikedMace();
    public static final Item KNIGHT_WAR_SICKLE   = new HandScythe();
    public static final Item THROWING_AXE        = new ThrowingAxe();
    public static final Item THROWING_AXE_BROKEN = new ThrowingAxeBroken();

    // Crafting component items
    public static final Item KNIGHT_LONG_SWORD_HANDLE = new Item(new Item.Settings().maxCount(1));
    public static final Item KNIGHT_LONG_SWORD_BLADE  = new Item(new Item.Settings().maxCount(1));

    /** Register all items with the vanilla registry. Called from KnightArmorWeapons.onInitialize(). */
    public static void onInitializeItems() {
        register("knight_helmet",    KNIGHT_HELMET);
        register("knight_chestplate", KNIGHT_CHESTPLATE);
        register("knight_leggings",  KNIGHT_LEGGINGS);
        register("knight_boots",     KNIGHT_BOOTS);

        register("knight_long_sword",        KNIGHT_LONG_SWORD);
        register("knight_spiked_mace",       SPIKED_MACE);
        register("knight_war_sickle",        KNIGHT_WAR_SICKLE);
        register("knight_throwing_axe",      THROWING_AXE);
        register("knight_throwing_axe_broken", THROWING_AXE_BROKEN);

        register("knight_long_sword_handle", KNIGHT_LONG_SWORD_HANDLE);
        register("knight_long_sword_blade",  KNIGHT_LONG_SWORD_BLADE);
    }

    private static void register(String id, Item item) {
        Registry.register(Registries.ITEM,
                new Identifier(KnightArmorWeapons.MOD_ID, id), item);
    }
}
