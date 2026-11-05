package io.github.vulpesstella.retrolytra;

import cpw.mods.fml.common.registry.EntityRegistry;
import io.github.vulpesstella.retrolytra.entity.EntityBoostingFireworkRocket;

public final class ModEntities {

    private ModEntities() {
    }

    public static void init() {
        EntityRegistry.registerModEntity(
                EntityBoostingFireworkRocket.class,
                "boosting_firework_rocket",
                0,
                Retrolytra.instance,
                64,
                1,
                true);
    }
}

