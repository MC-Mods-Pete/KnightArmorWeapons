package net.petemc.knightarmorweapons.events;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.items.SpikedMace;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles the two unique combat mechanics of the Spiked Mace.
 *
 * Registered automatically on the FORGE event bus via @EventBusSubscriber.
 * No manual registration needed in KnightArmorWeapons.java.
 *
 * Mechanics:
 *   A) Shield Obliteration — 100% guaranteed shield disable (100-tick cooldown)
 *                            + 50% crushing damage through the broken guard.
 *   B) Armor Sunder        — Each armor point gives +2% damage (max +40% at 20 pts).
 *                            Heavily armored targets are hurt MORE, not less.
 *   C) Daze / Concussion   — Every hit applies Slowness III for 30 ticks (1.5 s).
 *                            Stops sprinting elite zombies dead, giving the player
 *                            time to recover from the slow 0.8 attack speed.
 */
@Mod.EventBusSubscriber(modid = KnightArmorWeapons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpikedMaceCombatEvents {

    /**
     * Thread-safe set of entity IDs whose shield was just obliterated.
     * Written in onShieldBlock, read & cleared in onLivingHurt.
     * Used to apply the 50% crushing damage penalty exactly once per hit.
     */
    private static final Set<Integer> obliteratedShields =
            Collections.synchronizedSet(new HashSet<>());

    // ─────────────────────────────────────────────────────────────────────────
    // A) SHIELD OBLITERATION — Step 1: cancel the block, disable the shield
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Fired when a shield is about to block an attack.
     * If the attacker holds a Spiked Mace:
     *  - Cancel the block (attack lands regardless).
     *  - Apply a 100-tick (5 second) shield cooldown — identical to an axe stagger
     *    but with 100% guaranteed trigger (no RNG, no critical hit required).
     *  - Mark the blocker so onLivingHurt can apply the 50% damage reduction.
     */
    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        DamageSource source = event.getDamageSource();

        // Only trigger for direct player attacks
        if (!(source.getDirectEntity() instanceof Player attacker)) return;

        // Only trigger when holding a Spiked Mace
        ItemStack held = attacker.getMainHandItem();
        if (!(held.getItem() instanceof SpikedMace)) return;

        // ── Cancel the block: the mace spikes punch straight through ──
        event.setCanceled(true);

        // ── Apply shield cooldown to disable it (same length as axe stagger) ──
        LivingEntity blocker = event.getEntity();
        if (blocker instanceof Player blockerPlayer) {
            // Add 100-tick cooldown (5 s) to the Shield item type
            blockerPlayer.getCooldowns().addCooldown(Items.SHIELD, 100);
            // Force them to stop blocking immediately
            blockerPlayer.stopUsingItem();
        }

        // ── Mark for 50% crushing damage in onLivingHurt ──
        obliteratedShields.add(blocker.getId());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // A + B) SHIELD 50% + ARMOR SUNDER — applied in LivingHurtEvent
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Fired every time a living entity is about to take damage.
     * Applies both Spiked Mace modifiers to the damage amount:
     *   A) If the shield was just obliterated → halve the damage (50% crushing impact).
     *   B) Armor Sunder: each armor point adds +2% damage (max +40% at 20 pts).
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();

        // Guard: must be a direct player attack
        Entity directAttacker = source.getDirectEntity();
        if (!(directAttacker instanceof Player attacker)) return;

        // Guard: attacker must hold a Spiked Mace in the main hand
        if (!(attacker.getMainHandItem().getItem() instanceof SpikedMace)) return;

        LivingEntity target = event.getEntity();
        float damage = event.getAmount();
        float rawDamage = damage; // snapshot before any modifiers

        // ────────────────────────────────────────────────────────────────────
        // A) SHIELD OBLITERATION — Step 2: 50% crushing impact damage
        //    The shield was bypassed in onShieldBlock; we now apply 50% so
        //    blocking still provides SOME protection but cannot fully negate
        //    the mace's weight.
        // ────────────────────────────────────────────────────────────────────
        boolean shieldObliterated = obliteratedShields.remove(target.getId());
        if (shieldObliterated) {
            damage *= 0.50f; // half damage: brutal, but shield did absorb some impact
        }

        // ────────────────────────────────────────────────────────────────────
        // B) ARMOR SUNDER
        //    Heavy armor = MORE spikes land in plate gaps = MORE damage.
        //    +2% per armor point, capped at +40% (20 armor points).
        //    This is calculated AFTER the shield reduction so both effects
        //    stack correctly (shield half → then armor bonus applied).
        // ────────────────────────────────────────────────────────────────────
        int armorValue = target.getArmorValue();                      // 0–20 range
        float sunderBonus = Math.min(armorValue * 0.02f, 0.40f);      // 0.0 – 0.40
        damage *= (1.0f + sunderBonus);

        event.setAmount(damage);

        // ────────────────────────────────────────────────────────────────────
        // C) DAZE / CONCUSSION
        //    The mace's brutal impact stuns the target.
        //    Slowness III for 30 ticks (1.5 s) — halts sprinting elite zombies
        //    and gives the player time to recover from the slow 0.8 attack speed.
        //    ambient=false → shows particles; showIcon=true → visible in HUD.
        // ────────────────────────────────────────────────────────────────────
        target.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                30,   // duration in ticks (1.5 seconds)
                2,    // amplifier 2 = Slowness III (0-indexed)
                false, // not ambient (shows particles)
                true   // show icon in HUD
        ));

        // ── DEBUG: print damage breakdown to the attacker's chat ──
        if (KnightArmorWeapons.DAMAGE_DEBUG) {
            String shieldTag = shieldObliterated ? " §c[Shield Obliterated ×0.5]§f" : "";
            int armorVal = target.getArmorValue();
            float sunderPct = Math.min(armorVal * 2, 40);
            String sunderTag = armorVal > 0 ? String.format(" §e[Sunder +%.0f%%]§f", sunderPct) : "";
            attacker.sendSystemMessage(Component.literal(
                String.format("§6[SpikedMace DEBUG]§f Raw: §c%.1f§f (§c♥%.1f§f)%s%s → Final: §a%.1f§f (§a♥%.1f§f)",
                    rawDamage, rawDamage / 2f,
                    shieldTag, sunderTag,
                    damage, damage / 2f)));
        }
    }
}

