package net.petemc.knightarmorweapons.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.petemc.knightarmorweapons.events.HandScytheCombatEvents;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Hand Scythe — a heavy crowd-control weapon for unarmored hordes.
 *
 * Attack speed: final = 4.0 + modifier → modifier = 1.1 - 4.0 = -2.9
 *
 * Unique mechanics:
 *  A) Armor Penalty      — 2.5% damage reduction per armor point (max 50%) [via LivingEntityMixin]
 *  B) Anti-Climb Counter — 50% chance to knock climbing Zombies off walls [postHit]
 *  C) Dynamic Cleave     — hits up to 2 extra enemies within 2 blocks for 50% damage [postHit]
 */
public class HandScythe extends SwordItem {

    public HandScythe() {
        super(new ToolMaterial() {
            @Override public int getDurability()              { return 200; }
            @Override public float getMiningSpeedMultiplier() { return 8.0F; }
            @Override public float getAttackDamage()          { return 4.0F; }  // 1 + 4 + 3 = 8 total
            @Override public int getMiningLevel()             { return 3; }
            @Override public int getEnchantability()          { return 30; }
            @Override public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.ofStacks();
            }
        },
        3,      // attackDamage modifier → total = 1 + 4 + 3 = 8 damage
        -2.9F,  // attackSpeed modifier  → final = 4.0 + (-2.9) = 1.1 (heavy & sluggish)
        new Item.Settings().maxCount(1));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // B) ANTI-CLIMB COUNTER + C) DYNAMIC CLEAVE
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.getWorld().isClient) return super.postHit(stack, target, attacker);
        if (!(attacker instanceof PlayerEntity player)) return super.postHit(stack, target, attacker);

        // B) Anti-Climb Counter: knock climbing zombies off walls
        if (target instanceof ZombieEntity zombie && zombie.isClimbing()) {
            if (player.getRandom().nextFloat() < 0.50f) {
                double dx = zombie.getX() - player.getX();
                double dz = zombie.getZ() - player.getZ();
                double length = Math.sqrt(dx * dx + dz * dz);
                if (length > 0.001) { dx /= length; dz /= length; }
                zombie.setVelocity(dx * 0.6, 0.35, dz * 0.6);
                zombie.velocityModified = true;
            }
        }

        // C) Dynamic Cleave: hit up to 2 nearby enemies for 50% damage
        // Cleave damage = 50% of the final damage of this hit (same as Forge)
        Float hitDamage = HandScytheCombatEvents.lastHitDamage.remove(target.getId());
        float cleaveDamage = (hitDamage != null ? hitDamage
                : (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)) * 0.5f;
        DamageSource cleaveSource = target.getWorld().getDamageSources().playerAttack(player);

        List<LivingEntity> nearbyEnemies = target.getWorld().getEntitiesByClass(
                LivingEntity.class,
                target.getBoundingBox().expand(2.0),
                n -> isCleaveTarget(player, target, n));

        int cleaveCount = 0;
        for (LivingEntity cleaveTarget : nearbyEnemies) {
            if (cleaveCount >= 2) break;
            HandScytheCombatEvents.pendingCleave.add(cleaveTarget.getId());
            cleaveTarget.damage(cleaveSource, cleaveDamage);
            HandScytheCombatEvents.pendingCleave.remove(cleaveTarget.getId());
            double dx = cleaveTarget.getX() - player.getX();
            double dz = cleaveTarget.getZ() - player.getZ();
            cleaveTarget.takeKnockback(0.4, -dx, -dz);
            cleaveCount++;
        }

        return super.postHit(stack, target, attacker);
    }

    /**
     * Cleave only hits real enemies: not armor stands, teammates, the player's own pets,
     * and no other players when PvP is off (or same team without friendly fire).
     */
    private static boolean isCleaveTarget(PlayerEntity player, LivingEntity target, LivingEntity other) {
        if (other == player || other == target || !other.isAlive() || other instanceof ArmorStandEntity) {
            return false;
        }
        if (player.isTeammate(other) || other.isTeammate(player)) {
            return false;
        }
        if (other instanceof Tameable pet && player.getUuid().equals(pet.getOwnerUuid())) {
            return false;
        }
        return !(other instanceof PlayerEntity otherPlayer) || player.shouldDamagePlayer(otherPlayer);
    }
}
