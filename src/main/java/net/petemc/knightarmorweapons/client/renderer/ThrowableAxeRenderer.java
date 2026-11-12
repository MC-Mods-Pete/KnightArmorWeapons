package net.petemc.knightarmorweapons.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.item.ItemStack;
import net.petemc.knightarmorweapons.entity.ThrowableAxeEntity;
import net.petemc.knightarmorweapons.items.ModItems;

public class ThrowableAxeRenderer extends EntityRenderer<ThrowableAxeEntity> {

    private final ItemRenderer itemRenderer;

    public ThrowableAxeRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(ThrowableAxeEntity entity, float entityYaw, float partialTick,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int packedLight) {
        matrices.push();

        // Scale
        matrices.scale(1.0F, 1.0F, 1.0F);

        // 1. Rotate horizontally to face flight direction (Yaw)
        // Forge: entity.getYRot() -> Fabric: entity.getYaw()
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw() + 180.0F));

        // 2. Tilt vertically (Pitch)
        // Forge: entity.getXRot() -> Fabric: entity.getPitch()
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-entity.getPitch()));

        // 3. Forward tumble of the axe (freezes on impact)
        // Forge: Mth.lerp -> Fabric: MathHelper.lerp
        float angle = MathHelper.lerp(partialTick, entity.lastRotation, entity.rotation);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(angle));

        // Render item with NONE transform (no JSON transform = clean XY-plane, upright)
        ItemStack stack = new ItemStack(ModItems.THROWING_AXE);

        // Forge: ItemDisplayContext.NONE -> Fabric: ModelTransformationMode.NONE
        // Forge: entity.level() -> Fabric: entity.getWorld()
        this.itemRenderer.renderItem(
                stack,
                ModelTransformationMode.NONE,
                packedLight,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                entity.getId()
        );

        matrices.pop();
        super.render(entity, entityYaw, partialTick, matrices, vertexConsumers, packedLight);
    }

    @Override
    public Identifier getTexture(ThrowableAxeEntity entity) {
        // Forge: ResourceLocation -> Fabric: Identifier
        return new Identifier("minecraft", "textures/misc/white.png");
    }
}