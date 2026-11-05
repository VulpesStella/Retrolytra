package io.github.vulpesstella.retrolytra.mixin;

import io.github.vulpesstella.retrolytra.elytra.IElytraEntityTrackerEntry;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(EntityTracker.class)
public abstract class MixinEntityTracker {

    @Shadow
    private Set trackedEntities;

    @Inject(method = "updateTrackedEntities", at = @At("RETURN"))
    private void retrolytra$updateElytraVelocityUpdates(CallbackInfo ci) {
        for (Object object : this.trackedEntities) {
            EntityTrackerEntry entry = (EntityTrackerEntry) object;
            if (entry == null || !(entry.myEntity instanceof IElytraPlayer)) {
                continue;
            }

            IElytraEntityTrackerEntry duck = (IElytraEntityTrackerEntry) entry;
            boolean flying = ((IElytraPlayer) entry.myEntity).retrolytra$isElytraFlying();

            if (flying) {
                if (!duck.retrolytra$isSendingVelocityUpdates()) {
                    duck.retrolytra$setWasSendingVelocityUpdates(true);
                }
                duck.retrolytra$setSendingVelocityUpdates(true);
            } else if (duck.retrolytra$wasSendingVelocityUpdates()) {
                duck.retrolytra$setSendingVelocityUpdates(false);
                duck.retrolytra$setWasSendingVelocityUpdates(false);
            }
        }
    }
}
