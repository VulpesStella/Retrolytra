package io.github.vulpesstella.retrolytra.util;

import net.minecraft.util.DamageSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 1.6.4 has no public DamageSource constructor, but 1.9 uses a dedicated
 * "flyIntoWall" damage type with the "experienced kinetic energy" death
 * message. Reflective creation keeps the correct type without an access
 * transformer.
 */
public final class RetrolytraDamageSources {

    public static final DamageSource FLY_INTO_WALL = createFlyIntoWall();

    private RetrolytraDamageSources() {
    }

    private static DamageSource createFlyIntoWall() {
        try {
            Constructor<DamageSource> constructor =
                    DamageSource.class.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            DamageSource source = constructor.newInstance("flyIntoWall");

            Method bypassArmor = DamageSource.class.getDeclaredMethod("setDamageBypassesArmor");
            bypassArmor.setAccessible(true);
            return (DamageSource) bypassArmor.invoke(source);
        } catch (Throwable ignored) {
            return DamageSource.fall;
        }
    }
}

