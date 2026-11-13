package net.petemc.knightarmorweapons.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
 * Intercepts LivingEntity.hurt() to apply weapon-specific damage modifiers
 * BEFORE the damage value is processed by vanilla armor/enchantments (Forge: LivingHurtEvent).
 *
 *   - SpikedMace      -> Armor Sunder + Shield Obliteration (50%)
 *   - KnightLongSword -> Dead-Zone penalty / Sweet-Spot bonus
 *   - HandScythe      -> Armor Penalty + Shield Bypass (50%)
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    /** Prevents recursive re-entry when we call hurt() with a modified amount. */
    @Unique
    private boolean knightmod_inCustomDamage = false;

    @Inject(
        method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void knightmod_onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.knightmod_inCustomDamage) return;

        // Only modify direct player attacks (Forge: source.getDirectEntity())
        if (!(source.getDirectEntity() instanceof Player player)) return;

        LivingEntity self = (LivingEntity) (Object) this;
        float newAmount = amount;

        if (player.getMainHandItem().getItem() instanceof SpikedMace) {
            newAmount = SpikedMaceCombatEvents.modifyDamage(self, newAmount, player);
        } else if (player.getMainHandItem().getItem() instanceof KnightLongSword) {
            newAmount = KnightLongSwordCombatEvents.modifyDamage(self, newAmount, player);
        } else if (player.getMainHandItem().getItem() instanceof HandScythe) {
            newAmount = HandScytheCombatEvents.modifyDamage(self, newAmount, player, source);
        }

        // If the damage was modified, reapply with the new amount
        if (newAmount != amount) {
            this.knightmod_inCustomDamage = true;
            boolean result = self.hurt(source, newAmount);
            this.knightmod_inCustomDamage = false;
            cir.setReturnValue(result);
        }
    }

    /**
     * Hand Scythe Shield Bypass: a raised shield does not block the main hit
     * (Forge: ShieldBlockEvent cancelled). The 50% damage penalty is applied in
     * HandScytheCombatEvents.modifyDamage().
     */
    @WrapOperation(
        method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
        at = @At(value = "INVOKE",
                 target = "Lnet/minecraft/world/entity/LivingEntity;isDamageSourceBlocked(Lnet/minecraft/world/damagesource/DamageSource;)Z")
    )
    private boolean knightmod_scytheShieldBypass(LivingEntity self, DamageSource source, Operation<Boolean> original) {
        if (HandScytheCombatEvents.bypassesShield(self, source)) return false;
        return original.call(self, source);
    }
}
