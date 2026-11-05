package io.github.vulpesstella.retrolytra.mixin;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetServerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;

/**
 * The vanilla "moved too quickly" check rejects normal elytra speeds. It uses a
 * single 100.0D constant; raise it while the player is flying.
 */
@Mixin(NetServerHandler.class)
public abstract class MixinNetServerHandler {

    @Shadow
    public EntityPlayerMP playerEntity;

    @ModifyConstant(method = "handleFlying", constant = @Constant(doubleValue = 100.0D))
    private double retrolytra$movementLimit(double original) {
        return ((IElytraPlayer) this.playerEntity).retrolytra$isElytraFlying() ? 300.0D : original;
    }
}

