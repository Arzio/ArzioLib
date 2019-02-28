package com.arzio.arziolib.module.addon;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.CDPlayerPreDropEvent;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent.HitType;
import com.arzio.arziolib.api.event.packet.CDFlamethrowerTriggerEvent;
import com.arzio.arziolib.api.event.packet.CDGrenadeThrowEvent;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.api.region.StateFlagWrapper;
import com.arzio.arziolib.api.util.CDDamageCause;
import com.arzio.arziolib.api.util.CDInventoryType;
import com.arzio.arziolib.api.util.CDPotionEffectType;
import com.arzio.arziolib.module.ListenerModule;
import com.craftingdead.server.API;
import com.sk89q.worldguard.protection.flags.StateFlag;

public class ModuleAddonCustomFlags extends ListenerModule{

	public static final StateFlagWrapper INFITE_AMMO_FLAG = new StateFlagWrapper(new StateFlag("infinite-ammo", false));
	public static final StateFlagWrapper AMMO_RECOVERY_FLAG = new StateFlagWrapper(new StateFlag("ammo-recovery", false));
	public static final StateFlagWrapper HEADSHOT_ONLY_FLAG = new StateFlagWrapper(new StateFlag("headshot-only", false));
	public static final StateFlagWrapper KEEP_INVENTORY_FLAG = new StateFlagWrapper(new StateFlag("keep-inventory", false));
	public static final StateFlagWrapper REPAIR_LEG_FLAG = new StateFlagWrapper(new StateFlag("repair-leg", false));
	public static final StateFlagWrapper ZERO_DAMAGE_FLAG = new StateFlagWrapper(new StateFlag("zero-damage", false));
	public static final StateFlagWrapper HEADSHOT_HITKILL_FLAG = new StateFlagWrapper(new StateFlag("headshot-hitkill", false));
	public static final StateFlagWrapper GRENADE_THROWING_FLAG = new StateFlagWrapper(new StateFlag("grenade-throwing", false));
	public static final StateFlagWrapper HIDE_NAMETAGS_FLAG = new StateFlagWrapper(new StateFlag("hide-nametags", false));
	public static final StateFlagWrapper CAN_FIRE_GUNS_FLAG = new StateFlagWrapper(new StateFlag("can-fire-guns", false));
	public static final StateFlagWrapper OPEN_ITEM_INVENTORY_FLAG = new StateFlagWrapper(new StateFlag("open-item-inventory", false));
	
	
	private Set<String> hiddenPlayers = new HashSet<>();
	
	public ModuleAddonCustomFlags(ArzioLib plugin) {
		super(plugin);
	}
	
	@Override
	protected void onEnable() {
		super.onEnable();
		INFITE_AMMO_FLAG.enable();
		AMMO_RECOVERY_FLAG.enable();
		HEADSHOT_ONLY_FLAG.enable();
		KEEP_INVENTORY_FLAG.enable();
		REPAIR_LEG_FLAG.enable();
		ZERO_DAMAGE_FLAG.enable();
		HEADSHOT_HITKILL_FLAG.enable();
		GRENADE_THROWING_FLAG.enable();
		HIDE_NAMETAGS_FLAG.enable();
		CAN_FIRE_GUNS_FLAG.enable();
		OPEN_ITEM_INVENTORY_FLAG.enable();
	}

