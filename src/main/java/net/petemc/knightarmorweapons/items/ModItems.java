package net.petemc.knightarmorweapons.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.armor.ModArmorMaterials;

import java.util.function.Function;

public class ModItems {
    // Since 1.21.2 armor is a normal Item with the armor components (Properties.humanoidArmor), there is no ArmorItem anymore.
    // The 3D model is rendered client-side by client.renderer.KnightArmorRenderer (Fabric ArmorRenderer).
    public static final Item KNIGHT_HELMET = register("knight_helmet", Item::new, armor(ArmorType.HELMET));
    public static final Item KNIGHT_CHESTPLATE = register("knight_chestplate", Item::new, armor(ArmorType.CHESTPLATE));
    public static final Item KNIGHT_LEGGINGS = register("knight_leggings", Item::new, armor(ArmorType.LEGGINGS));
    public static final Item KNIGHT_BOOTS = register("knight_boots", Item::new, armor(ArmorType.BOOTS));

    public static final Item KNIGHT_LONG_SWORD = register("knight_long_sword", KnightLongSword::new, KnightLongSword.properties());
    public static final Item SPIKED_MACE = register("knight_spiked_mace", SpikedMace::new, SpikedMace.properties());
    public static final Item KNIGHT_WAR_SICKLE = register("knight_war_sickle", HandScythe::new, HandScythe.properties());
    public static final Item THROWING_AXE = register("knight_throwing_axe", ThrowingAxe::new, new Item.Properties());
    public static final Item THROWING_AXE_BROKEN = register("knight_throwing_axe_broken", ThrowingAxeBroken::new, new Item.Properties());

    public static final Item KNIGHT_LONG_SWORD_HANDLE = register("knight_long_sword_handle", Item::new,
            new Item.Properties().stacksTo(1));
    public static final Item KNIGHT_LONG_SWORD_BLADE = register("knight_long_sword_blade", Item::new,
            new Item.Properties().stacksTo(1));

    private static Item.Properties armor(ArmorType type) {
        return new Item.Properties().humanoidArmor(ModArmorMaterials.KNIGHT, type);
    }

    /** Since 1.21.2 the item id must be known before the item is created (Properties.setId), like vanilla's Items.registerItem. */
    private static Item register(String name, Function<Item.Properties, Item> factory, Item.Properties properties) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, KnightArmorWeapons.id(name));
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(properties.setId(key)));
    }

    /** Loads the class, which registers all items (called from the mod initializer). */
    public static void register() {
    }
}
