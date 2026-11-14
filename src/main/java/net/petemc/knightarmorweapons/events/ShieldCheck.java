package net.petemc.knightarmorweapons.events;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.phys.Vec3;

/**
 * Tells whether a raised shield would block this hit. Since 1.21.2 vanilla has no isDamageSourceBlocked() anymore,
 * the block is computed inside LivingEntity.applyItemBlocking(); this repeats the same check without its side effects
 * (shield damage, disabling the attacker's knockback), so Spiked Mace and Hand Scythe can react before the block happens.
 */
public final class ShieldCheck {
    private ShieldCheck() {}

    public static boolean wouldBlock(LivingEntity target, DamageSource source, float damage) {
        if (damage <= 0.0F) return false;
        ItemStack blockingWith = target.getItemBlockingWith();
        if (blockingWith == null) return false;

        BlocksAttacks blocksAttacks = blockingWith.get(DataComponents.BLOCKS_ATTACKS);
        if (blocksAttacks == null || blocksAttacks.bypassedBy().map(t -> t.contains(source.typeHolder())).orElse(false)) {
            return false;
        }
        if (source.getDirectEntity() instanceof AbstractArrow arrow && arrow.getPierceLevel() > 0) {
            return false;
        }

        double angle;
        Vec3 sourcePosition = source.getSourcePosition();
        if (sourcePosition != null) {
            Vec3 viewVector = target.calculateViewVector(0.0F, target.getYHeadRot());
            Vec3 vectorTo = sourcePosition.subtract(target.position());
            vectorTo = new Vec3(vectorTo.x, 0.0, vectorTo.z).normalize();
            angle = Math.acos(vectorTo.dot(viewVector));
        } else {
            angle = (float) Math.PI;
        }
        return blocksAttacks.resolveBlockedDamage(source, damage, angle) > 0.0F;
    }
}
