package io.github.vulpesstella.retrolytra.mixin;

import io.github.vulpesstella.retrolytra.Config;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetServerHandler;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * The vanilla "moved too quickly" check rejects normal elytra speeds. It uses a
 * single 100.0D constant; raise it while the player is flying.
 *
 * <p>Vanilla 1.6.4 also kicks players who float for 80 ticks when
 * {@code allow-flight=false}. Since 1.6.4 predates elytra, exemption that
 * vanilla applies to elytra flight has to be supplied here.</p>
 */
@Mixin(NetServerHandler.class)
public abstract class MixinNetServerHandler {

    @Shadow
    public EntityPlayerMP playerEntity;

    @ModifyConstant(method = "handleFlying", constant = @Constant(doubleValue = 100.0D))
    private double retrolytra$movementLimit(double original) {
        return ((IElytraPlayer) this.playerEntity).retrolytra$isElytraFlying() ? 300.0D : original;
    }

    @Redirect(
            method = "handleFlying",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isFlightAllowed()Z"))
    private boolean retrolytra$allowElytraFlight(MinecraftServer server) {
        if (Config.exemptElytraFromAllowFlight
                && ((IElytraPlayer) this.playerEntity).retrolytra$isElytraFlying()) {
            return true;
        }
        return server.isFlightAllowed();
    }
}

