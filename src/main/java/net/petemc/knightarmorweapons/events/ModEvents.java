package net.petemc.knightarmorweapons.events;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.client.KnightArmorClientExtensions;
import net.petemc.knightarmorweapons.client.renderer.ThrowableAxeRenderer;
import net.petemc.knightarmorweapons.entity.ModEntities;
import net.petemc.knightarmorweapons.items.ModItems;

/** Client mod-bus events. The flat inventory icons need no code: the item model definitions (assets/.../items/*.json) pick the "_gui" model. */
@EventBusSubscriber(modid = KnightArmorWeapons.MOD_ID, value = Dist.CLIENT)
public class ModEvents {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THROWABLE_AXE.get(), ThrowableAxeRenderer::new);
    }

    // Fired on every resource reload after the model layers were baked
    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        KnightArmorClientExtensions.bakeModels(event.getEntityModels());
    }

    // The 3D armor model and texture for all four Knight armor pieces
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(KnightArmorClientExtensions.INSTANCE,
                ModItems.KNIGHT_HELMET.get(), ModItems.KNIGHT_CHESTPLATE.get(), ModItems.KNIGHT_LEGGINGS.get(), ModItems.KNIGHT_BOOTS.get());
    }
}
