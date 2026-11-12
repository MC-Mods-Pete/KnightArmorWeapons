package net.petemc.knightarmorweapons.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.NotNull;

/**
 * Spiked Mace — a heavy, short-range armor-crusher.
 *
 * Design: Terrible vs. fast unarmored hordes (slow, single-target),
 *         but obliterates shields and heavy armor.
 *
 * Stats:
 *   Attack Damage : 9.0  (= 1 base + 7 tier bonus + 1 modifier)
 *   Attack Speed  : 0.8  (modifier = 0.8 - 4.0 = -3.2, slower than any axe)
 *   Sweep Attack  : DISABLED (PlayerEntityMixin)
 *   Reach         : -0.3 blocks (WeaponReach + GameRendererMixin)
 *
 * Unique mechanics:
 *   A) Shield Obliteration — guaranteed shield disable + 50% crushing damage (via AttackEntityCallback)
 *   B) Armor Sunder        — +2% damage per armor point (max +40% at 20 pts) (via LivingEntityMixin)
 *   C) Daze / Concussion   — Slowness III for 30 ticks on every hit (postHit)
 */
public class SpikedMace extends SwordItem {

    public SpikedMace() {
        super(new ToolMaterial() {
            @Override public int getDurability()              { return 350; }
            @Override public float getMiningSpeedMultiplier() { return 6.0F; }
            @Override public float getAttackDamage()          { return 7.0F; } // 1 + 7 + 1 = 9 total
            @Override public int getMiningLevel()             { return 3; }
            @Override public int getEnchantability()          { return 15; }
            @Override public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.ofStacks(
                        new ItemStack(Items.IRON_INGOT),
                        new ItemStack(Items.FLINT));
            }
        },
        1,      // attackDamage modifier → total = 1 + 7 + 1 = 9.0 damage
        -3.2F,  // attackSpeed modifier  → final = 4.0 + (-3.2) = 0.8 (extremely slow)
        new Item.Settings().maxCount(1));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // C) DAZE / CONCUSSION — Slowness III for 30 ticks after every hit
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SLOWNESS,
                30,   // duration in ticks (1.5 s)
                2,    // amplifier 2 = Slowness III (0-indexed)
                false, // not ambient (shows particles)
                true   // show icon in HUD
        ));
        return super.postHit(stack, target, attacker);
    }
}

