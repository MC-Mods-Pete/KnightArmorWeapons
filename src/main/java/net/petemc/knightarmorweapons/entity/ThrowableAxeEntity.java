package net.petemc.knightarmorweapons.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity; // Korrigiert für Yarn
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.petemc.knightarmorweapons.items.ModItems;

public class ThrowableAxeEntity extends PersistentProjectileEntity { // Korrigiert für Yarn

    public ThrowableAxeEntity(EntityType<? extends ThrowableAxeEntity> type, World world) {
        super(type, world);
    }

    public ThrowableAxeEntity(EntityType<? extends ThrowableAxeEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    public ThrowableAxeEntity(EntityType<? extends ThrowableAxeEntity> type, LivingEntity entity, World world) {
        super(type, entity, world);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(ModItems.THROWING_AXE);
    }

    @Override
    protected void onEntityHit(EntityHitResult pResult) { // In Yarn oft onEntityHit statt onHitEntity
        super.onEntityHit(pResult);
        if (!this.getWorld().isClient) {
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1.0F, 1.0F);

            boolean broken = this.random.nextFloat() < 0.10f;
            dropAndDiscard(broken);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult pResult) { // In Yarn oft onBlockHit statt onHitBlock
        super.onBlockHit(pResult);
        if (!this.getWorld().isClient) {
            boolean broken = this.random.nextFloat() < 0.25f;
            dropAndDiscard(broken);
        }
    }

    private void dropAndDiscard(boolean broken) {
        ItemStack drop = broken ? new ItemStack(ModItems.THROWING_AXE_BROKEN) : new ItemStack(ModItems.THROWING_AXE);
        ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), drop);

        itemEntity.age = Integer.MAX_VALUE;

        this.getWorld().spawnEntity(itemEntity);
        this.discard();
    }

    public float rotation = 0F;
    public float lastRotation = 0F;

    @Override
    public void tick() {
        lastRotation = rotation;
        super.tick();
        if (!this.inGround) {
            rotation += 20.0F;
        }
    }

    public static ThrowableAxeEntity setVelocity(World world, LivingEntity entity, Random random, float power, double damage, int knockback) {
        ThrowableAxeEntity entityarrow = new ThrowableAxeEntity(ModEntities.THROWABLE_AXE, entity, world);

        Vec3d look = entity.getRotationVec(1.0F);
        entityarrow.setVelocity(look.x, look.y, look.z, power * 2.0F, 0.0F);
        entityarrow.setSilent(true);
        entityarrow.setShotFromCrossbow(false);
        entityarrow.setDamage(damage);
        entityarrow.setPunch(knockback);

        world.spawnEntity(entityarrow);

        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.BLOCK_METAL_HIT,
                SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + power / 2.0F);

        return entityarrow;
    }

    public static ThrowableAxeEntity setVelocity(LivingEntity entity, LivingEntity target) {
        ThrowableAxeEntity entityarrow = new ThrowableAxeEntity(ModEntities.THROWABLE_AXE, entity, entity.getWorld());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + (double) target.getHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityarrow.setVelocity(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2, dz, 2.5F, 12.0F);
        entityarrow.setSilent(true);
        entityarrow.setDamage(16.0);
        entityarrow.setPunch(2);
        entityarrow.setShotFromCrossbow(false);
        entity.getWorld().spawnEntity(entityarrow);
        entity.getWorld().playSound((PlayerEntity) null, entity.getX(), entity.getY(), entity.getZ(),
                (SoundEvent) Registries.SOUND_EVENT.get(Identifier.tryParse("block.metal.hit")),
                SoundCategory.PLAYERS, 1.0F, 1.0F / (Random.create().nextFloat() * 0.5F + 1.0F));
        return entityarrow;
    }
}