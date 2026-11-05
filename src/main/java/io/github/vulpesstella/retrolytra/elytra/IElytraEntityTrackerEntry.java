package io.github.vulpesstella.retrolytra.elytra;

/**
 * Duck interface for the server-side entity tracker entry used while a player
 * is gliding. Velocity updates are normally disabled for players; enabling
 * them makes other clients interpolate elytra movement smoothly.
 */
public interface IElytraEntityTrackerEntry {

    boolean retrolytra$wasSendingVelocityUpdates();

    void retrolytra$setWasSendingVelocityUpdates(boolean value);

    boolean retrolytra$isSendingVelocityUpdates();

    void retrolytra$setSendingVelocityUpdates(boolean value);
}
