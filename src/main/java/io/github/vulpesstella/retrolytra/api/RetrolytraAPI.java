package io.github.vulpesstella.retrolytra.api;

import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import io.github.vulpesstella.retrolytra.item.ItemElytra;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Small compatibility surface for other mods. The underlying state is the
 * player's entity flag, so it is synchronised to clients by vanilla.
 */
public final class RetrolytraAPI {

    private RetrolytraAPI() {
    }

    public static boolean isElytraFlying(EntityPlayer player) {
        return player != null && ((IElytraPlayer) player).retrolytra$isElytraFlying();
    }

    public static void setElytraFlying(EntityPlayer player, boolean flying) {
        if (player != null) {
            ((IElytraPlayer) player).retrolytra$setElytraFlying(flying);
        }
    }

    public static int getTicksElytraFlying(EntityPlayer player) {
        return player != null ? ((IElytraPlayer) player).retrolytra$getTicksElytraFlying() : 0;
    }

    public static ItemStack getWornElytra(EntityPlayer player) {
        return ItemElytra.getElytra(player);
    }
}

