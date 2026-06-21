package net.petemc.knightarmorweapons.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.items.KnightLongSword;

/**
 * Handles all unique combat mechanics of the Knight Longsword.
 * Fabric migration: call applyCombatModifiers() from your LivingEntityHurt event listener.
 *
 * Mechanics:
 *   B) Close-Quarters Penalty (Dead Zone)
 *      → If the target is closer than 1.8 blocks, deal only 60% base damage.
 *
 *   C) Fencer's Precision (Sweet-Spot Bonus)
 *      → If the target is between 3.5 and 4.5 blocks away, deal 120% base damage.
 */
public class KnightLongSwordCombatEvents {

    private static final double DEAD_ZONE_MAX   = 1.8;
    private static final double SWEET_SPOT_MIN  = 3.5;
    private static final double SWEET_SPOT_MAX  = 4.5;
    private static final float DEAD_ZONE_FACTOR  = 0.60f;
    private static final float PRECISION_FACTOR  = 1.20f;

    /**
     * Apply Longsword combat modifiers to a damage event.
     * Call this from your Fabric LifeEntityHurtEvent listener when the attacker holds a KnightLongSword.
     * Returns the modified damage amount.
     */
    public static float applyCombatModifiers(LivingEntity target, float originalDamage, Player attacker) {
        DamageSource source = target.getLastDamageCause(); // or pass via event
        if (source == null) return originalDamage;

        Entity directAttacker = source.getDirectEntity();
        if (!(directAttacker instanceof Player player)) return originalDamage;

        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof KnightLongSword)) return originalDamage;

        float damage = originalDamage;
        double distance = player.distanceTo(target);
        String zoneTag;

        if (distance < DEAD_ZONE_MAX) {
            damage *= DEAD_ZONE_FACTOR;
            zoneTag = String.format(" §c[Dead Zone %.1fm -40%%]§f", distance);
        } else if (distance >= SWEET_SPOT_MIN && distance <= SWEET_SPOT_MAX) {
            damage *= PRECISION_FACTOR;
            zoneTag = String.format(" §a[Sweet Spot %.1fm +20%%]§f", distance);
        } else {
            zoneTag = String.format(" §7[Normal %.1fm]§f", distance);
        }

        if (KnightArmorWeapons.DAMAGE_DEBUG && !target.level().isClientSide()) {
            player.sendSystemMessage(Component.literal(
                String.format("§b[LongSword DEBUG]§f Raw: §c%.1f§f (§c♥%.1f§f)%s → Final: §a%.1f§f (§a♥%.1f§f)",
                    originalDamage, originalDamage / 2f,
                    zoneTag,
                    damage, damage / 2f)));
        }

        return damage;
    }
}

