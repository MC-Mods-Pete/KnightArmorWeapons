package net.petemc.knightarmorweapons.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.petemc.knightarmorweapons.items.ModItems;

public class ThrowableAxeEntity extends AbstractArrow {

    public ThrowableAxeEntity(EntityType<? extends ThrowableAxeEntity> type, Level world) {
        super(type, world);
    }

    public ThrowableAxeEntity(EntityType<? extends ThrowableAxeEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world, new ItemStack(ModItems.THROWING_AXE.get()), null);
    }

    public ThrowableAxeEntity(EntityType<? extends ThrowableAxeEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world, new ItemStack(ModItems.THROWING_AXE.get()), null);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.THROWING_AXE.get());
    }

    // Arrows lost their knockback field in 1.21 (knockback now only comes from enchantments of the firing weapon),
    // so the throwing axe keeps its own value and applies it like 1.20.1 did.
    private int knockback;

    public void setKnockback(int knockback) {
        this.knockback = knockback;
    }

    @Override
    protected void doKnockback(LivingEntity entity, DamageSource damageSource) {
        super.doKnockback(entity, damageSource);
        if (this.knockback > 0) {
            double resistance = Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            Vec3 push = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(this.knockback * 0.6 * resistance);
            if (push.lengthSqr() > 0.0) {
                entity.push(push.x, 0.1, push.z);
            }
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setHealth(entity.getHealth() - 1);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        // Store HP before the hit
        //float hpBefore = (pResult.getEntity() instanceof LivingEntity living) ? living.getHealth() : 0f;

        // Apply damage normally
        super.onHitEntity(pResult);

        /*
        // Calculate actual damage and print to chat
        if (!this.level.isClientSide() && pResult.getEntity() instanceof LivingEntity target
                && this.getOwner() instanceof Player shooter) {
            float hpAfter = target.getHealth();
            float actualDamage = hpBefore - hpAfter;
            shooter.sendSystemMessage(Component.literal(
                    "[DEBUG] Wurfaxt Schaden: " + String.format("%.2f", actualDamage)
                    + " HP (" + target.getName().getString() + ")"));
        }
         */

        // Hit sound
        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        // 10% chance to break on entity hit
        boolean broken = this.random.nextFloat() < 0.10f;
        dropAndDiscard(broken);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        // Don't stick in the ground – drop as a floating item instead
        super.onHitBlock(pResult);
        // 25% chance to break on block hit
        boolean broken = this.random.nextFloat() < 0.25f;
        dropAndDiscard(broken);
    }

    /** Spawns the axe as an ItemEntity and removes the projectile */
    private void dropAndDiscard(boolean broken) {
        if (this.level().isClientSide()) {
            return;
        }
        ItemStack drop = broken
                ? new ItemStack(ModItems.THROWING_AXE_BROKEN.get())
                : new ItemStack(ModItems.THROWING_AXE.get());

        if (broken) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
        }

        ItemEntity itemEntity = new ItemEntity(
                this.level(), this.getX(), this.getY(), this.getZ(), drop
        );
        itemEntity.setDefaultPickUpDelay();
        itemEntity.lifespan = Integer.MAX_VALUE;  // never despawn
        this.level().addFreshEntity(itemEntity);
        this.discard();
    }

    // Rotation angle for the renderer – freezes on impact
    public float rotation = 0F;
    public float lastRotation = 0F;

    @Override
    public void tick() {
        lastRotation = rotation;
        super.tick();
        // Only spin while the axe is in flight, not when stuck in the ground
        if (!inGround) {
            rotation += 20.0F;
        }
    }
}
