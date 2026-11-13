package net.petemc.knightarmorweapons.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModItems {
    public static final Item KNIGHT_HELMET = register("knight_helmet", new KnightArmor.Helmet());
    public static final Item KNIGHT_CHESTPLATE = register("knight_chestplate", new KnightArmor.Chestplate());
    public static final Item KNIGHT_LEGGINGS = register("knight_leggings", new KnightArmor.Leggings());
    public static final Item KNIGHT_BOOTS = register("knight_boots", new KnightArmor.Boots());

    public static final Item KNIGHT_LONG_SWORD = register("knight_long_sword", new KnightLongSword());
    public static final Item SPIKED_MACE = register("knight_spiked_mace", new SpikedMace());
    public static final Item KNIGHT_WAR_SICKLE = register("knight_war_sickle", new HandScythe());
    public static final Item THROWING_AXE = register("knight_throwing_axe", new ThrowingAxe());
    public static final Item THROWING_AXE_BROKEN = register("knight_throwing_axe_broken", new ThrowingAxeBroken());

    public static final Item KNIGHT_LONG_SWORD_HANDLE = register("knight_long_sword_handle",
            new Item(new Item.Properties().stacksTo(1)));
    public static final Item KNIGHT_LONG_SWORD_BLADE = register("knight_long_sword_blade",
            new Item(new Item.Properties().stacksTo(1)));

    private static Item register(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, KnightArmorWeapons.id(name), item);
    }

    /** Loads the class, which registers all items (called from the mod initializer). */
    public static void register() {
    }
}
