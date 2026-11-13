package net.petemc.knightarmorweapons.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import org.jetbrains.annotations.NotNull;

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
 *   Sweep Attack  : DISABLED (strictly single-target)
 *
 * Unique mechanics handled in KnightLongSwordCombatEvents:
 *   B) Close-Quarters Penalty — -40% damage if enemy is closer than 1.8 blocks
 *   C) Fencer's Precision     — +20% damage if enemy is 3.5–4.5 blocks away
 */
public class KnightLongSword extends SwordItem {

    private static final ResourceLocation REACH_MODIFIER_ID = KnightArmorWeapons.id("longsword_reach");

    private static final Tier TIER = new Tier() {
        @Override public int getUses()                { return 350; }
        @Override public float getSpeed()             { return 4.0F; }
        @Override public float getAttackDamageBonus() { return 5.0F; } // 1 + 5 + 1 = 7.0 total
        // Replaces getLevel() == 3 (iron level) from 1.20.1
        @Override public @NotNull TagKey<Block> getIncorrectBlocksForDrops() { return BlockTags.INCORRECT_FOR_IRON_TOOL; }
        @Override public int getEnchantmentValue()    { return 30; }
        @Override public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(Items.IRON_INGOT, Items.FLINT);
        }
    };

    public KnightLongSword() {
        super(TIER, new Item.Properties().stacksTo(1).attributes(
                SwordItem.createAttributes(TIER, 1, -2.6F) // 1 + 5 + 1 = 7.0 damage, speed 4.0 - 2.6 = 1.4
                        // Extended reach: a long blade keeps enemies at bay (default 3.0 -> 4.2 blocks)
                        .withModifierAdded(Attributes.ENTITY_INTERACTION_RANGE,
                                new AttributeModifier(REACH_MODIFIER_ID, 1.2, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)));
    }

    // NO SWEEP: Player.attack() asks the stack for ItemAbilities.SWORD_SWEEP before any sweep damage or particles.
    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility action) {
        if (action == ItemAbilities.SWORD_SWEEP) return false; // strictly single-target
        return super.canPerformAction(stack, action);
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack stack) { return false; }
}
