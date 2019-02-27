package com.arzio.arziolib.api.event.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDPacketDataWrapper;
import com.arzio.arziolib.api.util.CDSpecialSlot;
import com.arzio.arziolib.api.wrapper.Gun;

public class CDSwapGunEvent extends GunEvent{

	private static final HandlerList handlers = new HandlerList();

	public CDSwapGunEvent(Player player, Gun gun, CDPacketDataWrapper dataWrapper) {
		super(player, gun, dataWrapper);
	}

	public Gun getGunInMyBack() {
		ItemStack itemInBack = ArzioLib.getInstance().getPlayerDataHandler().getPlayerData(player).getInventory().getStackInSpecialSlot(CDSpecialSlot.GUN);
		return ArzioLib.getInstance().getItemProvider().getStackAs(Gun.class, itemInBack);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
