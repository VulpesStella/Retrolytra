package io.github.vulpesstella.retrolytra.mixin;

import io.github.vulpesstella.retrolytra.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla 1.9 elytra is an ItemArmor, so shift-clicking it equips it. The 1.6.4
 * ContainerPlayer hardcodes {@code instanceof ItemArmor}; handle the elytra
 * explicitly.
 */
@Mixin(ContainerPlayer.class)
public abstract class MixinContainerPlayer {

    @Inject(method = "transferStackInSlot", at = @At("HEAD"), cancellable = true)
    private void retrolytra$shiftClickEquip(EntityPlayer player, int index,
                                            CallbackInfoReturnable<ItemStack> cir) {
        ContainerPlayer container = (ContainerPlayer) (Object) this;
        Slot source = (Slot) container.inventorySlots.get(index);
        if (source == null || !source.getHasStack()) {
            return;
        }

        ItemStack stack = source.getStack();
        if (stack.getItem() != ModItems.elytra || index < 9 || index >= 45) {
            return;
        }

        Slot chest = (Slot) container.inventorySlots.get(6);
        if (chest == null || chest.getHasStack()) {
            return;
        }

        ItemStack moved = stack.copy();
        chest.putStack(moved);
        source.putStack(null);
        source.onSlotChanged();
        chest.onSlotChanged();
        cir.setReturnValue(moved);
    }
}

