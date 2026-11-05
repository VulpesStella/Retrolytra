package io.github.vulpesstella.retrolytra.handler;

import io.github.vulpesstella.retrolytra.Config;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import io.github.vulpesstella.retrolytra.entity.EntityBoostingFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Right-clicking a firework rocket while gliding attaches a boosting rocket,
 * matching the 1.9 behaviour.
 */
public class FireworkBoostHandler {

    @ForgeSubscribe
    public void onInteract(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            return;
        }
        if (!Config.enableFireworkBoost) {
            return;
        }

        EntityPlayer player = event.entityPlayer;
        ItemStack held = player.getHeldItem();
        if (held == null || held.getItem() != Item.firework) {
            return;
        }
        if (!((IElytraPlayer) player).retrolytra$isElytraFlying()) {
            return;
        }

        player.swingItem();
        if (!player.worldObj.isRemote) {
            player.worldObj.spawnEntityInWorld(
                    new EntityBoostingFireworkRocket(player.worldObj, held, player));

            if (!player.capabilities.isCreativeMode) {
                --held.stackSize;
                if (held.stackSize <= 0) {
                    player.inventory.mainInventory[player.inventory.currentItem] = null;
                }
            }
        }

        event.useItem = Event.Result.ALLOW;
    }
}

