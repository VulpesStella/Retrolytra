package io.github.vulpesstella.retrolytra;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import io.github.vulpesstella.retrolytra.network.RetrolytraPacketHandler;
import io.github.vulpesstella.retrolytra.proxy.CommonProxy;

/**
 * Retrolytra - an Elytra backport for Minecraft 1.6.4.
 */
@Mod(modid = Retrolytra.MOD_ID, name = Retrolytra.MOD_NAME, version = Retrolytra.VERSION)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false,
        channels = { Retrolytra.CHANNEL },
        packetHandler = RetrolytraPacketHandler.class)
public final class Retrolytra {

    // Expanded from gradle.properties into build/prepared-sources/main/java.
    public static final String MOD_ID = "@MOD_ID@";
    public static final String MOD_NAME = "@MOD_NAME@";
    public static final String VERSION = "@MOD_VERSION@";

    public static final String CHANNEL = "Retrolytra";

    @Mod.Instance(MOD_ID)
    public static Retrolytra instance;

    @SidedProxy(
            clientSide = "io.github.vulpesstella.retrolytra.proxy.ClientProxy",
            serverSide = "io.github.vulpesstella.retrolytra.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.load(event);
        ModItems.init();
        ModRecipes.init();
        ModEntities.init();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
}

