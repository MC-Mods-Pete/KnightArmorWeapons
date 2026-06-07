package net.petemc.knightarmorweapons.items;

import net.minecraft.world.item.Item;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ThrowingAxeBroken extends Item {

    public ThrowingAxeBroken() {
        super(new Properties()
                .tab(KnightArmorWeapons.TAB)
                .stacksTo(8)
        );
    }
}

