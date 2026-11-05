package io.github.vulpesstella.retrolytra;

import cpw.mods.fml.common.registry.GameRegistry;
import io.github.vulpesstella.retrolytra.recipe.ElytraDyeRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Survival recipes, selected by the {@code recipe} config option.
 */
public final class ModRecipes {

    private ModRecipes() {
    }

    public static void init() {
        String recipe = Config.recipe;
        if ("simple".equals(recipe)) {
            addSimple();
        } else if ("day_one".equals(recipe)) {
            addDayOne();
        } else if ("ridiculous".equals(recipe)) {
            addRidiculous();
        }

        if (Config.enableDyeing) {
            GameRegistry.addRecipe(new ElytraDyeRecipe());
            RecipeSorter.register(
                    "retrolytra:elytra_dye",
                    ElytraDyeRecipe.class,
                    RecipeSorter.Category.SHAPELESS,
                    "after:minecraft:shapeless");
        }
    }

    private static void addSimple() {
        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(ModItems.elytra),
                "ses",
                "plp",
                "f f",
                's', Item.silk,
                'e', Item.emerald,
                'p', Item.paper,
                'l', Item.leather,
                'f', Item.feather));
    }

    private static void addDayOne() {
        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(ModItems.elytra),
                "lLl",
                "lLl",
                "fLf",
                'l', Item.leather,
                'L', "logWood",
                'f', Item.feather));
    }

    private static void addRidiculous() {
        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(ModItems.elytra),
                "ses",
                "plp",
                "f f",
                's', Item.silk,
                'e', Block.dragonEgg,
                'p', Item.paper,
                'l', Item.leather,
                'f', Item.feather));
    }
}

