package net.petemc.knightarmorweapons.items;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.petemc.knightarmorweapons.armor.ModArmorMaterials;

/** The 3D model and texture are rendered client-side by client.renderer.KnightArmorRenderer (Fabric ArmorRenderer). */
public abstract class KnightArmor extends ArmorItem {
    public KnightArmor(ArmorItem.Type type, Properties properties) {
        super(ModArmorMaterials.KNIGHT, type, properties.durability(ModArmorMaterials.durability(type)));
    }

    public static class Helmet extends KnightArmor {
        public Helmet() {
            super(ArmorItem.Type.HELMET, new Item.Properties());
        }
    }

    public static class Chestplate extends KnightArmor {
        public Chestplate() {
            super(ArmorItem.Type.CHESTPLATE, new Item.Properties());
        }
    }

    public static class Leggings extends KnightArmor {
        public Leggings() {
            super(ArmorItem.Type.LEGGINGS, new Item.Properties());
        }
    }

    public static class Boots extends KnightArmor {
        public Boots() {
            super(ArmorItem.Type.BOOTS, new Item.Properties());
        }
    }
}
