package net.petemc.knightarmorweapons.client.model;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

@EventBusSubscriber(modid = KnightArmorWeapons.MOD_ID, value = Dist.CLIENT)
public class ModModels {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(KnightArmorModel.LAYER_LOCATION, KnightArmorModel::createBodyLayer);
    }
}
