package net.petemc.knightarmorweapons.items;

import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KnightArmorWeapons.MOD_ID);

    public static final RegistryObject<Item> KNIGHT_HELMET = ITEMS.register("knight_helmet", KnightArmor.Helmet::new);
    public static final RegistryObject<Item> KNIGHT_CHESTPLATE = ITEMS.register("knight_chestplate", KnightArmor.Chestplate::new);
    public static final RegistryObject<Item> KNIGHT_LEGGINGS = ITEMS.register("knight_leggings", KnightArmor.Leggings::new);
    public static final RegistryObject<Item> KNIGHT_BOOTS = ITEMS.register("knight_boots", KnightArmor.Boots::new);

    public static final RegistryObject<Item> KNIGHT_LONG_SWORD = ITEMS.register("knight_long_sword", KnightLongSword::new);
    public static final RegistryObject<Item> SPIKED_MACE = ITEMS.register("knight_spiked_mace", SpikedMace::new);
    public static final RegistryObject<Item> KNIGHT_WAR_SICKLE = ITEMS.register("knight_war_sickle", HandScythe::new);
    public static final RegistryObject<Item> THROWING_AXE = ITEMS.register("knight_throwing_axe", ThrowingAxe::new);
    public static final RegistryObject<Item> THROWING_AXE_BROKEN = ITEMS.register("knight_throwing_axe_broken", ThrowingAxeBroken::new);

    public static final RegistryObject<Item> KNIGHT_LONG_SWORD_HANDLE = ITEMS.register("knight_long_sword_handle",
            () -> new Item(new Item.Properties().tab(KnightArmorWeapons.TAB).stacksTo(1)));
    public static final RegistryObject<Item> KNIGHT_LONG_SWORD_BLADE = ITEMS.register("knight_long_sword_blade",
            () -> new Item(new Item.Properties().tab(KnightArmorWeapons.TAB).stacksTo(1)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
