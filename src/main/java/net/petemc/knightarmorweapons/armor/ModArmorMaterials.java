package net.petemc.knightarmorweapons.armor;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

import java.util.EnumMap;
import java.util.Map;

/**
 * Since 1.21.2 armor materials are plain records again (no registry entry). Same values as the older versions:
 * durability {11, 16, 15, 13} x 22 = Helmet 242, Chest 352, Legs 330, Boots 286, protection 3/6/7/3,
 * toughness 1.0, knockback resistance 0.1, enchantability 10.
 */
public class ModArmorMaterials {
    public static final int DURABILITY_MULTIPLIER = 22;

    /** Repair material for the anvil: iron ingot (data/knightarmorweapons/tags/item/repairs_knight_armor.json) */
    public static final TagKey<Item> REPAIRS_KNIGHT_ARMOR = TagKey.create(Registries.ITEM, KnightArmorWeapons.id("repairs_knight_armor"));

    /**
     * The armor is rendered with its own 3D model by client.renderer.KnightArmorRenderer, so there is no
     * equipment asset file (assets/knightarmorweapons/equipment/knight.json). Vanilla treats a missing asset as "no layers".
     */
    public static final ResourceKey<EquipmentAsset> KNIGHT_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, KnightArmorWeapons.id("knight"));

    public static final ArmorMaterial KNIGHT = new ArmorMaterial(
            DURABILITY_MULTIPLIER,
            defense(3, 6, 7, 3),
            10,
            SoundEvents.ARMOR_EQUIP_IRON,
            1.0F, 0.1F,
            REPAIRS_KNIGHT_ARMOR,
            KNIGHT_ASSET);

    private static Map<ArmorType, Integer> defense(int boots, int leggings, int chestplate, int helmet) {
        Map<ArmorType, Integer> map = new EnumMap<>(ArmorType.class);
        map.put(ArmorType.BOOTS, boots);
        map.put(ArmorType.LEGGINGS, leggings);
        map.put(ArmorType.CHESTPLATE, chestplate);
        map.put(ArmorType.HELMET, helmet);
        return map;
    }
}
