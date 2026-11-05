package io.github.vulpesstella.retrolytra.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * A firework rocket that follows the player and boosts them while gliding.
 * 1.6.4's EntityFireworkRocket has no attachment support, so this subclass adds
 * a data-watched target entity id and drives the boost from onUpdate.
 */
public class EntityBoostingFireworkRocket extends EntityFireworkRocket {

    private static final int BOOSTED_ENTITY_ID = 9;

    private EntityLivingBase boostedEntity;

    public EntityBoostingFireworkRocket(World world) {
        super(world);
    }

    public EntityBoostingFireworkRocket(World world, ItemStack stack, EntityLivingBase target) {
        super(world, target.posX, target.posY, target.posZ, stack);
        this.dataWatcher.updateObject(BOOSTED_ENTITY_ID, target.entityId);
        this.boostedEntity = target;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(BOOSTED_ENTITY_ID, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.isDead || !this.isAttachedToEntity()) {
            return;
        }

        if (this.boostedEntity == null) {
            Entity entity = this.worldObj.getEntityByID(
                    this.dataWatcher.getWatchableObjectInt(BOOSTED_ENTITY_ID));
            if (entity instanceof EntityLivingBase) {
                this.boostedEntity = (EntityLivingBase) entity;
            }
        }

        if (this.boostedEntity == null) {
            return;
        }

        if (this.boostedEntity instanceof IElytraPlayer
                && ((IElytraPlayer) this.boostedEntity).retrolytra$isElytraFlying()) {
            Vec3 look = this.boostedEntity.getLookVec();
            this.boostedEntity.motionX += look.xCoord * 0.1D
                    + (look.xCoord * 1.5D - this.boostedEntity.motionX) * 0.5D;
            this.boostedEntity.motionY += look.yCoord * 0.1D
                    + (look.yCoord * 1.5D - this.boostedEntity.motionY) * 0.5D;
            this.boostedEntity.motionZ += look.zCoord * 0.1D
                    + (look.zCoord * 1.5D - this.boostedEntity.motionZ) * 0.5D;
        } else {
            this.setDead();
        }

        this.setPosition(this.boostedEntity.posX, this.boostedEntity.posY, this.boostedEntity.posZ);
        this.motionX = this.boostedEntity.motionX;
        this.motionY = this.boostedEntity.motionY;
        this.motionZ = this.boostedEntity.motionZ;
    }

    public boolean isAttachedToEntity() {
        return this.dataWatcher.getWatchableObjectInt(BOOSTED_ENTITY_ID) > 0;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("RetrolytraBoostedEntity",
                this.dataWatcher.getWatchableObjectInt(BOOSTED_ENTITY_ID));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.dataWatcher.updateObject(BOOSTED_ENTITY_ID, tag.getInteger("RetrolytraBoostedEntity"));
    }
}

