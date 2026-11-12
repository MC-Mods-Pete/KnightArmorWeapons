package net.petemc.knightarmorweapons.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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

    @ModifyExpressionValue(
        method = "attack(Lnet/minecraft/entity/Entity;)V",
        at = @At(value = "CONSTANT", args = "classValue=net/minecraft/item/SwordItem")
    )
    private boolean knightmod_disableSweep(boolean isSword) {
        Item held = ((PlayerEntity) (Object) this).getMainHandStack().getItem();
        return isSword && !(held instanceof KnightLongSword) && !(held instanceof SpikedMace);
    }
}
