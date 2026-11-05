package io.github.vulpesstella.retrolytra.network;

import cpw.mods.fml.common.network.PacketDispatcher;
import io.github.vulpesstella.retrolytra.Retrolytra;
import net.minecraft.network.packet.Packet;

public final class RetrolytraNetwork {

    public static final byte START_FLYING = 0;

    private RetrolytraNetwork() {
    }

    public static void sendStartFlying() {
        Packet packet = PacketDispatcher.getPacket(Retrolytra.CHANNEL, new byte[] { START_FLYING });
        if (packet != null) {
            PacketDispatcher.sendPacketToServer(packet);
        }
    }
}

