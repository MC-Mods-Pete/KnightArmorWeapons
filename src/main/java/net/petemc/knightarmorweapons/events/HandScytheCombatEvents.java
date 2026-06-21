package net.petemc.knightarmorweapons.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.items.HandScythe;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles all unique combat mechanics of the Hand Scythe.
 * Fabric migration: call applyCombatModifiers() from your LivingEntityHurt event listener.
 *
 * Mechanics:
 *   A) Armor Penalty      — 2.5% damage reduction per armor point (cap 50% at 20 pts)
 *   B) Shield Bypass      — bypasses block but deals only 50% damage
 *   C) Anti-Climb Counter — 50% chance to knock climbing Zombies off walls
 *   D) Dynamic Cleave     — hits up to 2 nearby enemies for 50% of armor-reduced damage
 */
public class HandScytheCombatEvents {

    private static final Set<Integer> pendingCleave =
            Collections.synchronizedSet(new HashSet<>());
    private static final Set<Integer> pendingShieldBypass =
            Collections.synchronizedSet(new HashSet<>());

    /**
     * Apply Hand Scythe combat modifiers to a damage event.
     * Call this from your Fabric LivingEntityHurtEvent listener.
     * Returns the modified damage amount.
     */
    public static float applyCombatModifiers(
            LivingEntity target,
            float originalDamage,
            Player attacker,
            DamageSource source) {

        // Loop guard: ignore our own cleave damage
        if (pendingCleave.contains(target.getId())) return originalDamage;

        ItemStack held = attacker.getMainHandItem();
        if (!(held.getItem() instanceof HandScythe)) return originalDamage;

        int armorValue = target.getArmorValue();
        float armorPenaltyFactor = Math.min(armorValue * 0.025f, 0.50f);
        float damage = originalDamage * (1.0f - armorPenaltyFactor);

        boolean shieldBypassed = pendingShieldBypass.remove(target.getId());
        if (shieldBypassed) {
            damage *= 0.50f;
        }

        if (KnightArmorWeapons.DAMAGE_DEBUG && !target.level().isClientSide()) {
            float armorPenaltyPct = Math.min(armorValue * 2.5f, 50f);
            String armorTag = armorValue > 0 ? String.format(" §c[Armor -%.0f%%]§f", armorPenaltyPct) : "";
            String shieldTag = shieldBypassed ? " §c[Shield x0.5]§f" : "";
            attacker.sendSystemMessage(Component.literal(
                String.format("§5[HandScythe DEBUG]§f Raw: §c%.1f§f (%.1f)%s%s -> Final: §a%.1f§f (%.1f)",
                    originalDamage, originalDamage / 2f,
                    armorTag, shieldTag,
                    damage, damage / 2f)));
        }

        if (target instanceof Zombie zombie && zombie.onClimbable()) {
            if (attacker.getRandom().nextFloat() < 0.50f) {
                double dx = zombie.getX() - attacker.getX();
                double dz = zombie.getZ() - attacker.getZ();
                double length = Math.sqrt(dx * dx + dz * dz);
                if (length > 0.001) { dx /= length; dz /= length; }
                zombie.setDeltaMovement(dx * 0.6, 0.35, dz * 0.6);
                zombie.hasImpulse = true;
            }
        }

        float cleaveDamage = damage * 0.50f;
        DamageSource cleaveSource = target.level().damageSources().playerAttack(attacker);
        List<LivingEntity> nearbyEnemies = target.level().getEntitiesOfClass(
                LivingEntity.class,
                target.getBoundingBox().inflate(2.0),
                n -> n != target && n != attacker && n.isAlive() && !n.isAlliedTo(attacker));

        int cleaveCount = 0;
        for (LivingEntity cleaveTarget : nearbyEnemies) {
            if (cleaveCount >= 2) break;
            pendingCleave.add(cleaveTarget.getId());
            cleaveTarget.hurt(cleaveSource, cleaveDamage);
            pendingCleave.remove(cleaveTarget.getId());
            double dx = cleaveTarget.getX() - attacker.getX();
            double dz = cleaveTarget.getZ() - attacker.getZ();
            cleaveTarget.knockback(0.4, -dx, -dz);
            cleaveCount++;
        }

        return damage;
    }
}
