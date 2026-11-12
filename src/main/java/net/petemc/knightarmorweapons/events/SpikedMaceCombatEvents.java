package net.petemc.knightarmorweapons.events;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.items.SpikedMace;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles the two unique combat mechanics of the Spiked Mace.
 *
 * Architecture (Fabric port):
 *   A) Shield Obliteration — registered via AttackEntityCallback (fires before hit).
 *                            Disables the blocker's shield and marks them for 50% damage.
 *   B) Armor Sunder        — modifyDamage() called from LivingEntityMixin.
 *   C) Daze / Concussion   — applied in SpikedMace.postHit().
 *
 * Call register() from KnightArmorWeapons.onInitialize().
 */
public class SpikedMaceCombatEvents {

    /**
     * Thread-safe set of entity IDs whose shield was just obliterated.
     * Written in the AttackEntityCallback, read & cleared in modifyDamage().
     */
    private static final Set<Integer> obliteratedShields =
            Collections.synchronizedSet(new HashSet<>());

    // ─────────────────────────────────────────────────────────────────────────
    // Register Fabric event callbacks
    // ────────────────────────────────────────────────────────────────────────

    public static void register() {
        // A) SHIELD OBLITERATION — intercept attack BEFORE damage is applied
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (hand != Hand.MAIN_HAND) return ActionResult.PASS;
            if (!(player.getMainHandStack().getItem() instanceof SpikedMace)) return ActionResult.PASS;
            if (!(entity instanceof LivingEntity target)) return ActionResult.PASS;

            if (target.isBlocking()) {
                // Force shield offline (100-tick cooldown, identical to axe stagger but guaranteed)
                if (target instanceof PlayerEntity blockerPlayer) {
                    blockerPlayer.getItemCooldownManager().set(Items.SHIELD, 100);
                    blockerPlayer.stopUsingItem();
                }
                // Mark for 50% crushing-damage reduction in modifyDamage()
                obliteratedShields.add(target.getId());
            }
            return ActionResult.PASS;
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // A + B) SHIELD 50% + ARMOR SUNDER — called from LivingEntityMixin
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Modifies incoming damage for the two Spiked Mace passives.
     * Called from LivingEntityMixin when the attacker holds a SpikedMace.
     */
    public static float modifyDamage(LivingEntity target, float originalDamage, PlayerEntity attacker) {
        ItemStack held = attacker.getMainHandStack();
        if (!(held.getItem() instanceof SpikedMace)) return originalDamage;

        float damage = originalDamage;
        float rawDamage = damage;

        // A) Shield Obliteration Step 2: 50% crushing impact
        boolean shieldObliterated = obliteratedShields.remove(target.getId());
        if (shieldObliterated) {
            damage *= 0.50f;
        }

        // B) Armor Sunder: +2% per armor point, capped at +40%
        int armorValue = target.getArmor();
        float sunderBonus = Math.min(armorValue * 0.02f, 0.40f);
        damage *= (1.0f + sunderBonus);

        if (KnightArmorWeapons.DAMAGE_DEBUG) {
            String shieldTag = shieldObliterated ? " §c[Shield Obliterated ×0.5]§f" : "";
            float sunderPct = Math.min(armorValue * 2, 40);
            String sunderTag = armorValue > 0 ? String.format(" §e[Sunder +%.0f%%]§f", sunderPct) : "";
            attacker.sendMessage(Text.literal(
                String.format("§6[SpikedMace DEBUG]§f Raw: §c%.1f§f (§c♥%.1f§f)%s%s → Final: §a%.1f§f (§a♥%.1f§f)",
                    rawDamage, rawDamage / 2f,
                    shieldTag, sunderTag,
                    damage, damage / 2f)), false);
        }

        return damage;
    }
}
