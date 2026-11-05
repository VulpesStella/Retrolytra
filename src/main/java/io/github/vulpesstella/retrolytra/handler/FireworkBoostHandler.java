package io.github.vulpesstella.retrolytra.handler;

import io.github.vulpesstella.retrolytra.Config;
import io.github.vulpesstella.retrolytra.elytra.IElytraPlayer;
import io.github.vulpesstella.retrolytra.entity.EntityBoostingFireworkRocket;
import io.github.vulpesstella.retrolytra.item.ItemElytra;
import io.github.vulpesstella.retrolytra.network.RetrolytraNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * Right-clicking a firework rocket while gliding attaches a boosting rocket,
 * matching the 1.9 behaviour. With {@code enableQuickTakeoff}, the same action
 * can deploy the elytra first.
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
        if (player.onGround) {
            return;
        }

        boolean flying = ((IElytraPlayer) player).retrolytra$isElytraFlying();
        if (!flying && (!Config.enableQuickTakeoff || !canQuickTakeoff(player))) {
            return;
        }

        player.swingItem();
        if (!flying) {
            // Predict the takeoff locally so the client immediately applies
            // elytra physics instead of continuing its normal fall and
            // overwriting the server-side boost with movement packets.
            ((IElytraPlayer) player).retrolytra$setElytraFlying(true);
            if (player.worldObj.isRemote) {
                RetrolytraNetwork.sendStartFlying();
            }
        }
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

    private boolean canQuickTakeoff(EntityPlayer player) {
        if (player.isInWater() || player.isRiding() || player.capabilities.isFlying) {
            return false;
        }
        ItemStack chest = ItemElytra.getElytra(player);
        return chest != null && ItemElytra.isUsable(chest);
    }
}

