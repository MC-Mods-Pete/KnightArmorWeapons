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
 * Spiked Mace — a heavy, short-range armor-crusher.
 *
 * Design: Terrible vs. fast unarmored hordes (slow, single-target),
 *         but obliterates shields and heavy armor.
 *
 * Stats:
 *   Attack Damage : 9.0F  (= 1 base + 7 tier bonus + 1 modifier)
 *   Attack Speed  : 0.8F  (modifier = 0.8 - 4.0 = -3.2F, slower than any axe)
 *   Attack Range  : -0.3 block reach penalty (minimal, enough to space vs. fast mobs)
 *   Sweep Attack  : DISABLED (single-target only)
 *
 * Unique mechanics handled in SpikedMaceCombatEvents:
 *   A) Shield Obliteration — 100% guaranteed shield disable + 50% crushing damage
 *   B) Armor Sunder        — +2% damage per armor point (max +40% at 20 pts)
 */
public class SpikedMace extends SwordItem {

    private static final ResourceLocation REACH_MODIFIER_ID = KnightArmorWeapons.id("mace_reach");

    private static final Tier TIER = new Tier() {
        @Override public int getUses()                { return 350; }
        @Override public float getSpeed()             { return 6.0F; }
        @Override public float getAttackDamageBonus() { return 7.0F; } // 1 + 7 + 1 = 9 total
        // Replaces getLevel() == 3 (iron level) from 1.20.1
        @Override public @NotNull TagKey<Block> getIncorrectBlocksForDrops() { return BlockTags.INCORRECT_FOR_IRON_TOOL; }
        @Override public int getEnchantmentValue()    { return 15; }
        @Override public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(Items.IRON_INGOT, Items.FLINT);
        }
    };

    public SpikedMace() {
        super(TIER, new Item.Properties().stacksTo(1).attributes(
                SwordItem.createAttributes(TIER, 1, -3.2F) // 1 + 7 + 1 = 9.0 damage, speed 4.0 - 3.2 = 0.8
                        // Minimal reach penalty: -0.3 blocks (was -1.0, reduced after playtest)
                        .withModifierAdded(Attributes.ENTITY_INTERACTION_RANGE,
                                new AttributeModifier(REACH_MODIFIER_ID, -0.3, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)));
    }

    // NO SWEEP: heavy single-target weapon
    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility action) {
        if (action == ItemAbilities.SWORD_SWEEP) return false;
        return super.canPerformAction(stack, action);
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack stack) { return false; }
}
