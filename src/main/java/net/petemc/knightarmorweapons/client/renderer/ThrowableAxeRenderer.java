package net.petemc.knightarmorweapons.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.petemc.knightarmorweapons.entity.ThrowableAxeEntity;
import net.petemc.knightarmorweapons.items.ModItems;

/** Since 1.21.2 entity renderers work with render states (extracted every frame, then submitted). */
public class ThrowableAxeRenderer extends EntityRenderer<ThrowableAxeEntity, ThrowableAxeRenderer.State> {

    public static class State extends EntityRenderState {
        public final ItemStackRenderState item = new ItemStackRenderState();
        public float yRot;
        public float xRot;
        public float spin;
    }

    private final ItemModelResolver itemModelResolver;
    // Created lazily: renderers are built before item components are bound (ItemStack in the constructor fails since 26.1)
    private ItemStack stack;

    public ThrowableAxeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
        this.shadowRadius = 0.0F;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ThrowableAxeEntity entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.yRot = entity.getYRot(partialTick);
        state.xRot = entity.getXRot(partialTick);
        state.spin = Mth.lerp(partialTick, entity.lastRotation, entity.rotation);
        // Item model with NONE transform (no JSON transform = clean XY-plane, upright), like before
        if (this.stack == null) {
            this.stack = new ItemStack(ModItems.THROWING_AXE);
        }
        this.itemModelResolver.updateForNonLiving(state.item, this.stack, ItemDisplayContext.NONE, entity);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        // 1. Rotate horizontally to face flight direction (Yaw)
        poseStack.rotateDegrees(Axis.YP, state.yRot + 180.0F);

        // 2. Tilt vertically (Pitch)
        poseStack.rotateDegrees(Axis.XP, -state.xRot);

        // 3. Forward tumble of the axe (freezes on impact)
        poseStack.rotateDegrees(Axis.XP, state.spin);

        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