	@Override
	protected void onDisable() {
		super.onDisable();
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onInventoryOpen(InventoryOpenEvent event) {
		
		HumanEntity player = event.getPlayer();
		
		if (OPEN_ITEM_INVENTORY_FLAG.isDenied(event.getPlayer().getLocation())) {
			
			if (CDInventoryType.isItemContainer(event.getView())) {
				event.setCancelled(true);
				
				if (player instanceof Player) {
					((Player) player).sendMessage("§cYou cannot open this inventory here.");
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void canFireNormalGunFlag(CDGunTriggerEvent event) {
		if (CAN_FIRE_GUNS_FLAG.isDenied(event.getPlayer().getLocation())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou cannot use firearms here.");
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void canFireNormalGunFlag(CDFlamethrowerTriggerEvent event) {
		if (CAN_FIRE_GUNS_FLAG.isDenied(event.getPlayer().getLocation())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cYou cannot use firearms here.");
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void hideNameTag(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		
		if (HIDE_NAMETAGS_FLAG.isAllowed(player.getLocation())) {
			if (hiddenPlayers.add(player.getName())) {
				API.setCanViewPlayerTag(player.getName(), false);
			}
		} else {
			if (hiddenPlayers.remove(player.getName())) {
				API.setCanViewPlayerTag(player.getName(), true);
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		hiddenPlayers.remove(event.getPlayer().getName());
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void flagGrenadeThrowing(CDGrenadeThrowEvent event) {
		if (GRENADE_THROWING_FLAG.isDenied(event.getPlayer().getLocation())) {
			event.getPlayer().sendMessage("§cYou cannot throw grenades here.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void flagBulletRecovery(CDBulletHitEvent event) {
		if (event.getHitType() == HitType.ENTITY) {
			if (AMMO_RECOVERY_FLAG.isAllowed(event.getPlayer().getLocation())) {
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.SUCCESSFUL_HIT, 2F, 1.1F);
				event.setSpendAmmo(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void flagInfiniteAmmo(CDGunTriggerEvent event) {
		if (INFITE_AMMO_FLAG.isAllowed(event.getPlayer().getLocation())) {
			event.setSpendAmmo(false);
		}
	}
	
	@EventHandler
	public void flagHeadshotOnly(EntityDamageByEntityEvent event) {
		
		if (event.getCause() == CDDamageCause.BULLET_BODY.asBukkitDamageCause()) {
			org.bukkit.entity.Entity entityHit = event.getEntity();
			
			if (entityHit instanceof Player) {
				if (HEADSHOT_ONLY_FLAG.isAllowed(entityHit.getLocation())) {
					event.setCancelled(true);
				}
			}
		}

	}
	
	@EventHandler
	public void flagZeroDamage(EntityDamageByEntityEvent event) {
		
		org.bukkit.entity.Entity entityHit = event.getEntity();
		
		if (entityHit instanceof Player) {
			if (ZERO_DAMAGE_FLAG.isAllowed(entityHit.getLocation())) {
				event.setDamage(0.001D);
			}
		}

	}
	
	@EventHandler
	public void flagHeadshotHitkill(EntityDamageByEntityEvent event) {
		
		if (event.getCause() == CDDamageCause.BULLET_HEADSHOT.asBukkitDamageCause()) {
			org.bukkit.entity.Entity entityHit = event.getEntity();
			
			if (entityHit instanceof Player) {
				if (HEADSHOT_HITKILL_FLAG.isAllowed(entityHit.getLocation())) {
					((Player) entityHit).setHealth(0D);
				}
			}
		}

	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void flagKeepInventory(CDPlayerPreDropEvent event) {
		if (KEEP_INVENTORY_FLAG.isAllowed(event.getPlayer().getLocation())) {
			event.setKeepInventory(true);
		}
	}
	
	@EventHandler
	public void repairLegOnFall(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)){
			return;
		}
		
		if (event.getCause() == DamageCause.FALL) {
			Player player = (Player) event.getEntity();
			if (player.hasPotionEffect(CDPotionEffectType.BROKEN_LEG)) {
				
				if (REPAIR_LEG_FLAG.isAllowed(player.getLocation())) {
					player.removePotionEffect(CDPotionEffectType.BROKEN_LEG);
					player.removePotionEffect(PotionEffectType.BLINDNESS);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void repairLegOnMove(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		if (player.hasPotionEffect(CDPotionEffectType.BROKEN_LEG)) {
			
			if (REPAIR_LEG_FLAG.isAllowed(player.getLocation())) {
				player.removePotionEffect(CDPotionEffectType.BROKEN_LEG);
				player.removePotionEffect(PotionEffectType.BLINDNESS);
			}
		}
	}



	@Override
	public String getName() {
		return "addon-custom-flags";
	}

}
