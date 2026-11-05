package io.github.vulpesstella.retrolytra.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.vulpesstella.retrolytra.Config;
import io.github.vulpesstella.retrolytra.recipe.ElytraDyeRecipe;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

/**
 * The elytra item. It occupies the chest armour slot but deliberately does not
 * extend {@link net.minecraft.item.ItemArmor}: the wings are rendered by our own
 * model rather than by the vanilla armour renderer.
 */
public class ItemElytra extends Item {

    @SideOnly(Side.CLIENT)
    private Icon iconNormal;

    @SideOnly(Side.CLIENT)
    private Icon iconBroken;

    public ItemElytra(int id) {
        super(id);
        this.maxStackSize = 1;
        this.setMaxDamage(Config.durability);
        this.setCreativeTab(CreativeTabs.tabCombat);
    }

    /** Returns the elytra worn in the chest slot, or {@code null}. */
    public static ItemStack getElytra(EntityPlayer player) {
        if (player == null) {
            return null;
        }
        ItemStack stack = player.getCurrentArmor(2);
        if (stack != null && stack.getItem() instanceof ItemElytra) {
            return stack;
        }
        return null;
    }

    /** True while the elytra can still be used to fly. */
    public static boolean isUsable(ItemStack stack) {
        return stack != null && stack.getItemDamage() < stack.getMaxDamage() - 1;
    }

    /** Returns the dye metadata (0..15), or -1 when the elytra is undyed. */
    public static int getDyeColor(ItemStack stack) {
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey(ElytraDyeRecipe.DYE_TAG)) {
                return tag.getInteger(ElytraDyeRecipe.DYE_TAG);
            }
        }
        return -1;
    }

    /**
     * The colour used for rendering. When dyeing is disabled the NBT colour is
     * deliberately kept on the stack, but it is not displayed.
     */
    public static int getVisibleDyeColor(ItemStack stack) {
        return Config.enableDyeing ? getDyeColor(stack) : -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        int dye = getVisibleDyeColor(stack);
        if (dye < 0 || dye >= ItemDye.dyeColors.length || dye == 15) {
            return 16777215;
        }
        return ItemDye.dyeColors[dye];
    }

    @Override
    public boolean isValidArmor(ItemStack stack, int armorType, Entity entity) {
        return armorType == 1;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (player.getCurrentArmor(2) == null) {
            player.setCurrentItemOrArmor(3, stack.copy());
            stack.stackSize = 0;
        }
        return stack;
    }

    @Override
    public int getItemEnchantability() {
        // Vanilla elytra uses the leather armour material.
        return 15;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair != null && repair.getItem() == Item.leather;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        this.iconNormal = register.registerIcon("retrolytra:elytra");
        this.iconBroken = register.registerIcon("retrolytra:elytra_broken");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int damage) {
        return isBroken(damage) ? this.iconBroken : this.iconNormal;
    }

    @SideOnly(Side.CLIENT)
    private boolean isBroken(int damage) {
        return damage >= this.getMaxDamage() - 1;
    }
}

