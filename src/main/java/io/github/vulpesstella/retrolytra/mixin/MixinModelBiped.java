package io.github.vulpesstella.retrolytra.mixin;

import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Applies the 1.9 elytra body pose to player models. Without this the player
 * keeps the standing pose while the body is rotated, which looks like twitching.
 */
@Mixin(ModelBiped.class)
public abstract class MixinModelBiped {

    @Shadow
    public ModelRenderer bipedHead;

    @Shadow
    public ModelRenderer bipedHeadwear;

    @Shadow
    public ModelRenderer bipedBody;

    @Shadow
    public ModelRenderer bipedRightArm;

    @Shadow
    public ModelRenderer bipedLeftArm;

    @Shadow
    public ModelRenderer bipedRightLeg;

    @Shadow
    public ModelRenderer bipedLeftLeg;

    @Inject(method = "setRotationAngles", at = @At("RETURN"))
    private void retrolytra$applyElytraPose(float limbSwing, float limbSwingAmount, float ageInTicks,
                                            float netHeadYaw, float headPitch, float scale,
                                            Entity entity, CallbackInfo ci) {
        if (!(entity instanceof IElytraPlayer)
                || ((IElytraPlayer) entity).retrolytra$getTicksElytraFlying() <= 4) {
            return;
        }

        float swing = ageInTicks;
        // The elytra travel path cancels vanilla's limb-swing update, so the
        // retained limbSwingAmount can be a walking value. Cap it to avoid a
        // fast, twitchy oscillation in the air.
        float motionAmount = Math.min(limbSwingAmount, 0.25F);
        float speed = (float) (entity.motionX * entity.motionX
                + entity.motionY * entity.motionY
                + entity.motionZ * entity.motionZ);
        speed = speed / 0.2F;
        speed = speed * speed * speed;
        if (speed < 1.0F) {
            speed = 1.0F;
        }

        this.bipedHead.rotateAngleY = this.bipedHeadwear.rotateAngleY = netHeadYaw * 0.017453292F;
        this.bipedHead.rotateAngleX = this.bipedHeadwear.rotateAngleX = -((float) Math.PI / 4.0F);
        this.bipedHead.rotationPointY = 0.0F;
        this.bipedHeadwear.rotationPointY = 0.0F;

        this.bipedBody.rotateAngleX = 0.0F;
        this.bipedBody.rotateAngleY = 0.0F;

        this.bipedRightArm.rotationPointX = -5.0F;
        this.bipedRightArm.rotationPointY = 2.0F;
        this.bipedRightArm.rotationPointZ = 0.0F;
        this.bipedLeftArm.rotationPointX = 5.0F;
        this.bipedLeftArm.rotationPointY = 2.0F;
        this.bipedLeftArm.rotationPointZ = 0.0F;

        this.bipedRightArm.rotateAngleX = MathHelper.cos(swing * 0.6662F + (float) Math.PI)
                * 2.0F * motionAmount * 0.5F / speed;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(swing * 0.6662F)
                * 2.0F * motionAmount * 0.5F / speed;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;

        this.bipedRightLeg.rotationPointY = 12.0F;
        this.bipedLeftLeg.rotationPointY = 12.0F;
        this.bipedRightLeg.rotationPointZ = 0.1F;
        this.bipedLeftLeg.rotationPointZ = 0.1F;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(swing * 0.6662F)
                * 1.4F * motionAmount / speed;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(swing * 0.6662F + (float) Math.PI)
                * 1.4F * motionAmount / speed;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;
        this.bipedRightLeg.rotateAngleZ = 0.0F;
        this.bipedLeftLeg.rotateAngleZ = 0.0F;
    }
}

