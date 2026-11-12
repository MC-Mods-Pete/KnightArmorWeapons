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
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Knight Longsword — an elegant, long-reach dueling weapon.
 *
 * Design: Excels at keeping single targets at bay on open ground,
 *         but highly inefficient in close-quarters or against hordes.
 *
 * Stats:
 *   Attack Damage : 7.0F  (= 1 base + 5 tier bonus + 1 modifier)
 *   Attack Speed  : 1.4F  (modifier = 1.4 - 4.0 = -2.6F)
 *   Attack Range  : +1.2 blocks (ForgeMod.ATTACK_RANGE addition)
 *   Sweep Attack  : DISABLED (strictly single-target)
 *
 * Unique mechanics handled in KnightLongSwordCombatEvents:
 *   B) Close-Quarters Penalty — -40% damage if enemy is closer than 1.8 blocks
 *   C) Fencer's Precision     — +20% damage if enemy is 3.5–4.5 blocks away
 */
public class KnightLongSword extends SwordItem {

    /** Fixed UUID for the reach-extension attribute modifier. */
    private static final UUID REACH_MODIFIER_UUID =
            UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");

    public KnightLongSword() {
        super(new Tier() {
            @Override public int getUses()                { return 350; }
            @Override public float getSpeed()             { return 4.0F; }
            @Override public float getAttackDamageBonus() { return 5.0F; } // 1 + 5 + 1 = 7.0 total
            @Override public int getLevel()               { return 3; }
            @Override public int getEnchantmentValue()    { return 30; }
            @Override public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.FLINT));
            }
        },
        1,      // attackDamageModifier → total = 1 + 5 + 1 = 7.0 damage
        -2.6F,  // attackSpeedModifier  → final = 4.0 + (-2.6) = 1.4F (moderate, precise)
        new Item.Properties().tab(KnightArmorWeapons.TAB).stacksTo(1));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // REACH EXTENSION: +1.2 blocks to entity attack range while held in main hand
    // ForgeMod.ATTACK_RANGE is the correct Forge 1.19.2 attribute for melee reach
    // (default: 3.0 blocks → with bonus: 4.2 blocks)
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
            EquipmentSlot slot, ItemStack stack) {

        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder =
                    ImmutableMultimap.builder();
            // Keep base SwordItem modifiers (attack damage + attack speed)
            builder.putAll(super.getAttributeModifiers(slot, stack));
            // Extended reach: a long blade keeps enemies at bay
            builder.put(
                    ForgeMod.ATTACK_RANGE.get(),
                    new AttributeModifier(
                            REACH_MODIFIER_UUID,
                            "Knight Longsword Extended Reach",
                            1.2,
                            AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // A) NO SWEEP: disable the sword sweep action entirely.
    //    Vanilla's Player.attack() checks this flag before applying any sweep
    //    damage or particles — returning false here is the canonical Forge way.
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction action) {
        if (action == ToolActions.SWORD_SWEEP) return false; // strictly single-target
        return super.canPerformAction(stack, action);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Standard item helpers
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public boolean isRepairable(@NotNull ItemStack stack) { return false; }
}
