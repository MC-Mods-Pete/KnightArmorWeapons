package net.petemc.knightarmorweapons.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.items.HandScythe;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles all unique combat mechanics of the Hand Scythe.
 *
 * Subscribed to the FORGE event bus via @EventBusSubscriber — no manual
 * registration needed in KnightArmorWeapons.java.
 *
 * Mechanics:
 *   A) Armor Penalty      — 2.5% damage reduction per armor point (cap 50% at 20 pts)
 *   B) Shield Bypass      — bypasses block but deals only 50% damage
 *   C) Anti-Climb Counter — 50% chance to knock climbing Zombies off walls
 *   D) Dynamic Cleave     — hits up to 2 nearby enemies for 50% of armor-reduced damage
 */
@Mod.EventBusSubscriber(modid = KnightArmorWeapons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HandScytheCombatEvents {

    /**
     * Thread-safe set of entity IDs that are currently receiving cleave damage.
     * Prevents infinite cleave → cleave loops in onLivingHurt.
     */
    private static final Set<Integer> pendingCleave =
            Collections.synchronizedSet(new HashSet<>());

    /**
     * Thread-safe set of entity IDs whose shield block was just bypassed by the Hand Scythe.
     * Set in onShieldBlock, consumed in onLivingHurt to apply 50% penalty.
     */
    private static final Set<Integer> pendingShieldBypass =
            Collections.synchronizedSet(new HashSet<>());

    // ─────────────────────────────────────────────────────────────────────────
    // B) Shield Bypass — cancel the shield block, mark for 50% damage reduction
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Called when a shield would normally block an attack.
     * If the attacker is wielding a Hand Scythe, cancel the block entirely
     * and record the target entity so onLivingHurt can apply the 50% penalty.
     */
    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        LivingEntity blocker = event.getEntity();

        // Cleave hits are blocked normally, only the main hit bypasses the shield
        if (pendingCleave.contains(blocker.getId())) return;

        if (!(event.getDamageSource().getDirectEntity() instanceof Player attacker)) return;
        if (!(attacker.getMainHandItem().getItem() instanceof HandScythe)) return;

        event.setCanceled(true); // shield does not block, no shield durability loss
        pendingShieldBypass.add(blocker.getId());
    }


    // ─────────────────────────────────────────────────────────────────────────
    // Main LivingHurtEvent: A) Armor Penalty + B) Shield 50% + C) Anti-Climb + D) Cleave
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Called every time a living entity is about to take damage.
     * Applies all Hand Scythe mechanics in sequence.
     */
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();

        // ── Loop guard: ignore our own cleave damage to prevent infinite recursion ──
        if (pendingCleave.contains(event.getEntity().getId())) return;

        // ── Guard: must be a direct player attack ──
        Entity directAttacker = source.getDirectEntity();
        if (!(directAttacker instanceof Player player)) return;

        // ── Guard: player must hold a Hand Scythe in the main hand ──
        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof HandScythe)) return;

        LivingEntity target = event.getEntity();

        // ────────────────────────────────────────────────────────────────────
        // A) ARMOR PENALTY
        //    2.5% reduction per armor point, capped at 50% (20 armor points).
        //    Note: this is applied ON TOP of Minecraft's own armor damage
        //    reduction — it represents the scythe's poor penetration ability.
        // ────────────────────────────────────────────────────────────────────
        int armorValue = target.getArmorValue();                     // 0–20 range
        float armorPenaltyFactor = Math.min(armorValue * 0.025f, 0.50f); // 0.0 – 0.50
        float rawDamage = event.getAmount(); // snapshot before modifiers
        float damage = rawDamage * (1.0f - armorPenaltyFactor);

        // ────────────────────────────────────────────────────────────────────
        // B) SHIELD BYPASS (50% damage reduction)
        //    onShieldBlock already cancelled the actual block.
        //    Here we apply the compensating 50% damage penalty.
        // ────────────────────────────────────────────────────────────────────
        boolean shieldBypassed = pendingShieldBypass.remove(target.getId());
        if (shieldBypassed) {
            damage *= 0.50f; // half damage gets through the bypassed shield
        }

        // Apply the final modified damage
        event.setAmount(damage);

        // ── DEBUG: print damage breakdown to the attacker's chat ──
        if (KnightArmorWeapons.DAMAGE_DEBUG) {
            float armorPenaltyPct = Math.min(armorValue * 2.5f, 50f);
            String armorTag = armorValue > 0 ? String.format(" §c[Armor −%.0f%%]§f", armorPenaltyPct) : "";
            String shieldTag = shieldBypassed ? " §c[Shield ×0.5]§f" : "";
            player.sendSystemMessage(Component.literal(
                String.format("§5[HandScythe DEBUG]§f Raw: §c%.1f§f (§c♥%.1f§f)%s%s → Final: §a%.1f§f (§a♥%.1f§f)",
                    rawDamage, rawDamage / 2f,
                    armorTag, shieldTag,
                    damage, damage / 2f)));
        }

        // ────────────────────────────────────────────────────────────────────
        // C) ANTI-CLIMB COUNTER
        //    If the target is a Zombie currently climbing a wall,
        //    50% chance to knock them off with backward/downward velocity.
        // ────────────────────────────────────────────────────────────────────
        if (target instanceof Zombie zombie && zombie.onClimbable()) {
            if (player.getRandom().nextFloat() < 0.50f) {
                // Push away from attacker and slightly upward to detach from wall
                double dx = zombie.getX() - player.getX();
                double dz = zombie.getZ() - player.getZ();
                double length = Math.sqrt(dx * dx + dz * dz);
                if (length > 0.001) { dx /= length; dz /= length; }
                zombie.setDeltaMovement(dx * 0.6, 0.35, dz * 0.6);
                zombie.hasImpulse = true; // ensure client sees velocity change
            }
        }

        // ────────────────────────────────────────────────────────────────────
        // D) DYNAMIC CLEAVE (Crowd Control)
        //    Hit up to 2 additional nearby enemies within 2 blocks.
        //    Uses EntityDamageSource with a custom ID to prevent recursion.
        //    Cleave damage = 50% of the already armor-reduced damage.
        // ────────────────────────────────────────────────────────────────────
        float cleaveDamage = damage * 0.50f;

        // Custom source: reuse player's standard attack source (loop guard uses pendingCleave set)
        DamageSource cleaveSource = target.level().damageSources().playerAttack(player);

        // Find nearby living entities (2-block radius), excluding primary target and player
        List<LivingEntity> nearbyEnemies = target.level().getEntitiesOfClass(
                LivingEntity.class,
                target.getBoundingBox().inflate(2.0),
                nearby -> isCleaveTarget(player, target, nearby)
        );

        int cleaveCount = 0;
        for (LivingEntity cleaveTarget : nearbyEnemies) {
            if (cleaveCount >= 2) break; // max 2 additional targets

            // Deal cleave damage (pendingCleave set prevents onLivingHurt from triggering again)
            pendingCleave.add(cleaveTarget.getId());
            cleaveTarget.hurt(cleaveSource, cleaveDamage);
            pendingCleave.remove(cleaveTarget.getId());

            // Apply moderate knockback away from the player
            double dx = cleaveTarget.getX() - player.getX();
            double dz = cleaveTarget.getZ() - player.getZ();
            cleaveTarget.knockback(0.4, -dx, -dz);

            cleaveCount++;
        }
    }

    /**
     * Cleave only hits real enemies: not armor stands, allies, the player's own pets,
     * and no other players when PvP is off (or same team without friendly fire).
     */
    private static boolean isCleaveTarget(Player player, LivingEntity target, LivingEntity other) {
        if (other == player || other == target || !other.isAlive() || other instanceof ArmorStand) {
            return false;
        }
        if (player.isAlliedTo(other) || other.isAlliedTo(player)) {
            return false;
        }
        if (other instanceof OwnableEntity pet && player.getUUID().equals(pet.getOwnerUUID())) {
            return false;
        }
        return !(other instanceof Player otherPlayer) || player.canHarmPlayer(otherPlayer);
    }
}
