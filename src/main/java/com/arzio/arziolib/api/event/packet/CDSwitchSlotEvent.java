package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDPacketDataWrapper;
import com.arzio.arziolib.api.util.CDSpecialSlot;

public class CDSwitchSlotEvent extends PayloadPacketEvent{

	private static final HandlerList handlers = new HandlerList();
	private final CDSpecialSlot slot;

	public CDSwitchSlotEvent(Player player, CDSpecialSlot slot, CDPacketDataWrapper dataWrapper) {
		super(player, dataWrapper);
		this.slot = slot;
	}

	public CDSpecialSlot getSlot() {
        return slot;
    }

    public ItemStack getItemInSlot() {
		return ArzioLib.getInstance().getPlayerDataHandler().getPlayerData(player).getInventory().getStackInSpecialSlot(this.getSlot());
	}
    
    public ItemStack getHeldItem() {
        return this.getPlayer().getItemInHand();
    }
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
