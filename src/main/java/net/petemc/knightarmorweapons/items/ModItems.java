package net.petemc.knightarmorweapons.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModItems {
    
    // Armor items - created as static instances for Fabric-style registration
    public static final Item KNIGHT_HELMET = new KnightArmor.Helmet(new Item.Properties().stacksTo(1));
    public static final Item KNIGHT_CHESTPLATE = new KnightArmor.Chestplate(new Item.Properties().stacksTo(1));
    public static final Item KNIGHT_LEGGINGS = new KnightArmor.Leggings(new Item.Properties().stacksTo(1));
    public static final Item KNIGHT_BOOTS = new KnightArmor.Boots(new Item.Properties().stacksTo(1));

    // Weapon items
    public static final Item KNIGHT_LONG_SWORD = new KnightLongSword(new Item.Properties().stacksTo(1));
    public static final Item SPIKED_MACE = new SpikedMace(new Item.Properties().stacksTo(1));
    public static final Item KNIGHT_WAR_SICKLE = new HandScythe(new Item.Properties().stacksTo(1));
    public static final Item THROWING_AXE = new ThrowingAxe(new Item.Properties().stacksTo(8));
    public static final Item THROWING_AXE_BROKEN = new ThrowingAxeBroken(new Item.Properties().stacksTo(1));

    // Crafting component items
    public static final Item KNIGHT_LONG_SWORD_HANDLE = new Item(new Item.Properties().stacksTo(1));
    public static final Item KNIGHT_LONG_SWORD_BLADE = new Item(new Item.Properties().stacksTo(1));

    /** Register all items with the vanilla registry. Called from KnightArmorWeapons.onInitializeItems(). */
    public static void onInitializeItems() {
        register(Registries.ITEM, "knight_helmet", KNIGHT_HELMET);
        register(Registries.ITEM, "knight_chestplate", KNIGHT_CHESTPLATE);
        register(Registries.ITEM, "knight_leggings", KNIGHT_LEGGINGS);
        register(Registries.ITEM, "knight_boots", KNIGHT_BOOTS);

        register(Registries.ITEM, "knight_long_sword", KNIGHT_LONG_SWORD);
        register(Registries.ITEM, "knight_spiked_mace", SPIKED_MACE);
        register(Registries.ITEM, "knight_war_sickle", KNIGHT_WAR_SICKLE);
        register(Registries.ITEM, "knight_throwing_axe", THROWING_AXE);
        register(Registries.ITEM, "knight_throwing_axe_broken", THROWING_AXE_BROKEN);

        register(Registries.ITEM, "knight_long_sword_handle", KNIGHT_LONG_SWORD_HANDLE);
        register(Registries.ITEM, "knight_long_sword_blade", KNIGHT_LONG_SWORD_BLADE);
    }

    @SuppressWarnings("unchecked")
    private static <T> void register(Registries registry, String id, T entry) {
        net.minecraft.core.Registry.register(registry, ResourceLocation.fromNamespaceAndPath(KnightArmorWeapons.MOD_ID, id), entry);
    }
}
