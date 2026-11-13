package net.petemc.knightarmorweapons.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Client-only cache of the Knight armor models (one HumanoidModel per armor slot).
 * <p>
 * Previously the model layer was baked again on every render call, i.e. every frame and several times per piece.
 * Sharing one instance is safe: Forge copies the current pose and part visibility onto the returned model right
 * before rendering it (ForgeHooksClient.copyModelProperties), just like vanilla shares its armor models.
 * <p>
 * Only referenced from client code paths (IClientItemExtensions), never loaded on a dedicated server.
 */
public final class KnightArmorModels {
    private static final String[] HUMANOID_PARTS = {"head", "hat", "body", "right_arm", "left_arm", "right_leg", "left_leg"};
    private static final Map<EquipmentSlot, HumanoidModel<LivingEntity>> CACHE = new EnumMap<>(EquipmentSlot.class);

    private KnightArmorModels() {
    }

    public static HumanoidModel<LivingEntity> get(EquipmentSlot slot, LivingEntity living, HumanoidModel<?> defaultModel) {
        HumanoidModel<LivingEntity> model = CACHE.computeIfAbsent(slot, KnightArmorModels::create);
        model.crouching = living.isShiftKeyDown();
        model.riding = defaultModel.riding;
        model.young = living.isBaby();
        return model;
    }

    private static HumanoidModel<LivingEntity> create(EquipmentSlot slot) {
        // Every slot bakes its own copy, so no ModelPart is ever shared between two models
        KnightArmorModel<LivingEntity> knight = new KnightArmorModel<>(
                Minecraft.getInstance().getEntityModels().bakeLayer(KnightArmorModel.LAYER_LOCATION));

        Map<String, ModelPart> parts = new HashMap<>();
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
        // HumanoidModel requires all humanoid parts to exist; unused ones stay empty
        for (String name : HUMANOID_PARTS) {
            parts.putIfAbsent(name, new ModelPart(Collections.emptyList(), Collections.emptyMap()));
        }
        return new HumanoidModel<>(new ModelPart(Collections.emptyList(), parts));
    }
}
