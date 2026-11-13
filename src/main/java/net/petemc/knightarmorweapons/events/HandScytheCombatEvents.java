package net.petemc.knightarmorweapons.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.items.HandScythe;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Damage modifiers of the Hand Scythe, called from LivingEntityMixin (Forge: LivingHurtEvent + ShieldBlockEvent).
 * Anti-Climb and Cleave are handled in HandScythe.hurtEnemy().
 *
 *   A) Armor Penalty: 2.5% damage reduction per armor point (max 50% at 20 points)
 *   B) Shield Bypass: the main hit goes through a raised shield but deals only 50% damage.
 *                     LivingEntityMixin makes the shield check in hurt() return false,
 *                     cleave hits are blocked normally (same as Forge).
 */
public class HandScytheCombatEvents {

    /**
     * Entity IDs currently being cleaved.
     * Read by LivingEntityMixin to skip damage modification for secondary cleave hits.
     * Written by HandScythe.hurtEnemy() around each cleave call.
     */
    public static final Set<Integer> pendingCleave =
            Collections.synchronizedSet(new HashSet<>());

    /**
     * Final damage of the last Hand Scythe hit per target entity ID.
     * Written in modifyDamage(), read and removed in HandScythe.hurtEnemy() to compute the cleave damage.
     */
    public static final Map<Integer, Float> lastHitDamage = new ConcurrentHashMap<>();

    /** True if this damage is a Hand Scythe main hit, which ignores the target's shield. */
    public static boolean bypassesShield(LivingEntity target, DamageSource source) {
        if (pendingCleave.contains(target.getId())) return false;
        return source.getDirectEntity() instanceof Player player
                && player.getMainHandItem().getItem() instanceof HandScythe;
    }

    public static float modifyDamage(LivingEntity target, float originalDamage, Player attacker,
                                     DamageSource source) {
        // Guard: skip cleave-secondary hits
        if (pendingCleave.contains(target.getId())) return originalDamage;

        ItemStack held = attacker.getMainHandItem();
        if (!(held.getItem() instanceof HandScythe)) return originalDamage;

        int armorValue = target.getArmorValue();
        float armorPenaltyFactor = Math.min(armorValue * 0.025f, 0.50f);
        float damage = originalDamage * (1.0f - armorPenaltyFactor);

        // Shield Bypass: the raised shield would block this hit, half the damage goes through.
        // isDamageSourceBlocked() is called directly here, only the call inside hurt() is overridden.
        boolean shieldBypassed = bypassesShield(target, source) && target.isDamageSourceBlocked(source);
        if (shieldBypassed) {
            damage *= 0.50f;
        }
        lastHitDamage.put(target.getId(), damage);

        if (KnightArmorWeapons.DAMAGE_DEBUG && !target.level().isClientSide()) {
            float armorPenaltyPct = Math.min(armorValue * 2.5f, 50f);
            String armorTag = armorValue > 0 ? String.format(" §c[Armor -%.0f%%]§f", armorPenaltyPct) : "";
            String shieldTag = shieldBypassed ? " §c[Shield ×0.5]§f" : "";
            attacker.sendSystemMessage(Component.literal(
                String.format("§5[HandScythe DEBUG]§f Raw: §c%.1f§f (%.1f)%s%s -> Final: §a%.1f§f (%.1f)",
                    originalDamage, originalDamage / 2f,
                    armorTag, shieldTag,
                    damage, damage / 2f)));
        }

        return damage;
    }
}
