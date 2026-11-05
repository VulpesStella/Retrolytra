package io.github.vulpesstella.retrolytra.recipe;

import io.github.vulpesstella.retrolytra.item.ItemElytra;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * One elytra plus one dye, producing a dyed elytra. The colour is stored in NBT
 * so it survives durability changes and can be read by the renderer.
 */
public class ElytraDyeRecipe implements IRecipe {

    public static final String DYE_TAG = "retrolytra:elytraDye";

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        boolean foundElytra = false;
        boolean foundDye = false;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack == null) {
                continue;
            }
            if (stack.getItem() instanceof ItemElytra) {
                if (foundElytra) {
                    return false;
                }
                foundElytra = true;
            } else if (stack.getItem() instanceof ItemDye) {
                if (foundDye) {
                    return false;
                }
                foundDye = true;
            } else {
                return false;
            }
        }

        return foundElytra && foundDye;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        int dye = -1;
        ItemStack elytra = null;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack == null) {
                continue;
            }
            if (stack.getItem() instanceof ItemDye) {
                dye = stack.getItemDamage();
            } else {
                elytra = stack;
            }
        }

        if (elytra == null || dye < 0) {
            return null;
        }

        ItemStack result = elytra.copy();
        if (!result.hasTagCompound()) {
            result.setTagCompound(new NBTTagCompound());
        }
        result.getTagCompound().setInteger(DYE_TAG, dye);
        return result;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}

