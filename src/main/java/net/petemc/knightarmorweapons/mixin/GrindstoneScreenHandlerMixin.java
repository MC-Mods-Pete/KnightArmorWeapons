package net.petemc.knightarmorweapons.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.petemc.knightarmorweapons.items.WeaponRepair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Two knight weapons in the grindstone give no result (Forge: GrindstoneMenu.createResult()
 * clears the result for two non-repairable items). A single weapon can still be disenchanted.
 */
@Mixin(GrindstoneScreenHandler.class)
public abstract class GrindstoneScreenHandlerMixin {

    @Shadow @Final Inventory input;
    @Shadow @Final private Inventory result;

    @Inject(method = "updateResult", at = @At("TAIL"))
    private void knightmod_blockWeaponRepair(CallbackInfo ci) {
        ItemStack first = this.input.getStack(0);
        ItemStack second = this.input.getStack(1);
        if (!first.isEmpty() && !second.isEmpty()
                && (WeaponRepair.isNonRepairable(first) || WeaponRepair.isNonRepairable(second))) {
            this.result.setStack(0, ItemStack.EMPTY);
        }
    }
}
