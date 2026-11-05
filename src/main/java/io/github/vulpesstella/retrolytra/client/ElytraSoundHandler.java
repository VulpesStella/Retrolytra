package io.github.vulpesstella.retrolytra.client;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.vulpesstella.retrolytra.Config;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

import java.util.EnumSet;

/**
 * 1.6.4 has no public {@code MovingSound}; its SoundManager instead provides an
 * entity-bound looping source ({@code playEntitySound} / {@code updateSoundLocation}
 * / {@code stopEntitySound}), which is exactly what a moving elytra loop needs.
 */
@SideOnly(Side.CLIENT)
public class ElytraSoundHandler implements ITickHandler {

    private static final String SOUND = "retrolytra:elytra_loop";
    private static final String SOUND_FILE = SOUND + ".ogg";

    private boolean registered;
    private EntityPlayerSP lastPlayer;

    public void registerSound(SoundManager manager) {
        if (manager != null) {
            manager.addSound(SOUND_FILE);
            this.registered = true;
        }
    }

    @ForgeSubscribe
    public void onSoundLoad(SoundLoadEvent event) {
        if (!this.registered) {
            this.registerSound(event.manager);
        }
    }

    @Override
    public void tickStart(EnumSet<TickType> types, Object... data) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> types, Object... data) {
        if (!types.contains(TickType.CLIENT)) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.thePlayer;
        SoundManager manager = minecraft.sndManager;
        if (player == null || manager == null) {
            return;
        }

        if (this.lastPlayer != null && this.lastPlayer != player) {
            manager.stopEntitySound(this.lastPlayer);
        }
        this.lastPlayer = player;

        boolean flying = Config.enableFlightSound && !player.isDead
                && ((IElytraPlayer) player).retrolytra$isElytraFlying();
        if (!flying) {
            if (manager.isEntitySoundPlaying(player)) {
                manager.stopEntitySound(player);
            }
            return;
        }

        double speed = Math.sqrt(
                player.motionX * player.motionX
                        + player.motionY * player.motionY
                        + player.motionZ * player.motionZ);
        float volume = MathHelper.clamp_float((float) (speed * speed / 4.0D), 0.0F, 1.0F);
        float pitch = volume > 0.8F ? 1.0F + (volume - 0.8F) : 1.0F;

        if (!manager.isEntitySoundPlaying(player)) {
            if (volume > 0.0F) {
                manager.playEntitySound(SOUND, player, volume, pitch, true);
            }
        } else {
            manager.updateSoundLocation(player);
            manager.setEntitySoundVolume(player, volume);
            manager.setEntitySoundPitch(player, pitch);
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return "RetrolytraElytraSound";
    }
}

