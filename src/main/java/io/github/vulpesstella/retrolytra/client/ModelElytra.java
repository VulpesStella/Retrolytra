package io.github.vulpesstella.retrolytra.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelElytra extends ModelBase {

    private final ModelRenderer leftWing;
    private final ModelRenderer rightWing;

    public ModelElytra() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.leftWing = new ModelRenderer(this, 22, 0);
        this.leftWing.addBox(-10.0F, 0.0F, 0.0F, 10, 20, 2, 1.0F);
        this.leftWing.setRotationPoint(5.0F, 0.0F, 0.0F);

        this.rightWing = new ModelRenderer(this, 22, 0);
        this.rightWing.mirror = true;
        this.rightWing.addBox(0.0F, 0.0F, 0.0F, 10, 20, 2, 1.0F);
        this.rightWing.setRotationPoint(-5.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                       float netHeadYaw, float headPitch, float scale) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        this.leftWing.render(scale);
        this.rightWing.render(scale);
        GL11.glPopAttrib();
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch, float scale, Entity entity) {
        float rotateX = 0.2617994F;
        float rotateZ = -0.2617994F;
        float pointY = 0.0F;
        float rotateY = 0.0F;

        if (entity instanceof IElytraPlayer && ((IElytraPlayer) entity).retrolytra$isElytraFlying()) {
            float speed = 1.0F;
            if (entity.motionY < 0.0D) {
                Vec3 motion = Vec3.createVectorHelper(entity.motionX, entity.motionY, entity.motionZ).normalize();
                speed = 1.0F - (float) Math.pow(-motion.yCoord, 1.5D);
            }

            rotateX = speed * 0.34906584F + (1.0F - speed) * rotateX;
            rotateZ = speed * -((float) Math.PI / 2.0F) + (1.0F - speed) * rotateZ;
        } else if (entity.isSneaking()) {
            rotateX = (float) Math.PI * 2.0F / 9.0F;
            rotateZ = -((float) Math.PI / 4.0F);
            pointY = 3.0F;
            rotateY = 0.08726646F;
        }

        this.leftWing.rotationPointX = 5.0F;
        this.leftWing.rotationPointY = pointY;
        this.leftWing.rotateAngleX = rotateX;
        this.leftWing.rotateAngleY = rotateY;
        this.leftWing.rotateAngleZ = rotateZ;

        this.rightWing.rotationPointX = -5.0F;
        this.rightWing.rotationPointY = pointY;
        this.rightWing.rotateAngleX = rotateX;
        this.rightWing.rotateAngleY = -rotateY;
        this.rightWing.rotateAngleZ = -rotateZ;
    }
}

