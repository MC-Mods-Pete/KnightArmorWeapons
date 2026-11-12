package net.petemc.knightarmorweapons.items;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.petemc.knightarmorweapons.KnightArmorWeapons;
import net.petemc.knightarmorweapons.armor.ModArmorMaterials;
import net.petemc.knightarmorweapons.client.model.KnightArmorModel;
import org.jetbrains.annotations.NotNull;

public abstract class KnightArmor extends ArmorItem {
    public KnightArmor(EquipmentSlot pSlot, Properties pProperties) {
        super(ModArmorMaterials.KNIGHT, pSlot, pProperties);
    }

    public static class Helmet extends KnightArmor {
        public Helmet() {
            super(EquipmentSlot.HEAD, (new Item.Properties()).tab(KnightArmorWeapons.TAB));
        }

        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                public @NotNull HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
                    HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(), 
                            Map.of( "head", (new KnightArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(KnightArmorModel.LAYER_LOCATION))).helmet,
                                    "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                    armorModel.crouching = living.isShiftKeyDown();
                    armorModel.riding = defaultModel.riding;
                    armorModel.young = living.isBaby();
                    return armorModel;
                }
            });
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "knightarmorweapons:textures/entities/knight_armor.png";
        }
    }

    public static class Chestplate extends KnightArmor {
        public Chestplate() {
            super(EquipmentSlot.CHEST, (new Item.Properties()).tab(KnightArmorWeapons.TAB));
        }

        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @OnlyIn(Dist.CLIENT)
                public @NotNull HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
                    HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
                            Map.of( "body", (new KnightArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(KnightArmorModel.LAYER_LOCATION))).chestplate,
                                    "left_arm", (new KnightArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(KnightArmorModel.LAYER_LOCATION))).left_arm,
                                    "right_arm", (new KnightArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(KnightArmorModel.LAYER_LOCATION))).right_arm,
                                    "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "right_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "left_leg", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                    armorModel.crouching = living.isShiftKeyDown();
                    armorModel.riding = defaultModel.riding;
                    armorModel.young = living.isBaby();
                    return armorModel;
                }
            });
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "knightarmorweapons:textures/entities/knight_armor.png";
        }
    }

    public static class Leggings extends KnightArmor {
        public Leggings() {
            super(EquipmentSlot.LEGS, (new Item.Properties()).tab(KnightArmorWeapons.TAB));
        }

        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @OnlyIn(Dist.CLIENT)
                public @NotNull HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
                    HumanoidModel armorModel = new HumanoidModel(new ModelPart(Collections.emptyList(),
                            Map.of( "left_leg", (new KnightArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(KnightArmorModel.LAYER_LOCATION))).leggingsleft,
                                    "right_leg", (new KnightArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(KnightArmorModel.LAYER_LOCATION))).leggingsright,
                                    "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                    armorModel.crouching = living.isShiftKeyDown();
                    armorModel.riding = defaultModel.riding;
                    armorModel.young = living.isBaby();
                    return armorModel;
                }
            });
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "knightarmorweapons:textures/entities/knight_armor.png";
        }
    }

    public static class Boots extends KnightArmor {
        public Boots() {
            super(EquipmentSlot.FEET, (new Item.Properties()).tab(KnightArmorWeapons.TAB));
        }

        public void initializeClient(Consumer<IClientItemExtensions> consumer) {
            consumer.accept(new IClientItemExtensions() {
                @OnlyIn(Dist.CLIENT)
                public @NotNull HumanoidModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel defaultModel) {
                    HumanoidModel armorModel = new HumanoidModel<>(new ModelPart(Collections.emptyList(),
                            Map.of( "left_leg", (new KnightArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(KnightArmorModel.LAYER_LOCATION))).left_leg,
                                    "right_leg", (new KnightArmorModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(KnightArmorModel.LAYER_LOCATION))).right_leg,
                                    "head", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "hat", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "body", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "right_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()),
                                    "left_arm", new ModelPart(Collections.emptyList(), Collections.emptyMap()))));
                    armorModel.crouching = living.isShiftKeyDown();
                    armorModel.riding = defaultModel.riding;
                    armorModel.young = living.isBaby();
                    return armorModel;
                }
            });
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "knightarmorweapons:textures/entities/knight_armor.png";
        }
    }
}

