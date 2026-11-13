package net.petemc.knightarmorweapons.armor;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

import java.util.EnumMap;
import java.util.List;

/**
 * Since 1.20.5 armor materials are registry entries. Same values as the 1.20.1 versions.
 * Durability is no longer part of the material: it is set per item via {@link #durability(ArmorItem.Type)},
 * base values {boots 13, leggings 15, chestplate 16, helmet 11} x 22 (same as before).
 */
public class ModArmorMaterials {
    public static final int DURABILITY_MULTIPLIER = 22;

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, KnightArmorWeapons.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> KNIGHT = ARMOR_MATERIALS.register("knight",
            () -> new ArmorMaterial(
                    defense(3, 6, 7, 3),
                    10,
                    SoundEvents.ARMOR_EQUIP_IRON,
                    () -> Ingredient.of(Items.IRON_INGOT),
                    // Texture is chosen in KnightArmor.getArmorTexture, this layer name is only the vanilla fallback
                    List.of(new ArmorMaterial.Layer(KnightArmorWeapons.id("knight"))),
                    1.0F, 0.1F));

    public static int durability(ArmorItem.Type type) {
        return switch (type) {
            case BOOTS -> 13;
            case LEGGINGS -> 15;
            case CHESTPLATE -> 16;
            case HELMET -> 11;
            case BODY -> 16;
        } * DURABILITY_MULTIPLIER;
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }

    private static EnumMap<ArmorItem.Type, Integer> defense(int boots, int leggings, int chestplate, int helmet) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.BOOTS, boots);
        map.put(ArmorItem.Type.LEGGINGS, leggings);
        map.put(ArmorItem.Type.CHESTPLATE, chestplate);
        map.put(ArmorItem.Type.HELMET, helmet);
        return map;
    }
}
