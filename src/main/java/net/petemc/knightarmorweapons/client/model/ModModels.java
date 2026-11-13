package net.petemc.knightarmorweapons.client.model;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

// bus = MOD stays on purpose: older NeoForge 21.1 builds don't detect the bus automatically
@SuppressWarnings("removal")
@EventBusSubscriber(modid = KnightArmorWeapons.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModModels {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(KnightArmorModel.LAYER_LOCATION, KnightArmorModel::createBodyLayer);
    }
}
