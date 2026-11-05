package io.github.vulpesstella.retrolytra.mixin;

import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Rotates the whole player model into the diving pose. Without this the model
 * stays upright even though the wings and head are animated.
 */
@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer {

    @Inject(method = "rotatePlayer", at = @At("RETURN"))
    private void retrolytra$rotateForFlight(AbstractClientPlayer player, float yaw, float pitch,
                                            float partialTicks, CallbackInfo ci) {
        if (!((IElytraPlayer) player).retrolytra$isElytraFlying()) {
            return;
        }

        float ticks = ((IElytraPlayer) player).retrolytra$getTicksElytraFlying() + partialTicks;
        float progress = MathHelper.clamp_float(ticks * ticks / 100.0F, 0.0F, 1.0F);
        GL11.glRotatef(progress * (-90.0F - player.rotationPitch), 1.0F, 0.0F, 0.0F);

        Vec3 look = player.getLook(partialTicks);
        double motionSq = player.motionX * player.motionX + player.motionZ * player.motionZ;
        double lookSq = look.xCoord * look.xCoord + look.zCoord * look.zCoord;
        if (motionSq > 0.0D && lookSq > 0.0D) {
            double dot = (player.motionX * look.xCoord + player.motionZ * look.zCoord)
                    / (Math.sqrt(motionSq) * Math.sqrt(lookSq));
            dot = Math.max(-1.0D, Math.min(1.0D, dot));
            double cross = player.motionX * look.zCoord - player.motionZ * look.xCoord;
            GL11.glRotatef(
                    (float) (Math.signum(cross) * Math.acos(dot)) * 180.0F / (float) Math.PI,
                    0.0F, 1.0F, 0.0F);
        }
    }
}

