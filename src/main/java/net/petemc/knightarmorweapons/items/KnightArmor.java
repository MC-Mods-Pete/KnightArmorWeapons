package net.petemc.knightarmorweapons.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.armor.ModArmorMaterials;

/** The 3D model is registered client-side in events.ModEvents (RegisterClientExtensionsEvent). */
public abstract class KnightArmor extends ArmorItem {
    private static final ResourceLocation ARMOR_TEXTURE = KnightArmorWeapons.id("textures/entities/knight_armor.png");

    public KnightArmor(ArmorItem.Type type, Properties properties) {
        super(ModArmorMaterials.KNIGHT, type, properties.durability(ModArmorMaterials.durability(type)));
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ARMOR_TEXTURE;
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
