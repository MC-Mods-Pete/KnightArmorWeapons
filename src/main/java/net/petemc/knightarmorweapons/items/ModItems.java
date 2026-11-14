package net.petemc.knightarmorweapons.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.armor.ModArmorMaterials;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KnightArmorWeapons.MOD_ID);

    // Since 1.21.2 armor is a normal Item with the armor components (Properties.humanoidArmor), there is no ArmorItem anymore.
    // The 3D model is swapped in client-side (client.KnightArmorClientExtensions).
    // registerItem sets the item id on the properties (Properties.setId) before the item is created.
    public static final DeferredItem<Item> KNIGHT_HELMET = ITEMS.registerItem("knight_helmet", Item::new, () -> armor(ArmorType.HELMET));
    public static final DeferredItem<Item> KNIGHT_CHESTPLATE = ITEMS.registerItem("knight_chestplate", Item::new, () -> armor(ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> KNIGHT_LEGGINGS = ITEMS.registerItem("knight_leggings", Item::new, () -> armor(ArmorType.LEGGINGS));
    public static final DeferredItem<Item> KNIGHT_BOOTS = ITEMS.registerItem("knight_boots", Item::new, () -> armor(ArmorType.BOOTS));

    public static final DeferredItem<Item> KNIGHT_LONG_SWORD = ITEMS.registerItem("knight_long_sword", KnightLongSword::new, KnightLongSword::properties);
    public static final DeferredItem<Item> SPIKED_MACE = ITEMS.registerItem("knight_spiked_mace", SpikedMace::new, SpikedMace::properties);
    public static final DeferredItem<Item> KNIGHT_WAR_SICKLE = ITEMS.registerItem("knight_war_sickle", HandScythe::new, HandScythe::properties);
    public static final DeferredItem<Item> THROWING_AXE = ITEMS.registerItem("knight_throwing_axe", ThrowingAxe::new);
    public static final DeferredItem<Item> THROWING_AXE_BROKEN = ITEMS.registerItem("knight_throwing_axe_broken", ThrowingAxeBroken::new);

    public static final DeferredItem<Item> KNIGHT_LONG_SWORD_HANDLE = ITEMS.registerItem("knight_long_sword_handle", Item::new,
            () -> new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> KNIGHT_LONG_SWORD_BLADE = ITEMS.registerItem("knight_long_sword_blade", Item::new,
            () -> new Item.Properties().stacksTo(1));

    private static Item.Properties armor(ArmorType type) {
        return new Item.Properties().humanoidArmor(ModArmorMaterials.KNIGHT, type);
    }

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
