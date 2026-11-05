package io.github.vulpesstella.retrolytra.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import io.github.vulpesstella.retrolytra.client.ElytraRenderHandler;
import io.github.vulpesstella.retrolytra.client.ElytraSoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        MinecraftForge.EVENT_BUS.register(new ElytraRenderHandler());

        ElytraSoundHandler soundHandler = new ElytraSoundHandler();
        MinecraftForge.EVENT_BUS.register(soundHandler);
        TickRegistry.registerTickHandler(soundHandler, Side.CLIENT);
        soundHandler.registerSound(Minecraft.getMinecraft().sndManager);
    }
}

