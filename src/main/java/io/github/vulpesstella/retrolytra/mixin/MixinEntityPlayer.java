package io.github.vulpesstella.retrolytra.mixin;

import io.github.vulpesstella.retrolytra.Config;
import io.github.vulpesstella.retrolytra.ModItems;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import io.github.vulpesstella.retrolytra.item.ItemElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase implements IElytraPlayer {

    @Shadow
    public PlayerCapabilities capabilities;

    @Shadow
    public abstract boolean isPlayerSleeping();

    @Shadow
    protected abstract void setHideCape(int layer, boolean hidden);

    @Unique
    private int retrolytra$ticksElytraFlying;

    public MixinEntityPlayer(World world) {
        super(world);
    }

    @Override
    public boolean retrolytra$isElytraFlying() {
        return this.getFlag(Config.dataWatcherFlag);
    }

    @Override
    public void retrolytra$setElytraFlying(boolean flying) {
        this.setFlag(Config.dataWatcherFlag, flying);
    }

    @Override
    public int retrolytra$getTicksElytraFlying() {
        return this.retrolytra$ticksElytraFlying;
    }

    @Inject(method = "getEyeHeight", at = @At("HEAD"), cancellable = true)
    private void retrolytra$getEyeHeight(CallbackInfoReturnable<Float> cir) {
        if (this.retrolytra$isElytraFlying() && !this.isPlayerSleeping()) {
            cir.setReturnValue(0.4F);
        }
    }

    @Inject(method = "onUpdate", at = @At("TAIL"))
    private void retrolytra$tick(CallbackInfo ci) {
        ItemStack chest = this.getCurrentItemOrArmor(3);
        boolean hasElytra = chest != null && chest.getItem() == ModItems.elytra;

        // Hide the vanilla cape while the elytra is worn. DataWatcher id 16 is
        // synchronised, so other clients see this too.
        this.setHideCape(1, hasElytra);

        boolean flying = this.retrolytra$isElytraFlying();
        if (flying && (!hasElytra || !ItemElytra.isUsable(chest) || this.onGround
                || this.isInWater() || this.isRiding() || this.capabilities.isFlying)) {
            this.retrolytra$setElytraFlying(false);
            flying = false;
        }

        if (flying) {
            this.retrolytra$ticksElytraFlying++;
            if (!this.worldObj.isRemote && this.retrolytra$ticksElytraFlying % 20 == 0
                    && chest.getItemDamage() < chest.getMaxDamage() - 1) {
                chest.damageItem(1, this);
            }
        } else {
            this.retrolytra$ticksElytraFlying = 0;
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void retrolytra$writeToNbt(NBTTagCompound tag, CallbackInfo ci) {
        tag.setBoolean("RetrolytraFlying", this.retrolytra$isElytraFlying());
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void retrolytra$readFromNbt(NBTTagCompound tag, CallbackInfo ci) {
        this.retrolytra$setElytraFlying(tag.getBoolean("RetrolytraFlying"));
    }
}

