package net.petemc.knightarmorweapons.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

/**
 * Entity attack reach of the knight weapons.
 *
 * Forge changes the ENTITY_REACH attribute (base 3.0) per weapon. Vanilla 1.20.1 on Fabric has no
 * such attribute, so the client crosshair targeting (GameRendererMixin) uses these values instead.
 */
public final class WeaponReach {
    /** Vanilla survival entity reach in blocks. */
    public static final double BASE_ENTITY_REACH = 3.0;
    /** Knight Longsword: +1.2 blocks (Forge: "Knight Longsword Extended Reach"). */
    public static final double LONG_SWORD_BONUS = 1.2;
    /** Spiked Mace: -0.3 blocks (Forge: "Spiked Mace Short Reach"). */
    public static final double SPIKED_MACE_BONUS = -0.3;

    private WeaponReach() {}

    /** Entity attack reach of the player for the item held in the main hand. */
    public static double getEntityReach(PlayerEntity player) {
        Item held = player.getMainHandStack().getItem();
        if (held instanceof KnightLongSword) return BASE_ENTITY_REACH + LONG_SWORD_BONUS;
        if (held instanceof SpikedMace) return BASE_ENTITY_REACH + SPIKED_MACE_BONUS;
        return BASE_ENTITY_REACH;
    }
}
