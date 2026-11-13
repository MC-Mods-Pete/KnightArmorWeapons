package net.petemc.knightarmorweapons.items;

import net.minecraft.world.item.ItemStack;

/**
 * Forge marks the knight longsword, spiked mace and hand scythe with isRepairable() = false.
 * Forge uses that flag only in the crafting grid repair recipe and in the grindstone:
 * two of these weapons can not be combined there. Anvil repair still works.
 * Fabric has no such flag, the mixins RepairItemRecipeMixin and GrindstoneMenuMixin
 * use this check instead.
 */
public final class WeaponRepair {
    private WeaponRepair() {}

    public static boolean isNonRepairable(ItemStack stack) {
        return stack.getItem() instanceof KnightLongSword
                || stack.getItem() instanceof SpikedMace
                || stack.getItem() instanceof HandScythe;
    }
}
