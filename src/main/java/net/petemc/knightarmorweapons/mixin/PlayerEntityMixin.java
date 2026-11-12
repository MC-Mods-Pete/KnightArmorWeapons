package net.petemc.knightarmorweapons.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.petemc.knightarmorweapons.items.KnightLongSword;
import net.petemc.knightarmorweapons.items.SpikedMace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Disables the sweep attack for the Knight Longsword and the Spiked Mace
 * (Forge: canPerformAction(SWORD_SWEEP) returns false).
 *
 * PlayerEntity.attack() only sweeps if the held item is a SwordItem;
 * this mixin makes that instanceof check return false for our single-target weapons.
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @WrapOperation(
        method = "attack(Lnet/minecraft/entity/Entity;)V",
        at = @At(value = "CONSTANT", args = "classValue=net/minecraft/item/SwordItem")
    )
    private boolean knightmod_disableSweep(Object item, Operation<Boolean> original) {
        if (item instanceof KnightLongSword || item instanceof SpikedMace) return false;
        return original.call(item);
    }
}
