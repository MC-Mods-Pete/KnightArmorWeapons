package net.petemc.knightarmorweapons.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

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
public class HandScythe extends SwordItem {

    private static final Tier TIER = new Tier() {
        @Override public int getUses()                             { return 200; }
        @Override public float getSpeed()                          { return 8.0F; }
        @Override public float getAttackDamageBonus()              { return 4.0F; }
        // Replaces getLevel() == 3 (iron level) from 1.20.1
        @Override public @NotNull TagKey<Block> getIncorrectBlocksForDrops() { return BlockTags.INCORRECT_FOR_IRON_TOOL; }
        @Override public int getEnchantmentValue()                 { return 30; }
        @Override public @NotNull Ingredient getRepairIngredient() { return Ingredient.of(); }
    };

    public HandScythe() {
        // attackDamageModifier 3, attackSpeedModifier -2.9 → final speed 4.0 - 2.9 = 1.1 (heavy & sluggish)
        super(TIER, new Item.Properties().stacksTo(1).attributes(SwordItem.createAttributes(TIER, 3, -2.9F)));
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack stack) { return false; }
}
