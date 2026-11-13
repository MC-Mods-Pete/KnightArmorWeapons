package net.petemc.knightarmorweapons.events;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
 * Spiked Mace mechanics (Forge: ShieldBlockEvent + LivingHurtEvent).
 *
 *   A) Shield Obliteration: a raised shield is disabled for 100 ticks, 50% damage goes through.
 *   B) Armor Sunder:        +2% damage per armor point (max +40% at 20 points).
 *   C) Daze / Concussion:   every hit applies Slowness III for 30 ticks (in SpikedMace.hurtEnemy).
 *
 * The shield check runs in an attack callback, the damage in LivingEntityMixin.
 */
public class SpikedMaceCombatEvents {

    /** Entity IDs whose shield was just obliterated; read and cleared in modifyDamage. */
    private static final Set<Integer> obliteratedShields =
            Collections.synchronizedSet(new HashSet<>());

    public static void register() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
            if (!(player.getMainHandItem().getItem() instanceof SpikedMace)) return InteractionResult.PASS;
            if (!(entity instanceof LivingEntity target)) return InteractionResult.PASS;

            if (target.isDamageSourceBlocked(world.damageSources().playerAttack(player))) {
                if (target instanceof Player blockerPlayer) {
                    blockerPlayer.getCooldowns().addCooldown(Items.SHIELD, 100);
                    blockerPlayer.stopUsingItem();
                }
                obliteratedShields.add(target.getId());
            }
            return InteractionResult.PASS;
        });
    }

    public static float modifyDamage(LivingEntity target, float originalDamage, Player attacker) {
        ItemStack held = attacker.getMainHandItem();
        if (!(held.getItem() instanceof SpikedMace)) return originalDamage;

        float damage = originalDamage;

        boolean shieldObliterated = obliteratedShields.remove(target.getId());
        if (shieldObliterated) {
            damage *= 0.50f;
        }

        int armorValue = target.getArmorValue();
        float sunderBonus = Math.min(armorValue * 0.02f, 0.40f);
        damage *= (1.0f + sunderBonus);

        if (KnightArmorWeapons.DAMAGE_DEBUG && !target.level().isClientSide()) {
            String shieldTag = shieldObliterated ? " §c[Shield Obliterated ×0.5]§f" : "";
            float sunderPct = Math.min(armorValue * 2, 40);
            String sunderTag = armorValue > 0 ? String.format(" §e[Sunder +%.0f%%]§f", sunderPct) : "";
            attacker.sendSystemMessage(Component.literal(
                String.format("§6[SpikedMace DEBUG]§f Raw: §c%.1f§f (§c♥%.1f§f)%s%s → Final: §a%.1f§f (§a♥%.1f§f)",
                    originalDamage, originalDamage / 2f,
                    shieldTag, sunderTag,
                    damage, damage / 2f)));
        }

        return damage;
    }
}
