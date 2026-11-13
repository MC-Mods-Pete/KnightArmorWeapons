package net.petemc.knightarmorweapons.items;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KnightArmorWeapons.MOD_ID);

    public static final DeferredItem<Item> KNIGHT_HELMET = ITEMS.register("knight_helmet", KnightArmor.Helmet::new);
    public static final DeferredItem<Item> KNIGHT_CHESTPLATE = ITEMS.register("knight_chestplate", KnightArmor.Chestplate::new);
    public static final DeferredItem<Item> KNIGHT_LEGGINGS = ITEMS.register("knight_leggings", KnightArmor.Leggings::new);
    public static final DeferredItem<Item> KNIGHT_BOOTS = ITEMS.register("knight_boots", KnightArmor.Boots::new);

    public static final DeferredItem<Item> KNIGHT_LONG_SWORD = ITEMS.register("knight_long_sword", KnightLongSword::new);
    public static final DeferredItem<Item> SPIKED_MACE = ITEMS.register("knight_spiked_mace", SpikedMace::new);
    public static final DeferredItem<Item> KNIGHT_WAR_SICKLE = ITEMS.register("knight_war_sickle", HandScythe::new);
    public static final DeferredItem<Item> THROWING_AXE = ITEMS.register("knight_throwing_axe", ThrowingAxe::new);
    public static final DeferredItem<Item> THROWING_AXE_BROKEN = ITEMS.register("knight_throwing_axe_broken", ThrowingAxeBroken::new);

    public static final DeferredItem<Item> KNIGHT_LONG_SWORD_HANDLE = ITEMS.register("knight_long_sword_handle",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KNIGHT_LONG_SWORD_BLADE = ITEMS.register("knight_long_sword_blade",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
