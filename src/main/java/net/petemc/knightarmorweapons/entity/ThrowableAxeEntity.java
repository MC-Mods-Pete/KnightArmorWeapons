package net.petemc.knightarmorweapons.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.petemc.knightarmorweapons.items.ModItems;

public class ThrowableAxeEntity extends AbstractArrow {

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
        return new ItemStack(ModItems.THROWING_AXE);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);

        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1.0F, 1.0F);

            boolean broken = this.getRandom().nextFloat() < 0.10f;
            dropAndDiscard(broken);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level().isClientSide()) {
            boolean broken = this.getRandom().nextFloat() < 0.25f;
            dropAndDiscard(broken);
        }
    }

    private void dropAndDiscard(boolean broken) {
        ItemStack drop = broken ? new ItemStack(ModItems.THROWING_AXE_BROKEN) : new ItemStack(ModItems.THROWING_AXE);
        ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), drop);
        itemEntity.setDefaultPickUpDelay();
        itemEntity.age = Integer.MAX_VALUE;
        this.level().addFreshEntity(itemEntity);
        this.discard();
    }

    public float rotation = 0F;
    public float lastRotation = 0F;

    @Override
    public void tick() {
        lastRotation = rotation;
        super.tick();
        if (!inGround) {
            rotation += 20.0F;
        }
    }

    public static ThrowableAxeEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        ThrowableAxeEntity entityarrow = new ThrowableAxeEntity(ModEntities.THROWABLE_AXE, entity, world);
        var look = entity.getRotationVector();
        entityarrow.shoot(look.x, look.y, look.z, power * 2.0F, 0.0F);
        entityarrow.setSilent(true);
        entityarrow.setShotFromCrossbow(false);
        entityarrow.setBaseDamage(damage);
        entityarrow.setKnockback(knockback);
        world.addFreshEntity(entityarrow);
        world.playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.BLOCK_METAL_HIT,
                SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + power / 2.0F);
        return entityarrow;
    }

    public static ThrowableAxeEntity shoot(LivingEntity entity, LivingEntity target) {
        ThrowableAxeEntity entityarrow = new ThrowableAxeEntity(ModEntities.THROWABLE_AXE, entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + (double) target.getBbHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2, dz, 2.5F, 12.0F);
        entityarrow.setSilent(true);
        entityarrow.setShotFromCrossbow(false);
        entityarrow.setBaseDamage(8.0);
        entityarrow.setKnockback(2);
        return entityarrow;
    }
}
