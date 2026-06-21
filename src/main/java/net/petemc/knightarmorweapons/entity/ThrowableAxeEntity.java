package net.petemc.knightarmorweapons.entity;

import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundSource;
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