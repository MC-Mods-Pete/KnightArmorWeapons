package net.petemc.knightarmorweapons.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;

/**
 * Knight Longsword — an elegant, long-reach dueling weapon.
 *
 * Design: Excels at keeping single targets at bay on open ground,
 *         but highly inefficient in close-quarters or against hordes.
 *
 * Stats:
 *   Attack Damage : 7.0  (= 1 base + 5 tier bonus + 1 modifier)
 *   Attack Speed  : 1.4  (modifier = 1.4 - 4.0 = -2.6)
 *   Sweep Attack  : DISABLED (PlayerEntityMixin)
 *   Reach         : +1.2 blocks (WeaponReach + GameRendererMixin)
 *
 * Unique mechanics handled in KnightLongSwordCombatEvents (via LivingEntityMixin):
 *   B) Close-Quarters Penalty — -40% damage if enemy is closer than 1.8 blocks
 *   C) Fencer's Precision     — +20% damage if enemy is 3.5–4.5 blocks away
 */
public class KnightLongSword extends SwordItem {

    public KnightLongSword() {
        super(new ToolMaterial() {
            @Override public int getDurability()                  { return 350; }
            @Override public float getMiningSpeedMultiplier()     { return 4.0F; }
            @Override public float getAttackDamage()              { return 5.0F; } // 1 + 5 + 1 = 7.0 total
            @Override public int getMiningLevel()                 { return 3; }
            @Override public int getEnchantability()              { return 30; }
            @Override public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.ofStacks(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.FLINT));
            }
        },
        1,      // attackDamage modifier → total = 1 + 5 + 1 = 7.0 damage
        -2.6F,  // attackSpeed modifier  → final = 4.0 + (-2.6) = 1.4
        new Item.Settings().maxCount(1));
    }
}
