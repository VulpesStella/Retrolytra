package io.github.vulpesstella.retrolytra.network;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import io.github.vulpesstella.retrolytra.Config;
import io.github.vulpesstella.retrolytra.ModItems;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import io.github.vulpesstella.retrolytra.item.ItemElytra;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class RetrolytraPacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (!(player instanceof EntityPlayerMP)) {
            return;
        }

        byte id;
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(packet.data));
            id = in.readByte();
        } catch (IOException e) {
            return;
        }

        if (id == RetrolytraNetwork.START_FLYING) {
            this.handleStartFlying((EntityPlayerMP) player);
        }
    }

    private void handleStartFlying(EntityPlayerMP player) {
        IElytraPlayer elytraPlayer = (IElytraPlayer) player;
        if (player.onGround
                || (!Config.enableNewElytraTakeoffLogic && player.motionY >= 0.0D)
                || player.isInWater() || player.isRiding()
                || player.capabilities.isFlying || elytraPlayer.retrolytra$isElytraFlying()) {
            return;
        }

        ItemStack chest = player.getCurrentItemOrArmor(3);
        if (chest != null && chest.getItem() == ModItems.elytra && ItemElytra.isUsable(chest)) {
            elytraPlayer.retrolytra$setElytraFlying(true);
        }
    }
}

