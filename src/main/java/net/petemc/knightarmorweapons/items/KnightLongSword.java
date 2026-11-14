package net.petemc.knightarmorweapons.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

/**
 * Knight Longsword — an elegant, long-reach dueling weapon.
 *
 * Design: Excels at keeping single targets at bay on open ground,
 *         but highly inefficient in close-quarters or against hordes.
 *
 * Stats:
 *   Attack Damage : 7.0F  (= 1 base + 5 tier bonus + 1 modifier)
 *   Attack Speed  : 1.4F  (modifier = 1.4 - 4.0 = -2.6F)
 *   Attack Range  : +1.2 blocks (Attributes.ENTITY_INTERACTION_RANGE addition)
 *   Sweep Attack  : DISABLED (strictly single-target): since 26.1 the sweep needs the item tag minecraft:swords,
 *                 the Longsword is deliberately NOT in that tag (see data/minecraft/tags/item/enchantable/*)
 *
 * Unique mechanics handled in KnightLongSwordCombatEvents:
 *   B) Close-Quarters Penalty — -40% damage if enemy is closer than 1.8 blocks
 *   C) Fencer's Precision     — +20% damage if enemy is 3.5–4.5 blocks away
 */
public class KnightLongSword extends Item {

    /** Repair materials for the anvil: iron ingot, flint (data/knightarmorweapons/tags/item/repairs_knight_weapons.json) */
    public static final TagKey<Item> REPAIRS_KNIGHT_WEAPONS = TagKey.create(Registries.ITEM, KnightArmorWeapons.id("repairs_knight_weapons"));

    // Same values as the older versions: 350 uses, mining speed 4, +5 damage, iron mining level, enchantability 30
    static final ToolMaterial MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 350, 4.0F, 5.0F, 30, REPAIRS_KNIGHT_WEAPONS);

    public KnightLongSword(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    /**
     * 1 (player) + 1 + 5 = 7.0 damage, speed 4.0 - 2.6 = 1.4. Properties.sword sets the attribute modifiers itself,
     * so the reach modifier (+1.2 blocks: a long blade keeps enemies at bay, default 3.0 -> 4.2) is appended
     * whenever the attributes are set.
     */
    static Item.Properties properties() {
        return new Item.Properties() {
            @Override
            public Item.Properties attributes(ItemAttributeModifiers attributes) {
                return super.attributes(attributes.withModifierAdded(
                        Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(KnightArmorWeapons.id("longsword_reach"), 1.2, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND));
            }
        }.sword(MATERIAL, 1.0F, -2.6F);
    }
}
