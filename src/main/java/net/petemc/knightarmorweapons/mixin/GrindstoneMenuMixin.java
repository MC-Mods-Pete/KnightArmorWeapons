package net.petemc.knightarmorweapons.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
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
@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin {

    @Shadow @Final Container repairSlots;
    @Shadow @Final private Container resultSlots;

    @Inject(method = "createResult", at = @At("TAIL"))
    private void knightmod_blockWeaponRepair(CallbackInfo ci) {
        ItemStack first = this.repairSlots.getItem(0);
        ItemStack second = this.repairSlots.getItem(1);
        if (!first.isEmpty() && !second.isEmpty()
                && (WeaponRepair.isNonRepairable(first) || WeaponRepair.isNonRepairable(second))) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
        }
    }
}
