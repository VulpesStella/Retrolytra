package io.github.vulpesstella.retrolytra;

import cpw.mods.fml.common.registry.GameRegistry;
import io.github.vulpesstella.retrolytra.item.ItemElytra;

public final class ModItems {

    public static ItemElytra elytra;

    private ModItems() {
    }

    public static void init() {
        elytra = new ItemElytra(Config.itemId);
        elytra.setUnlocalizedName("retrolytra.elytra");
        elytra.setTextureName("retrolytra:elytra");
        GameRegistry.registerItem(elytra, "elytra");
    }
}

