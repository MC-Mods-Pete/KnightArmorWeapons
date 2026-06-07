package net.petemc.knightarmorweapons.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.petemc.knightarmorweapons.client.renderer.ThrowableAxeRenderer;
import net.petemc.knightarmorweapons.entity.ModEntities;

@EventBusSubscriber(bus = Bus.MOD, value = {Dist.CLIENT})
public class ModEvents {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THROWABLE_AXE.get(), ThrowableAxeRenderer::new);
    }
}
