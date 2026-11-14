package net.petemc.knightarmorweapons.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.petemc.knightarmorweapons.KnightArmorWeapons;

/**
 * Spiked Mace — a heavy, short-range armor-crusher.
 *
 * Design: Terrible vs. fast unarmored hordes (slow, single-target),
 *         but obliterates shields and heavy armor.
 *
 * Stats:
 *   Attack Damage : 9.0F  (= 1 base + 7 tier bonus + 1 modifier)
 *   Attack Speed  : 0.8F  (modifier = 0.8 - 4.0 = -3.2F, slower than any axe)
 *   Attack Range  : -0.3 block reach penalty (minimal, enough to space vs. fast mobs)
 *   Sweep Attack  : DISABLED (single-target only): not in the item tag minecraft:swords, see KnightLongSword
 *
 * Unique mechanics handled in SpikedMaceCombatEvents:
 *   A) Shield Obliteration — 100% guaranteed shield disable + 50% crushing damage
 *   B) Armor Sunder        — +2% damage per armor point (max +40% at 20 pts)
 */
public class SpikedMace extends Item {

    // Same values as the older versions: 350 uses, mining speed 6, +7 damage, iron mining level, enchantability 15
    static final ToolMaterial MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_IRON_TOOL, 350, 6.0F, 7.0F, 15, KnightLongSword.REPAIRS_KNIGHT_WEAPONS);

    public SpikedMace(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    /**
     * 1 (player) + 1 + 7 = 9.0 damage, speed 4.0 - 3.2 = 0.8. Minimal reach penalty -0.3 blocks (was -1.0, reduced after
     * playtest), appended whenever the attributes are set (see KnightLongSword.properties()).
     */
    static Item.Properties properties() {
        return new Item.Properties() {
            @Override
            public Item.Properties attributes(ItemAttributeModifiers attributes) {
                return super.attributes(attributes.withModifierAdded(
                        Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(KnightArmorWeapons.id("mace_reach"), -0.3, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND));
            }
        }.sword(MATERIAL, 1.0F, -3.2F);
    }

    // C) DAZE / CONCUSSION: Slowness III for 30 ticks after every hit
    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(
                MobEffects.SLOWNESS,
                30,    // duration in ticks (1.5 s)
                2,     // amplifier 2 = Slowness III (0-indexed)
                false, // not ambient (shows particles)
                true   // show icon in HUD
        ));
        super.hurtEnemy(stack, target, attacker);
    }
}
