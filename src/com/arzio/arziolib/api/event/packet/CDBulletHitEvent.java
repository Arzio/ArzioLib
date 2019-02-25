package com.arzio.arziolib.api.event.packet;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.ItemStackHelper;
import com.arzio.arziolib.api.event.PostEvent;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.config.UserData;

public class CDBulletHitEvent extends GunEvent implements PostEvent {

	private static final HandlerList handlers = new HandlerList();
	private final HitType hitType;
	private Entity entityHit;
	private boolean isHeadshot;
	private Block blockHit;
	private Location exactHitLocation;
	private boolean spendAmmo = false;
	
	public CDBulletHitEvent(Player shooter, Gun gun, Entity entityHit, boolean isHeadshot, byte[] packetData) {
		super(shooter, gun, packetData);
		this.hitType = HitType.ENTITY;
		this.entityHit = entityHit;
		this.isHeadshot = isHeadshot;
		this.exactHitLocation = (entityHit instanceof LivingEntity && isHeadshot) ? ((LivingEntity) entityHit).getEyeLocation() : entityHit.getLocation();
	}
	
	public CDBulletHitEvent(Player shooter, Gun gun, Location exactHitLocation, Block blockHit, byte[] packetData) {
		super(shooter, gun, packetData);
		this.hitType = HitType.BLOCK;
		this.blockHit = blockHit;
		this.exactHitLocation = exactHitLocation;
	}

	public void setSpendAmmo(boolean willSpend) {
		this.spendAmmo = willSpend;
	}
	
	public boolean willSpendAmmo() {
		return this.spendAmmo;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public Location getExactHitLocation() {
		return this.exactHitLocation;
	}
	
	public Block getBlockHit() {
		return this.blockHit;
	}
	
	public Entity getEntityHit() {
		return entityHit;
	}
	
	public HitType getHitType() {
		return this.hitType;
	}
	
	public boolean isHeadshot() {
		return this.isHeadshot;
	}

	public static enum HitType {
		BLOCK,
		ENTITY;
	}

	@Override
	public void afterPost() {
		if (getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		
		if (!this.willSpendAmmo()) {
			UserData data = UserData.getFrom(getPlayer());
			if (!data.hasFlag(UserData.FLAG_GUN_TRIGGER_HAS_RECOVERED_AMMO_LAST_TIME)){
				if (!data.hasFlag(UserData.FLAG_GUN_BULLET_HIT_HAS_RECOVERED_AMMO_LAST_TIME)) {
					data.addFlag(UserData.FLAG_GUN_BULLET_HIT_HAS_RECOVERED_AMMO_LAST_TIME);
					
					ItemStackHelper stackHelper = ArzioLib.getInstance().getItemStackHelper();
					
					stackHelper.setAmmoInGun(getPlayer().getItemInHand(), 
						stackHelper.getAmmoInGun(getPlayer().getItemInHand()) + 1);
				}
			}
		}
	}
}
