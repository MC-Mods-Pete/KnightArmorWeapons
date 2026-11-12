package net.petemc.knightarmorweapons.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.petemc.knightarmorweapons.events.HandScytheCombatEvents;
import net.petemc.knightarmorweapons.events.KnightLongSwordCombatEvents;
import net.petemc.knightarmorweapons.events.SpikedMaceCombatEvents;
import net.petemc.knightarmorweapons.items.HandScythe;
import net.petemc.knightarmorweapons.items.KnightLongSword;
import net.petemc.knightarmorweapons.items.SpikedMace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Intercepts LivingEntity.damage() to apply weapon-specific damage modifiers
 * BEFORE the damage value is processed by vanilla armor/enchantments.
 *
 * Weapons handled:
 *   - SpikedMace    → Armor Sunder + Shield Obliteration (50%)
 *   - KnightLongSword → Dead-Zone penalty / Sweet-Spot bonus
 *   - HandScythe    → Armor Penalty
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    /** Prevents recursive re-entry when we call damage() with a modified amount. */
    @Unique
    private boolean knightmod_inCustomDamage = false;

    @Inject(
        method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void knightmod_onDamage(DamageSource source, float amount,
                                    CallbackInfoReturnable<Boolean> cir) {
        // Recursion guard — skip if we are the one calling damage() below
        if (this.knightmod_inCustomDamage) return;

        // Only modify direct player attacks (same as Forge: source.getDirectEntity())
        if (!(source.getSource() instanceof PlayerEntity player)) return;

        LivingEntity self = (LivingEntity) (Object) this;
        float newAmount = amount;

        if (player.getMainHandStack().getItem() instanceof SpikedMace) {
            newAmount = SpikedMaceCombatEvents.modifyDamage(self, newAmount, player);
        } else if (player.getMainHandStack().getItem() instanceof KnightLongSword) {
            newAmount = KnightLongSwordCombatEvents.modifyDamage(self, newAmount, player);
        } else if (player.getMainHandStack().getItem() instanceof HandScythe) {
            newAmount = HandScytheCombatEvents.modifyDamage(self, newAmount, player);
        }

        // If the damage was modified, reapply with the new amount
        if (newAmount != amount) {
            this.knightmod_inCustomDamage = true;
            boolean result = self.damage(source, newAmount);
            this.knightmod_inCustomDamage = false;
            cir.setReturnValue(result);
            cir.cancel();
        }
    }
}

