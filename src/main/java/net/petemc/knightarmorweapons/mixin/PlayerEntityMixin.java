package net.petemc.knightarmorweapons.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.petemc.knightarmorweapons.items.KnightLongSword;
import net.petemc.knightarmorweapons.items.SpikedMace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Disables the sweep attack for the Knight Longsword and the Spiked Mace
 * (Forge: canPerformAction(SWORD_SWEEP) returns false).
 *
 * PlayerEntity.attack() only sweeps if getStackInHand(MAIN_HAND).getItem() is a SwordItem.
 * That getItem() call is the only one in attack() and its result is used solely for this
 * instanceof check, so returning AIR for our single-target weapons disables the sweep.
 *
 * Note: an @At("CONSTANT") with classValue=... does not work here, because the class name
 * in args is not remapped to intermediary and the mixin fails in production.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @WrapOperation(
        method = "attack(Lnet/minecraft/entity/Entity;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;")
    )
    private Item knightmod_disableSweep(ItemStack stack, Operation<Item> original) {
        Item item = original.call(stack);
        if (item instanceof KnightLongSword || item instanceof SpikedMace) return Items.AIR;
        return item;
    }
}
