package net.petemc.knightarmorweapons.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.items.KnightLongSword;

/**
 * Handles all unique combat mechanics of the Knight Longsword.
 *
 * Subscribed to the FORGE event bus via @EventBusSubscriber — no manual
 * registration needed in KnightArmorWeapons.java.
 *
 * Mechanics:
 *   A) Complete Cleave/Sweeping Removal
 *      → Handled statically in KnightLongSword.canPerformAction().
 *        Vanilla's Player.attack() checks the ToolActions.SWORD_SWEEP flag
 *        before applying any sweep damage or particles. No event needed here.
 *
 *   B) Close-Quarters Penalty (Dead Zone)
 *      → If the target is closer than 1.8 blocks, deal only 60% base damage.
 *        The blade has no room to swing — only the pommel or weak ricasso connects.
 *
 *   C) Fencer's Precision (Sweet-Spot Bonus)
 *      → If the target is between 3.5 and 4.5 blocks away, deal 120% base damage.
 *        The blade lands a fully-extended precision thrust at maximum leverage.
 */
@EventBusSubscriber(modid = KnightArmorWeapons.MOD_ID)
public class KnightLongSwordCombatEvents {

    // ─────────────────────────────────────────────────────────────────────────
    // DISTANCE THRESHOLDS
    // ─────────────────────────────────────────────────────────────────────────

    /** Below this distance the blade cannot swing properly — pommel hit penalty. */
    private static final double DEAD_ZONE_MAX   = 1.8;

    /** Sweet-spot range: fully extended thrust at ideal reach. */
    private static final double SWEET_SPOT_MIN  = 3.5;
    private static final double SWEET_SPOT_MAX  = 4.5;

    // ─────────────────────────────────────────────────────────────────────────
    // DAMAGE MODIFIERS
    // ─────────────────────────────────────────────────────────────────────────

    /** Dead-zone: only 60% of base damage (−40%). */
    private static final float DEAD_ZONE_FACTOR  = 0.60f;

    /** Sweet-spot: 120% of base damage (+20% Precision Strike). */
    private static final float PRECISION_FACTOR  = 1.20f;

    // ─────────────────────────────────────────────────────────────────────────
    // B) Close-Quarters Penalty  +  C) Fencer's Precision
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Called every time a living entity is about to take damage.
     * Applies the range-based damage modifiers for the Knight Longsword.
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        DamageSource source = event.getContainer().getSource();

        // ── Guard: must be a direct player attack ──
        Entity directAttacker = source.getDirectEntity();
        if (!(directAttacker instanceof Player player)) return;

        // ── Guard: player must hold a Knight Longsword in the main hand ──
        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof KnightLongSword)) return;

        LivingEntity target = event.getEntity();
        float rawDamage = event.getNewDamage(); // snapshot before modifiers
        float damage = rawDamage;

        // Calculate the 3D distance between attacker and target.
        // distanceTo() returns Euclidean distance, which is the most accurate
        // measure of "how far away is the enemy" in any terrain.
        double distance = player.distanceTo(target);

        // ────────────────────────────────────────────────────────────────────
        // B) CLOSE-QUARTERS PENALTY (Dead Zone — < 1.8 blocks)
        //    The longsword is too long to swing effectively at point-blank range.
        //    The player can only hit with the pommel or the weak ricasso section.
        //    Penalty: −40% damage (multiply by 0.60).
        // ────────────────────────────────────────────────────────────────────
        String zoneTag;
        if (distance < DEAD_ZONE_MAX) {
            damage *= DEAD_ZONE_FACTOR;
            zoneTag = String.format(" §c[Dead Zone %.1fm −40%%]§f", distance);
        }
        // ────────────────────────────────────────────────────────────────────
        // C) FENCER'S PRECISION (Sweet-Spot — 3.5 to 4.5 blocks)
        //    At the ideal engagement range the longsword lands a perfectly
        //    extended thrust with full leverage and blade speed.
        //    Bonus: +20% damage — the Precision Strike.
        //    Note: mutually exclusive with the dead-zone penalty (else-if).
        // ────────────────────────────────────────────────────────────────────
        else if (distance >= SWEET_SPOT_MIN && distance <= SWEET_SPOT_MAX) {
            damage *= PRECISION_FACTOR;
            zoneTag = String.format(" §a[Sweet Spot %.1fm +20%%]§f", distance);
        }
        // In the middle zone (1.8 – 3.5 blocks) or beyond 4.5 blocks:
        // no modifier — standard damage applies.
        else {
            zoneTag = String.format(" §7[Normal %.1fm]§f", distance);
        }

        event.setNewDamage(damage);

        // ── DEBUG: print damage breakdown to the attacker's chat ──
        if (KnightArmorWeapons.DAMAGE_DEBUG) {
            player.sendSystemMessage(Component.literal(
                String.format("§b[LongSword DEBUG]§f Raw: §c%.1f§f (§c♥%.1f§f)%s → Final: §a%.1f§f (§a♥%.1f§f)",
                    rawDamage, rawDamage / 2f,
                    zoneTag,
                    damage, damage / 2f)));
        }
    }
}

