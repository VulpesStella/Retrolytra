package io.github.vulpesstella.retrolytra.mixin;

import io.github.vulpesstella.retrolytra.elytra.IClientElytraPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer implements IClientElytraPlayer {

    @Unique
    private float retrolytra$rotateElytraX;

    @Unique
    private float retrolytra$rotateElytraY;

    @Unique
    private float retrolytra$rotateElytraZ;

    @Override
    public float retrolytra$getRotateElytraX() {
        return this.retrolytra$rotateElytraX;
    }

    @Override
    public void retrolytra$setRotateElytraX(float value) {
        this.retrolytra$rotateElytraX = value;
    }

    @Override
    public float retrolytra$getRotateElytraY() {
        return this.retrolytra$rotateElytraY;
    }

    @Override
    public void retrolytra$setRotateElytraY(float value) {
        this.retrolytra$rotateElytraY = value;
    }

    @Override
    public float retrolytra$getRotateElytraZ() {
        return this.retrolytra$rotateElytraZ;
    }

    @Override
    public void retrolytra$setRotateElytraZ(float value) {
        this.retrolytra$rotateElytraZ = value;
    }
}
