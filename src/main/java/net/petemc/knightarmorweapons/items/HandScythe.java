package net.petemc.knightarmorweapons.items;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.petemc.knightarmorweapons.events.HandScytheCombatEvents;
import java.util.List;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Hand Scythe — a heavy crowd-control weapon for unarmored hordes.
 *
 * Attack speed: final = 4.0 + modifier → modifier = 1.1 - 4.0 = -2.9F
 * (Normal sword uses -2.4F → 1.6F. This weapon is deliberately sluggish.)
 *
 * Unique mechanics are handled in HandScytheCombatEvents:
 *  A) Armor Penalty      — 2.5% damage reduction per armor point (max 50%)
 *  B) Anti-Climb Counter — 50% chance to knock climbing Zombies off walls
 *  C) Dynamic Cleave     — hits up to 2 extra enemies within 2 blocks for 50% damage
 */
public class HandScythe extends SwordItem {

    private static final Tier TIER = new Tier() {
        @Override public int getUses()                             { return 200; }
        @Override public float getSpeed()                          { return 8.0F; }
        @Override public float getAttackDamageBonus()              { return 4.0F; }
        // Replaces getLevel() == 3 (iron level) from 1.20.1
        @Override public @NotNull TagKey<Block> getIncorrectBlocksForDrops() { return BlockTags.INCORRECT_FOR_IRON_TOOL; }
        @Override public int getEnchantmentValue()                 { return 30; }
        @Override public @NotNull Ingredient getRepairIngredient() { return Ingredient.of(); }
    };

    public HandScythe() {
        // attackDamageModifier 3, attackSpeedModifier -2.9 → final speed 4.0 - 2.9 = 1.1 (heavy & sluggish)
        super(TIER, new Item.Properties().stacksTo(1).attributes(SwordItem.createAttributes(TIER, 3, -2.9F)));
    }

    // B) ANTI-CLIMB COUNTER + C) DYNAMIC CLEAVE (Forge: LivingHurtEvent in HandScytheCombatEvents)
    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (attacker.level().isClientSide() || !(attacker instanceof Player player)) {
            return super.hurtEnemy(stack, target, attacker);
        }

        // B) Anti-Climb Counter: 50% chance to knock climbing zombies off walls
        if (target instanceof Zombie zombie && zombie.onClimbable()) {
            if (player.getRandom().nextFloat() < 0.50f) {
                double dx = zombie.getX() - player.getX();
                double dz = zombie.getZ() - player.getZ();
                double length = Math.sqrt(dx * dx + dz * dz);
                if (length > 0.001) { dx /= length; dz /= length; }
                zombie.setDeltaMovement(dx * 0.6, 0.35, dz * 0.6);
                zombie.hasImpulse = true;
            }
        }

        // C) Dynamic Cleave: hit up to 2 nearby enemies for 50% of this hit's (armor-reduced) damage
        Float hitDamage = HandScytheCombatEvents.lastHitDamage.remove(target.getId());
        float cleaveDamage = (hitDamage != null ? hitDamage
                : (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE)) * 0.5f;
        DamageSource cleaveSource = target.level().damageSources().playerAttack(player);

        List<LivingEntity> nearbyEnemies = target.level().getEntitiesOfClass(
                LivingEntity.class,
                target.getBoundingBox().inflate(2.0),
                n -> isCleaveTarget(player, target, n));

        int cleaveCount = 0;
        for (LivingEntity cleaveTarget : nearbyEnemies) {
            if (cleaveCount >= 2) break;
            HandScytheCombatEvents.pendingCleave.add(cleaveTarget.getId());
            cleaveTarget.hurt(cleaveSource, cleaveDamage);
            HandScytheCombatEvents.pendingCleave.remove(cleaveTarget.getId());
            double dx = cleaveTarget.getX() - player.getX();
            double dz = cleaveTarget.getZ() - player.getZ();
            cleaveTarget.knockback(0.4, -dx, -dz);
            cleaveCount++;
        }

        return super.hurtEnemy(stack, target, attacker);
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
