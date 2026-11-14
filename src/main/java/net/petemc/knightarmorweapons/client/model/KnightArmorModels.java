package net.petemc.knightarmorweapons.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Client-only: the baked Knight armor parts, grouped per armor slot under the humanoid part names
 * (head, body, left_arm, right_arm, left_leg, right_leg). Baked again on every resource reload (EntityRenderersEvent.AddLayers).
 */
public final class KnightArmorModels {
    private final Map<EquipmentSlot, ModelPart> roots = new EnumMap<>(EquipmentSlot.class);

    public KnightArmorModels(EntityModelSet entityModels) {
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            // Every slot bakes its own copy, so no ModelPart is ever shared between two slots
            KnightArmorModel knight = new KnightArmorModel(entityModels.bakeLayer(KnightArmorModel.LAYER_LOCATION));
            Map<String, ModelPart> parts = new LinkedHashMap<>();
            switch (slot) {
                case HEAD -> parts.put("head", knight.helmet);
                case CHEST -> {
                    parts.put("body", knight.chestplate);
                    parts.put("left_arm", knight.left_arm);
                    parts.put("right_arm", knight.right_arm);
                }
                case LEGS -> {
                    parts.put("left_leg", knight.leggingsleft);
                    parts.put("right_leg", knight.leggingsright);
                }
                case FEET -> {
                    parts.put("left_leg", knight.left_leg);
                    parts.put("right_leg", knight.right_leg);
                }
                default -> {
                }
            }
            this.roots.put(slot, new ModelPart(Collections.emptyList(), parts));
        }
    }

    /**
     * A model for one submit: takes the pose from the wearer's model when it is rendered (the submit is deferred,
     * so the shared wearer model is posed again for this entity, like Fabric's TransformCopyingModel does).
     */
    public Piece create(EquipmentSlot slot, HumanoidModel<HumanoidRenderState> wearer, boolean babyProportions) {
        return new Piece(this.roots.get(slot), wearer, babyProportions);
    }

    public static final class Piece extends Model<HumanoidRenderState> {
        private static final Function<Identifier, RenderType> RENDER_TYPE = RenderTypes::armorCutoutNoCull;

        private final HumanoidModel<HumanoidRenderState> wearer;
        private final boolean babyProportions;

        private Piece(ModelPart root, HumanoidModel<HumanoidRenderState> wearer, boolean babyProportions) {
            super(root, RENDER_TYPE);
            this.wearer = wearer;
            this.babyProportions = babyProportions;
        }

        @Override
        public void setupAnim(HumanoidRenderState state) {
            this.resetPose();
            this.wearer.setupAnim(state);
            copy(this.wearer.head, "head", 0.75F, 0.75F, 0.75F, 0.0F);
            // Baby body pivot is in the middle of the body, the adult one (and the Knight chestplate) at the top
            copy(this.wearer.body, "body", 0.5F, 5.0F / 12.0F, 0.5F, -2.5F);
            copy(this.wearer.leftArm, "left_arm", 0.5F, 5.0F / 12.0F, 0.5F, 0.0F);
            copy(this.wearer.rightArm, "right_arm", 0.5F, 5.0F / 12.0F, 0.5F, 0.0F);
            copy(this.wearer.leftLeg, "left_leg", 0.5F, 4.0F / 12.0F, 0.5F, 0.0F);
            copy(this.wearer.rightLeg, "right_leg", 0.5F, 4.0F / 12.0F, 0.5F, 0.0F);
        }

        /**
         * Copies the pose of the wearer's part. Since 26.1 baby humanoids (zombie, husk, drowned, ...) have their own
         * smaller model with different proportions (head 6 instead of 8 pixels, body 4x5 instead of 8x12, ...), so the
         * adult-sized Knight parts are scaled per body part to fit (baby scale factors and body offset).
         */
        private void copy(ModelPart from, String name, float babyX, float babyY, float babyZ, float babyOffsetY) {
            if (!this.root.hasChild(name)) {
                return;
            }
            ModelPart to = this.root.getChild(name);
            to.x = from.x;
            to.y = from.y;
            to.z = from.z;
            to.xRot = from.xRot;
            to.yRot = from.yRot;
            to.zRot = from.zRot;
            to.xScale = from.xScale;
            to.yScale = from.yScale;
            to.zScale = from.zScale;
            to.visible = from.visible;
            if (this.babyProportions) {
                to.xScale *= babyX;
                to.yScale *= babyY;
                to.zScale *= babyZ;
                to.y += babyOffsetY;
            }
        }
    }
}
