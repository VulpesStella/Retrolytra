package io.github.vulpesstella.retrolytra.elytra;

/**
 * Duck interface implemented on {@link net.minecraft.entity.player.EntityPlayer}
 * by {@code MixinEntityPlayer}.
 */
public interface IElytraPlayer {

    boolean retrolytra$isElytraFlying();

    void retrolytra$setElytraFlying(boolean flying);

    int retrolytra$getTicksElytraFlying();
}

