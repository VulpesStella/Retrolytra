package io.github.vulpesstella.retrolytra.elytra;

/**
 * Client-only per-player wing rotation smoothing, matching the vanilla 1.9
 * elytra model.
 */
public interface IClientElytraPlayer {

    float retrolytra$getRotateElytraX();

    void retrolytra$setRotateElytraX(float value);

    float retrolytra$getRotateElytraY();

    void retrolytra$setRotateElytraY(float value);

    float retrolytra$getRotateElytraZ();

    void retrolytra$setRotateElytraZ(float value);
}
