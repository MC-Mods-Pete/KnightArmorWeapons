package net.petemc.knightarmorweapons.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.petemc.knightarmorweapons.items.WeaponReach;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Applies the weapon entity reach (Forge: ENTITY_REACH attribute modifiers) on the client.
 *
 * GameRenderer.updateTargetedEntity() hardcodes the survival entity reach:
 * {@code if (reach > 3.0) limited = true; ... if (limited && squaredDistance > 9.0) miss}.
 * Both constants are replaced with the reach of the held weapon. Creative mode (extended reach)
 * does not use them and stays unchanged. The server accepts entity attacks up to 6 blocks.
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private MinecraftClient client;

    private double knightmod_reach() {
        return this.client.player != null
                ? WeaponReach.getEntityReach(this.client.player)
                : WeaponReach.BASE_ENTITY_REACH;
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3.0))
    private double knightmod_entityReach(double original) {
        return knightmod_reach();
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9.0))
    private double knightmod_entityReachSquared(double original) {
        double reach = knightmod_reach();
        return reach * reach;
    }
}
