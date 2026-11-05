package io.github.vulpesstella.retrolytra.mixin;

import io.github.vulpesstella.retrolytra.Config;
import io.github.vulpesstella.retrolytra.ModItems;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import io.github.vulpesstella.retrolytra.item.ItemElytra;
import io.github.vulpesstella.retrolytra.network.RetrolytraNetwork;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP {

    @Unique
    private boolean retrolytra$lastJumping;

    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    private void retrolytra$startFlying(CallbackInfo ci) {
        EntityPlayerSP self = (EntityPlayerSP) (Object) this;
        boolean jumping = self.movementInput.jump;
        if (jumping && !this.retrolytra$lastJumping && !self.onGround
                && (Config.enableNewElytraTakeoffLogic || self.motionY < 0.0D)
                && !((IElytraPlayer) self).retrolytra$isElytraFlying()
                && !self.capabilities.isFlying && !self.isInWater() && !self.isRiding()) {
            ItemStack chest = self.getCurrentItemOrArmor(3);
            if (chest != null && chest.getItem() == ModItems.elytra && ItemElytra.isUsable(chest)) {
                RetrolytraNetwork.sendStartFlying();
            }
        }
        this.retrolytra$lastJumping = jumping;
    }
}

