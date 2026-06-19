package net.petemc.knightarmorweapons.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.UUID;

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

    /** Fixed UUID for the reach-reduction attribute modifier. */
    private static final UUID REACH_MODIFIER_UUID =
            UUID.fromString("c3d4e5f6-a1b2-7890-abcd-ef1234567891");

    public SpikedMace() {
        super(new Tier() {
            @Override public int getUses()                { return 350; }
            @Override public float getSpeed()             { return 6.0F; }
            @Override public float getAttackDamageBonus() { return 7.0F; } // 1 + 7 + 1 = 9 total
            @Override public int getLevel()               { return 3; }
            @Override public int getEnchantmentValue()    { return 15; }
            @Override public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of(
                        new ItemStack(Items.IRON_INGOT),
                        new ItemStack(Items.FLINT));
            }
        },
        1,      // attackDamageModifier → total = 1 + 7 + 1 = 9.0 damage
        -3.2F,  // attackSpeedModifier  → final = 4.0 + (-3.2) = 0.8F (extremely slow)
        new Item.Properties().stacksTo(1));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // REACH: minimal -0.3 block penalty (keeps vanilla feel, still single-target)
    // Default ATTACK_RANGE is 3.0 → with penalty: 2.7 blocks.
    // Reduced from -1.0 after playtest: full penalty prevented spacing vs. fast mobs.
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
            EquipmentSlot slot, ItemStack stack) {

        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder =
                    ImmutableMultimap.builder();
            // Keep base SwordItem modifiers (attack damage + attack speed)
            builder.putAll(super.getAttributeModifiers(slot, stack));
            // Minimal reach penalty: -0.3 blocks (was -1.0, reduced after playtest)
            builder.put(
                    ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(
                            REACH_MODIFIER_UUID,
                            "Spiked Mace Short Reach",
                            -0.3,
                            AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // NO SWEEP: disable the sword sweep action (single-target weapon only)
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction action) {
        if (action == ToolActions.SWORD_SWEEP) return false; // heavy single-target, no sweep
        return super.canPerformAction(stack, action);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Standard item helpers
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public boolean hasCraftingRemainingItem(@Nonnull ItemStack stack) { return true; }

    @Override
    public @Nonnull ItemStack getCraftingRemainingItem(@Nonnull ItemStack stack) {
        return new ItemStack(this);
    }

    @Override
    public boolean isRepairable(@Nonnull ItemStack stack) { return false; }
}
