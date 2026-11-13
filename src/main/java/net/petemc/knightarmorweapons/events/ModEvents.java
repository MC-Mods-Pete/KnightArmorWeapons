package net.petemc.knightarmorweapons.events;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.client.model.KnightArmorModels;
import net.petemc.knightarmorweapons.client.renderer.ThrowableAxeRenderer;
import net.petemc.knightarmorweapons.entity.ModEntities;
import net.petemc.knightarmorweapons.items.ModItems;

// bus = MOD stays on purpose: older NeoForge 21.1 builds don't detect the bus automatically
@SuppressWarnings("removal")
@EventBusSubscriber(modid = KnightArmorWeapons.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEvents {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THROWABLE_AXE.get(), ThrowableAxeRenderer::new);
    }

    // Replaces Item.initializeClient (removed in NeoForge 1.21): the 3D armor model for all four Knight armor pieces
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> defaultModel) {
                return KnightArmorModels.get(slot, living, defaultModel);
            }
        }, ModItems.KNIGHT_HELMET.get(), ModItems.KNIGHT_CHESTPLATE.get(), ModItems.KNIGHT_LEGGINGS.get(), ModItems.KNIGHT_BOOTS.get());
    }
}
