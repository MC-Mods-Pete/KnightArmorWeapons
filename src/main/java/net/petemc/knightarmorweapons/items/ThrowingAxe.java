package net.petemc.knightarmorweapons.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Prediction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.petemc.knightarmorweapons.entity.ModEntities;
import net.petemc.knightarmorweapons.entity.ThrowableAxeEntity;

public class ThrowingAxe extends Item {

    public ThrowingAxe(Item.Properties properties) {
        super(properties.stacksTo(8).attributes(ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 2.0, Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -3.3, Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build()));
    }

    @Override
    public InteractionResult use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        // TRIDENT = the old "raise to throw" animation (SPEAR is the new spear attack animation since 26.1)
        return ItemUseAnimation.TRIDENT;
    }

    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return 72000;
    }

    @Override
    public void hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pAttacker.level().isClientSide() && pAttacker instanceof Player player) {
            if (pAttacker.getRandom().nextFloat() < 0.50f) {
                // 50% chance: axe breaks
                pAttacker.level().playSound(null,
                        pAttacker.getX(), pAttacker.getY(), pAttacker.getZ(),
                        SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);

                if (!player.getAbilities().instabuild) {
                    pStack.shrink(1);
                }
                ItemStack broken = new ItemStack(ModItems.THROWING_AXE_BROKEN);
                if (!player.getInventory().add(broken)) {
                    player.drop(broken, false, Prediction.SERVER_ONLY);
                }
            }
        }
    }

    @Override
    public boolean releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        if (!(pLivingEntity instanceof Player player)) return false;

        int charge = this.getUseDuration(pStack, pLivingEntity) - pTimeLeft;
        if (charge < 10) return false;

        if (!pLevel.isClientSide()) {
            ThrowableAxeEntity axe = new ThrowableAxeEntity(
                    ModEntities.THROWABLE_AXE, player, pLevel);
            axe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.9F, 1.0F);
            // Damage = ceil(velocity * baseDamage): 6.0 * 1.9 = 11.4 -> 12 at launch.
            // Before: 5.5 base (11) + 1 HP via setHealth, which bypassed armor and the normal damage handling.
            axe.setBaseDamage(6.0);
            axe.setKnockback(1);
            axe.setSilent(true);

            axe.pickup = AbstractArrow.Pickup.ALLOWED;
            if (!player.getAbilities().instabuild) {
                pStack.shrink(1);
            }

            pLevel.addFreshEntity(axe);
            pLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        return true;
    }
}
