package io.github.vulpesstella.retrolytra.mixin;

import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import io.github.vulpesstella.retrolytra.util.RetrolytraDamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Replaces vanilla travel with 1.9-style elytra physics while flying.
 */
@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    public MixinEntityLivingBase(World world) {
        super(world);
    }

    @Inject(method = "moveEntityWithHeading", at = @At("HEAD"), cancellable = true)
    private void retrolytra$moveWithElytra(float strafe, float forward, CallbackInfo ci) {
        if (!((Object) this instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) (Object) this;
        if (!((IElytraPlayer) player).retrolytra$isElytraFlying()) {
            return;
        }
        if (this.isInWater() || this.handleLavaMovement() || player.capabilities.isFlying) {
            return;
        }

        if (this.motionY > -0.5D) {
            this.fallDistance = 1.0F;
        }

        Vec3 look = this.getLookVec();
        float pitch = this.rotationPitch * 0.017453292F;
        double horizontalLook = Math.sqrt(look.xCoord * look.xCoord + look.zCoord * look.zCoord);
        double horizontalSpeed = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        double lookLength = look.lengthVector();
        float lift = MathHelper.cos(pitch);
        lift = (float) ((double) lift * (double) lift * Math.min(1.0D, lookLength / 0.4D));
        this.motionY += -0.08D + (double) lift * 0.06D;

        if (this.motionY < 0.0D && horizontalLook > 0.0D) {
            double dive = this.motionY * -0.1D * (double) lift;
            this.motionY += dive;
            this.motionX += look.xCoord * dive / horizontalLook;
            this.motionZ += look.zCoord * dive / horizontalLook;
        }

        if (pitch < 0.0F && horizontalLook > 1.0E-4D) {
            double climb = horizontalSpeed * (double) (-MathHelper.sin(pitch)) * 0.04D;
            this.motionY += climb * 3.2D;
            this.motionX -= look.xCoord * climb / horizontalLook;
            this.motionZ -= look.zCoord * climb / horizontalLook;
        }

        if (horizontalLook > 0.0D) {
            this.motionX += (look.xCoord / horizontalLook * horizontalSpeed - this.motionX) * 0.1D;
            this.motionZ += (look.zCoord / horizontalLook * horizontalSpeed - this.motionZ) * 0.1D;
        }

        this.motionX *= 0.9900000095367432D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9900000095367432D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.isCollidedHorizontally && !this.worldObj.isRemote) {
            double newHorizontalSpeed = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            double lostSpeed = horizontalSpeed - newHorizontalSpeed;
            float damage = (float) (lostSpeed * 10.0D - 3.0D);
            if (damage > 0.0F) {
                this.playSound(damage > 4.0F ? "damage.fallbig" : "damage.fallsmall", 1.0F, 1.0F);
                this.attackEntityFrom(RetrolytraDamageSources.FLY_INTO_WALL, damage);
            }
        }

        if (this.onGround && !this.worldObj.isRemote) {
            ((IElytraPlayer) player).retrolytra$setElytraFlying(false);
        }

        ci.cancel();
    }
}

