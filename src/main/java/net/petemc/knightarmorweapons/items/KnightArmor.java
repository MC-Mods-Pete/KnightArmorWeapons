package net.petemc.knightarmorweapons.items;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.petemc.knightarmorweapons.armor.ModArmorMaterials;

/**
 * Knight Armor item base class and inner sub-classes.
 *
 * Fabric port: Forge IClientItemExtensions / initializeClient / getArmorTexture
 * are replaced by net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer,
 * registered in KnightArmorWeaponsClient.
 * The custom 3D model (KnightArmorModel) and texture
 * (textures/entities/knight_armor.png) are applied there.
 */
public abstract class KnightArmor extends ArmorItem {

    public KnightArmor(ArmorItem.Type type, Item.Settings settings) {
        super(ModArmorMaterials.KNIGHT, type, settings);
    }

    public static class Helmet extends KnightArmor {
        public Helmet()     { super(ArmorItem.Type.HELMET,    new Item.Settings().maxCount(1)); }
    }

    public static class Chestplate extends KnightArmor {
        public Chestplate() { super(ArmorItem.Type.CHESTPLATE, new Item.Settings().maxCount(1)); }
    }

    public static class Leggings extends KnightArmor {
        public Leggings()   { super(ArmorItem.Type.LEGGINGS,   new Item.Settings().maxCount(1)); }
    }

    public static class Boots extends KnightArmor {
        public Boots()      { super(ArmorItem.Type.BOOTS,      new Item.Settings().maxCount(1)); }
    }
}
