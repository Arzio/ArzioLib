package com.arzio.arziolib.api.event.packet;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.ItemStackHelper;
import com.arzio.arziolib.api.event.PostEvent;
import com.arzio.arziolib.api.util.CDPacketDataWrapper;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.config.UserData;

public class CDGunTriggerEvent extends GunEvent implements PostEvent{

	private static final HandlerList handlers = new HandlerList();
	private boolean spendAmmo = true;

	public CDGunTriggerEvent(Player player, Gun gun, CDPacketDataWrapper dataWrapper) {
		super(player, gun, dataWrapper);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public void setSpendAmmo(boolean willSpend) {
		this.spendAmmo = willSpend;
	}
	
	public boolean willSpendAmmo() {
		return this.spendAmmo;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public void afterPost() {
		if (getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		
		UserData data = UserData.getFrom(getPlayer());
		
		if (!this.willSpendAmmo()) {
			data.addFlag(UserData.FLAG_GUN_TRIGGER_HAS_RECOVERED_AMMO_LAST_TIME);
			
			ItemStackHelper stackHelper = ArzioLib.getInstance().getItemStackHelper();
			
			stackHelper.setAmmoInGun(getPlayer().getItemInHand(), 
					stackHelper.getAmmoInGun(getPlayer().getItemInHand()) + 1);
		} else {
			data.removeFlag(UserData.FLAG_GUN_TRIGGER_HAS_RECOVERED_AMMO_LAST_TIME);
			data.removeFlag(UserData.FLAG_GUN_BULLET_HIT_HAS_RECOVERED_AMMO_LAST_TIME);
		}
	}

}
