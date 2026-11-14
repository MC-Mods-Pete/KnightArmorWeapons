package net.petemc.knightarmorweapons.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.petemc.knightarmorweapons.KnightArmorWeapons;


/**
 * Hand Scythe — a heavy crowd-control weapon for unarmored hordes.
 *
 * Attack speed: final = 4.0 + modifier → modifier = 1.1 - 4.0 = -2.9F
 * (Normal sword uses -2.4F → 1.6F. This weapon is deliberately sluggish.)
 *
 * Unique mechanics are handled in HandScytheCombatEvents:
 *  A) Armor Penalty      — 2.5% damage reduction per armor point (max 50%)
 *  B) Anti-Climb Counter — 50% chance to knock climbing Zombies off walls
 *  C) Dynamic Cleave     — hits up to 2 extra enemies within 2 blocks for 50% damage
 */
public class HandScythe extends Item {

    /** The Hand Scythe can not be repaired in the anvil (empty tag: data/knightarmorweapons/tags/item/repairs_knight_scythe.json) */
    public static final TagKey<Item> REPAIRS_KNIGHT_SCYTHE = TagKey.create(Registries.ITEM, KnightArmorWeapons.id("repairs_knight_scythe"));

    // Same values as the older versions: 200 uses, mining speed 8, +4 damage, iron mining level, enchantability 30
    static final ToolMaterial MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 200, 8.0F, 4.0F, 30, REPAIRS_KNIGHT_SCYTHE);

    public HandScythe(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    /** 1 (player) + 3 + 4 = 8 damage, speed 4.0 - 2.9 = 1.1 (heavy & sluggish). */
    static Item.Properties properties() {
        return new Item.Properties().sword(MATERIAL, 3.0F, -2.9F);
    }

    // Not combinable in the crafting grid or the grindstone (Forge 1.20.1: isRepairable() = false), anvil repair still works
    @Override
    public boolean isCombineRepairable(ItemStack stack) {
        return false;
    }
}
