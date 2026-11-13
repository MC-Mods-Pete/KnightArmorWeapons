package net.petemc.knightarmorweapons.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.petemc.knightarmorweapons.items.KnightLongSword;
import net.petemc.knightarmorweapons.items.SpikedMace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Disables the sweep attack for the Knight Longsword and the Spiked Mace
 * (Forge: canPerformAction(SWORD_SWEEP) returns false).
 *
 * Player.attack() only sweeps if the main hand item is a SwordItem. That check is the second
 * ItemStack.getItem() call in attack() (the first one is getAttackDamageBonus), so returning AIR
 * there disables the sweep for our single-target weapons.
 *
 * Note: an @At("CONSTANT") with classValue=... does not work here, because the class name
 * in args is not remapped to intermediary and the mixin fails in production.
 */
@Mixin(Player.class)
public abstract class PlayerMixin {

    @WrapOperation(
        method = "attack(Lnet/minecraft/world/entity/Entity;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;", ordinal = 1)
    )
    private Item knightmod_disableSweep(ItemStack stack, Operation<Item> original) {
        Item item = original.call(stack);
        if (item instanceof KnightLongSword || item instanceof SpikedMace) return Items.AIR;
        return item;
    }
}
