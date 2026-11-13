package net.petemc.knightarmorweapons.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.items.KnightLongSword;

/**
 * Damage modifiers of the Knight Longsword, called from LivingEntityMixin (Forge: LivingHurtEvent).
 *
 *   B) Close-Quarters Penalty: target closer than 1.8 blocks -> 60% damage (-40%)
 *   C) Fencer's Precision:     target 3.5-4.5 blocks away     -> 120% damage (+20%)
 *
 * The missing sweep attack is handled in PlayerMixin.
 */
public class KnightLongSwordCombatEvents {

    /** Below this distance the blade cannot swing properly. */
    private static final double DEAD_ZONE_MAX = 1.8;
    /** Sweet-spot range: fully extended thrust at ideal reach. */
    private static final double SWEET_SPOT_MIN = 3.5;
    private static final double SWEET_SPOT_MAX = 4.5;
    private static final float DEAD_ZONE_FACTOR = 0.60f;
    private static final float PRECISION_FACTOR = 1.20f;

    public static float modifyDamage(LivingEntity target, float originalDamage, Player attacker) {
        ItemStack held = attacker.getMainHandItem();
        if (!(held.getItem() instanceof KnightLongSword)) return originalDamage;

        float damage = originalDamage;
        double distance = attacker.distanceTo(target);
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
            attacker.sendSystemMessage(Component.literal(
                String.format("§b[LongSword DEBUG]§f Raw: §c%.1f§f (§c♥%.1f§f)%s → Final: §a%.1f§f (§a♥%.1f§f)",
                    originalDamage, originalDamage / 2f,
                    zoneTag,
                    damage, damage / 2f)));
        }

        return damage;
    }
}
