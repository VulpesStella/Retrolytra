package io.github.vulpesstella.retrolytra.mixin;

import io.github.vulpesstella.retrolytra.elytra.IElytraEntityTrackerEntry;
import net.minecraft.entity.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityTrackerEntry.class)
public abstract class MixinEntityTrackerEntry implements IElytraEntityTrackerEntry {

    @Shadow
    private boolean sendVelocityUpdates;

    @Unique
    private boolean retrolytra$wasSendingVelocityUpdates;

    @Override
    public boolean retrolytra$wasSendingVelocityUpdates() {
        return this.retrolytra$wasSendingVelocityUpdates;
    }

    @Override
    public void retrolytra$setWasSendingVelocityUpdates(boolean value) {
        this.retrolytra$wasSendingVelocityUpdates = value;
    }

    @Override
    public boolean retrolytra$isSendingVelocityUpdates() {
        return this.sendVelocityUpdates;
    }

    @Override
    public void retrolytra$setSendingVelocityUpdates(boolean value) {
        this.sendVelocityUpdates = value;
    }
}
