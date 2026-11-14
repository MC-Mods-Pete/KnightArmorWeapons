package net.petemc.knightarmorweapons.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.events.HandScytheCombatEvents;

import java.util.List;

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
public class HandScythe extends Item {

    /** The Hand Scythe can not be repaired in the anvil (empty tag: data/knightarmorweapons/tags/item/repairs_knight_scythe.json) */
    public static final TagKey<Item> REPAIRS_KNIGHT_SCYTHE = TagKey.create(Registries.ITEM, KnightArmorWeapons.id("repairs_knight_scythe"));

    // Same values as the older versions: 200 uses, mining speed 8, +4 damage, iron mining level, enchantability 30
    static final ToolMaterial MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 200, 8.0F, 4.0F, 30, REPAIRS_KNIGHT_SCYTHE);

    public HandScythe(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    /** 1 (player) + 3 + 4 = 8 damage, speed 4.0 - 2.9 = 1.1 (heavy & sluggish). */
    static Item.Properties properties() {
        return new Item.Properties().sword(MATERIAL, 3.0F, -2.9F);
    }

    // B) ANTI-CLIMB COUNTER + C) DYNAMIC CLEAVE (Forge: LivingHurtEvent in HandScytheCombatEvents)
    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        if (!(attacker.level() instanceof ServerLevel level) || !(attacker instanceof Player player)) {
            return;
        }

        // B) Anti-Climb Counter: 50% chance to knock climbing zombies off walls
        if (target instanceof Zombie zombie && zombie.onClimbable()) {
            if (player.getRandom().nextFloat() < 0.50f) {
                double dx = zombie.getX() - player.getX();
                double dz = zombie.getZ() - player.getZ();
                double length = Math.sqrt(dx * dx + dz * dz);
                if (length > 0.001) { dx /= length; dz /= length; }
                zombie.setDeltaMovement(dx * 0.6, 0.35, dz * 0.6);
                zombie.needsSync = true;
            }
        }

        // C) Dynamic Cleave: hit up to 2 nearby enemies for 50% of this hit's (armor-reduced) damage
        Float hitDamage = HandScytheCombatEvents.lastHitDamage.remove(target.getId());
        float cleaveDamage = (hitDamage != null ? hitDamage
                : (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE)) * 0.5f;
        DamageSource cleaveSource = level.damageSources().playerAttack(player);

        List<LivingEntity> nearbyEnemies = level.getEntitiesOfClass(
                LivingEntity.class,
                target.getBoundingBox().inflate(2.0),
                n -> isCleaveTarget(player, target, n));

        int cleaveCount = 0;
        for (LivingEntity cleaveTarget : nearbyEnemies) {
            if (cleaveCount >= 2) break;
            HandScytheCombatEvents.pendingCleave.add(cleaveTarget.getId());
            cleaveTarget.hurtServer(level, cleaveSource, cleaveDamage);
            HandScytheCombatEvents.pendingCleave.remove(cleaveTarget.getId());
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
        if (other instanceof OwnableEntity pet && pet.getOwnerReference() != null
                && player.getUUID().equals(pet.getOwnerReference().getUUID())) {
            return false;
        }
        return !(other instanceof Player otherPlayer) || player.canHarmPlayer(otherPlayer);
    }
}
