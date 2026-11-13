package net.petemc.knightarmorweapons.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import net.petemc.knightarmorweapons.items.ModItems;

public class ThrowableAxeEntity extends AbstractArrow {

    public ThrowableAxeEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(ModEntities.THROWABLE_AXE.get(), world);
    }

    public ThrowableAxeEntity(EntityType<? extends ThrowableAxeEntity> type, Level world) {
        super(type, world);
    }

    public ThrowableAxeEntity(EntityType<? extends ThrowableAxeEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public ThrowableAxeEntity(EntityType<? extends ThrowableAxeEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    @Override
    public ItemStack getPickupItem() {
        return new ItemStack(ModItems.THROWING_AXE.get());
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
        if (!this.level().isClientSide()) {
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

    public static ThrowableAxeEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        ThrowableAxeEntity entityarrow = new ThrowableAxeEntity(ModEntities.THROWABLE_AXE.get(), entity, world);
        var look = entity.getViewVector(1.0F);
        entityarrow.shoot(look.x, look.y, look.z, power * 2.0F, 0.0F);
        entityarrow.setSilent(true);
        entityarrow.setShotFromCrossbow(false);
        entityarrow.setBaseDamage(damage);
        entityarrow.setKnockback(knockback);
        world.addFreshEntity(entityarrow);
        world.playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(),
                (SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.metal.hit")),
                SoundSource.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + power / 2.0F);
        return entityarrow;
    }

    public static ThrowableAxeEntity shoot(LivingEntity entity, LivingEntity target) {
        ThrowableAxeEntity entityarrow = new ThrowableAxeEntity(ModEntities.THROWABLE_AXE.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + (double) target.getBbHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2, dz, 2.5F, 12.0F);
        entityarrow.setSilent(true);
        entityarrow.setBaseDamage(16.0);
        entityarrow.setKnockback(2);
        entityarrow.setShotFromCrossbow(false);
        entity.level().addFreshEntity(entityarrow);
        entity.level().playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(),
                (SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.metal.hit")),
                SoundSource.PLAYERS, 1.0F, 1.0F / (RandomSource.create().nextFloat() * 0.5F + 1.0F));
        return entityarrow;
    }
}
