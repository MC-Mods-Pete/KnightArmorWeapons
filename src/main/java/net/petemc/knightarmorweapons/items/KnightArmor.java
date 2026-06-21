package net.petemc.knightarmorweapons.items;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.petemc.knightarmorweapons.armor.ModArmorMaterials;

/**
 * Knight Armor base class and sub-classes for Helmet, Chestplate, Leggings, Boots.
 * Fabric migration: removed IClientItemExtensions / initializeClient — armor rendering via ResourceKey&lt;ItemModels&gt;.
 */
public abstract class KnightArmor extends ArmorItem {
    public KnightArmor(ArmorItem.Type type, Properties pProperties) {
        super(ModArmorMaterials.KNIGHT, type, pProperties);
    }

    public static class Helmet extends KnightArmor {
        public Helmet() { super(ArmorItem.Type.HELMET, new Item.Properties()); }
    }

    public static class Chestplate extends KnightArmor {
        public Chestplate() { super(ArmorItem.Type.CHESTPLATE, new Item.Properties()); }
    }

    public static class Leggings extends KnightArmor {
        public Leggings() { super(ArmorItem.Type.LEGGINGS, new Item.Properties()); }
    }

    public static class Boots extends KnightArmor {
        public Boots() { super(ArmorItem.Type.BOOTS, new Item.Properties()); }
    }
}

