package net.petemc.knightarmorweapons.mixin;

import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import net.minecraft.world.level.Level;
import net.petemc.knightarmorweapons.items.WeaponRepair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Two knight weapons can not be combined in the crafting grid
 * (Forge: RepairItemRecipe.matches() checks isRepairable()).
 */
@Mixin(RepairItemRecipe.class)
public abstract class RepairItemRecipeMixin {

    @Inject(method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            at = @At("HEAD"), cancellable = true)
    private void knightmod_blockWeaponRepair(CraftingInput input, Level level, CallbackInfoReturnable<Boolean> cir) {
        for (int i = 0; i < input.size(); i++) {
            if (WeaponRepair.isNonRepairable(input.getItem(i))) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}
