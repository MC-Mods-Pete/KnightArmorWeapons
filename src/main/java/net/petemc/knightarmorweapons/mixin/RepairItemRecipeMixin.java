package net.petemc.knightarmorweapons.mixin;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.recipe.RepairItemRecipe;
import net.minecraft.world.World;
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

    @Inject(method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z",
            at = @At("HEAD"), cancellable = true)
    private void knightmod_blockWeaponRepair(RecipeInputInventory inventory, World world,
                                             CallbackInfoReturnable<Boolean> cir) {
        for (int i = 0; i < inventory.size(); i++) {
            if (WeaponRepair.isNonRepairable(inventory.getStack(i))) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}
