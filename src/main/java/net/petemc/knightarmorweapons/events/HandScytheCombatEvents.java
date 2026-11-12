package net.petemc.knightarmorweapons.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.items.HandScythe;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles the Armor-Penalty mechanic of the Hand Scythe.
 * Called from LivingEntityMixin when a HandScythe performs a melee hit.
 *
 * Anti-Climb and Cleave are handled in HandScythe.postHit().
 *
 * Mechanics:
 *   A) Armor Penalty — 2.5% damage reduction per armor point (cap 50% at 20 pts)
 */
public class HandScytheCombatEvents {

    /**
     * Set of entity IDs currently being cleaved.
     * Read by LivingEntityMixin to skip damage modification for secondary cleave hits.
     * Written by HandScythe.postHit() around each cleave call.
     */
    public static final Set<Integer> pendingCleave =
            Collections.synchronizedSet(new HashSet<>());

    /**
     * Final damage of the last Hand Scythe hit per target entity ID.
     * Written in modifyDamage(), read & removed in HandScythe.postHit() to compute the cleave damage
     * (50% of the hit, same as Forge).
     */
    public static final Map<Integer, Float> lastHitDamage = new ConcurrentHashMap<>();

    /**
     * Modifies incoming damage according to the Hand Scythe's Armor Penalty.
     * Called from LivingEntityMixin on every player attack that hits this target.
     */
    public static float modifyDamage(LivingEntity target, float originalDamage, PlayerEntity attacker) {
        // Guard: skip cleave-secondary hits
        if (pendingCleave.contains(target.getId())) return originalDamage;

        ItemStack held = attacker.getMainHandStack();
        if (!(held.getItem() instanceof HandScythe)) return originalDamage;

        int armorValue = target.getArmor();
        float armorPenaltyFactor = Math.min(armorValue * 0.025f, 0.50f);
        float damage = originalDamage * (1.0f - armorPenaltyFactor);
        lastHitDamage.put(target.getId(), damage);

        if (KnightArmorWeapons.DAMAGE_DEBUG && !target.getWorld().isClient) {
            float armorPenaltyPct = Math.min(armorValue * 2.5f, 50f);
            String armorTag = armorValue > 0 ? String.format(" §c[Armor -%.0f%%]§f", armorPenaltyPct) : "";
            attacker.sendMessage(Text.literal(
                String.format("§5[HandScythe DEBUG]§f Raw: §c%.1f§f (%.1f)%s -> Final: §a%.1f§f (%.1f)",
                    originalDamage, originalDamage / 2f,
                    armorTag,
                    damage, damage / 2f)), false);
        }

        return damage;
    }
}
