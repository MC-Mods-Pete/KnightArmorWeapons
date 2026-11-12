package net.petemc.knightarmorweapons.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.petemc.knightarmorweapons.entity.ModEntities;
import net.petemc.knightarmorweapons.entity.ThrowableAxeEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ThrowingAxe extends Item {

    // Standard attack-modifier UUIDs (same as vanilla weapons)
    private static final UUID ATTACK_DAMAGE_UUID =
            UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ATTACK_SPEED_UUID =
            UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    public ThrowingAxe() {
        super(new Item.Settings().maxCount(8));
    }

    // ── Attribute modifiers: attack damage +2, attack speed -3.3 ──────────────

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder =
                    ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    new EntityAttributeModifier(ATTACK_DAMAGE_UUID, "Weapon modifier", 2.0, Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED,
                    new EntityAttributeModifier(ATTACK_SPEED_UUID, "Weapon modifier", -3.3, Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot);
    }

    // ── Start charging (bow-like animation) ───────────────────────────────────

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public UseAction getUseAction(@NotNull ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(@NotNull ItemStack stack) {
        return 72000;
    }

    // ── On melee hit: 50% chance the axe breaks ───────────────────────────────

    @Override
    public boolean postHit(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        if (!attacker.getWorld().isClient && attacker instanceof PlayerEntity player) {
            if (attacker.getRandom().nextFloat() < 0.50f) {
                attacker.getWorld().playSound(null,
                        attacker.getX(), attacker.getY(), attacker.getZ(),
                        SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);

                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
                ItemStack broken = new ItemStack(ModItems.THROWING_AXE_BROKEN);
                if (!player.getInventory().insertStack(broken)) {
                    player.dropStack(broken);
                }
            }
        }
        return true;
    }

    // ── Release: throw the axe ────────────────────────────────────────────────

    @Override
    public void onStoppedUsing(@NotNull ItemStack stack, @NotNull World world,
                               @NotNull LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) return;

        int charge = this.getMaxUseTime(stack) - remainingUseTicks;
        if (charge < 10) return;

        if (!world.isClient) {
            ThrowableAxeEntity axe = new ThrowableAxeEntity(
                    ModEntities.THROWABLE_AXE, player, world);
            axe.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.9F, 1.0F);
            axe.setDamage(5.5);
            axe.setPunch(1);
            axe.setSilent(true);
            axe.pickupType = ThrowableAxeEntity.PickupPermission.ALLOWED;

            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }

            world.spawnEntity(axe);
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }
}
