package net.petemc.knightarmorweapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.entity.ThrowableAxeEntity;
import net.petemc.knightarmorweapons.items.ModItems;

public class ThrowableAxeRenderer extends EntityRenderer<ThrowableAxeEntity> {

    private final ItemRenderer itemRenderer;

    public ThrowableAxeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(ThrowableAxeEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // Scale
        poseStack.scale(1.0F, 1.0F, 1.0F);

        // 1. Rotate horizontally to face flight direction (Yaw)
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getYRot() + 180.0F));

        // 2. Tilt vertically (Pitch)
        poseStack.mulPose(Axis.XP.rotationDegrees(-entity.getXRot()));

        // 3. Forward tumble of the axe (freezes on impact)
        float angle = Mth.lerp(partialTick, entity.lastRotation, entity.rotation);
        poseStack.mulPose(Axis.XP.rotationDegrees(angle));

        // Render item with NONE transform (no JSON transform = clean XY-plane, upright)
        ItemStack stack = new ItemStack(ModItems.THROWING_AXE.get());
        this.itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.NONE,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                entity.level(),
                entity.getId()
        );

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrowableAxeEntity entity) {
        // Not used directly – ItemRenderer handles the texture
        return ResourceLocation.fromNamespaceAndPath("minecraft", "textures/misc/white.png");
    }
}

