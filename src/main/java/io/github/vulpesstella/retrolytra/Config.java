package io.github.vulpesstella.retrolytra;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * Static configuration, loaded during pre-init before items are registered.
 */
public final class Config {

    /** Numeric item id passed to {@link net.minecraft.item.Item#Item(int)}. */
    public static int itemId = 5000;

    /** Durability of the elytra, in points of damage. */
    public static int durability = 432;

    /**
     * Bit in {@code Entity} flags byte 0 used to synchronise the flying state.
     * Bits 0..5 are used by vanilla, so 6 and 7 are the only safe choices.
     */
    public static int dataWatcherFlag = 7;

    /** One of {@code none}, {@code simple}, {@code day_one}, {@code ridiculous}. */
    public static String recipe = "simple";

    /** Enables the elytra dye crafting recipe and the dyed wing colour. */
    public static boolean enableDyeing = false;

    /** Enables the looping flight sound. */
    public static boolean enableFlightSound = true;

    /** Enables firework-rocket boosting while gliding. */
    public static boolean enableFireworkBoost = true;

    /**
     * 1.15+ takeoff: jump while airborne, no downward-motion requirement.
     * When false, the 1.9-1.14 {@code motionY < 0} rule is used.
     */
    public static boolean enableNewElytraTakeoffLogic = true;

    /**
     * Exempt an actively gliding elytra player from the vanilla
     * {@code allow-flight=false} floating kick. The exemption only applies
     * while the server-side elytra flag is set.
     */
    public static boolean exemptElytraFromAllowFlight = true;

    /**
     * QuickTakeoff: right-clicking a firework while airborne starts elytra
     * flight and applies the boost in the same action. Disabled by default.
     */
    public static boolean enableQuickTakeoff = false;

    private Config() {
    }

    public static void load(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();

        Property itemIdProperty = config.get(
                Configuration.CATEGORY_GENERAL, "itemId", 5000,
                "Numeric item id for the elytra. Change this if it conflicts with another mod.");
        itemId = itemIdProperty.getInt();

        Property durabilityProperty = config.get(
                Configuration.CATEGORY_GENERAL, "durability", 432,
                "Maximum damage the elytra can take before it becomes unusable.");
        durability = durabilityProperty.getInt();

        Property flagProperty = config.get(
                Configuration.CATEGORY_GENERAL, "dataWatcherFlag", 7,
                "Entity flag bit used to synchronise the elytra flying state. "
                        + "Only change this if another mod also uses bit 6 or 7.");
        dataWatcherFlag = flagProperty.getInt();

        Property recipeProperty = config.get(
                Configuration.CATEGORY_GENERAL, "recipe", "simple",
                "Survival recipe: none, simple, day_one, or ridiculous.");
        recipe = recipeProperty.getString().trim().toLowerCase();

        Property dyeProperty = config.get(
                Configuration.CATEGORY_GENERAL, "enableDyeing", false,
                "Enable the elytra dye recipe and tinted wings (non-vanilla, from Backlytra).");
        enableDyeing = dyeProperty.getBoolean(false);

        Property soundProperty = config.get(
                Configuration.CATEGORY_GENERAL, "enableFlightSound", true,
                "Enable the looping elytra flight sound.");
        enableFlightSound = soundProperty.getBoolean(true);

        Property fireworkProperty = config.get(
                Configuration.CATEGORY_GENERAL, "enableFireworkBoost", true,
                "Enable firework-rocket boosting while gliding.");
        enableFireworkBoost = fireworkProperty.getBoolean(true);

        Property takeoffProperty = config.get(
                Configuration.CATEGORY_GENERAL, "enableNewElytraTakeoffLogic", true,
                "Use 1.15+ takeoff logic: jump while airborne, even while still ascending.");
        enableNewElytraTakeoffLogic = takeoffProperty.getBoolean(true);

        Property allowFlightProperty = config.get(
                Configuration.CATEGORY_GENERAL, "exemptElytraFromAllowFlight", true,
                "Allow active elytra gliding on servers with allow-flight=false. "
                        + "Only bypasses the floating kick while the elytra is deployed.");
        exemptElytraFromAllowFlight = allowFlightProperty.getBoolean(true);

        Property quickTakeoffProperty = config.get(
                Configuration.CATEGORY_GENERAL, "enableQuickTakeoff", false,
                "Allow right-clicking a firework while airborne to deploy the elytra and boost immediately.");
        enableQuickTakeoff = quickTakeoffProperty.getBoolean(false);

        config.save();
    }
}

