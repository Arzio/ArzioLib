package com.arzio.arziolib.module.addon;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.CDDefineNametagsEvent;
import com.arzio.arziolib.api.event.CDPlayerPreDropEvent;
import com.arzio.arziolib.api.event.RegionBorderEvent;
import com.arzio.arziolib.api.event.RegionBorderEvent.CrossType;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent.HitType;
import com.arzio.arziolib.api.event.packet.CDFlamethrowerTriggerEvent;
import com.arzio.arziolib.api.event.packet.CDGrenadeThrowEvent;
import com.arzio.arziolib.api.event.packet.CDGunReloadEvent;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.api.region.EasyStateFlag;
import com.arzio.arziolib.api.region.EasyStringFlag;
import com.arzio.arziolib.api.region.Flags;
import com.arzio.arziolib.api.util.CDDamageCause;
import com.arzio.arziolib.api.util.CDInventoryType;
import com.arzio.arziolib.api.util.CDMaterial;
import com.arzio.arziolib.api.util.CDPotionEffectType;
import com.arzio.arziolib.api.wrapper.PlayerData;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;
import com.arzio.arziolib.config.UserData;
import com.arzio.arziolib.config.UserDataProvider;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;
import com.arzio.arziolib.module.RepeatingTask;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

@RegisterModule(name = "addon-custom-flags")
public class ModuleAddonCustomFlags extends Module {

	public static final EasyStateFlag INFITE_AMMO_FLAG = new EasyStateFlag("infinite-ammo");
	public static final EasyStateFlag AMMO_RECOVERY_FLAG = new EasyStateFlag("ammo-recovery");
	public static final EasyStateFlag HEADSHOT_ONLY_FLAG = new EasyStateFlag("headshot-only");
	public static final EasyStateFlag KEEP_INVENTORY_FLAG = new EasyStateFlag("keep-inventory");
	public static final EasyStateFlag REPAIR_LEG_FLAG = new EasyStateFlag("repair-leg");
	public static final EasyStateFlag ZERO_DAMAGE_FLAG = new EasyStateFlag("zero-damage");
	public static final EasyStateFlag HEADSHOT_HITKILL_FLAG = new EasyStateFlag("headshot-hitkill");
	public static final EasyStateFlag GRENADE_THROWING_FLAG = new EasyStateFlag("grenade-throwing");
	public static final EasyStateFlag SHOW_NAMETAGS_FLAG = new EasyStateFlag("show-nametags");
	public static final EasyStateFlag DISABLE_NAMETAG_WHITELIST = new EasyStateFlag("disable-nametag-whitelist");
	public static final EasyStateFlag CAN_FIRE_GUNS_FLAG = new EasyStateFlag("can-fire-guns");
	public static final EasyStateFlag OPEN_ITEM_INVENTORY_FLAG = new EasyStateFlag("open-item-inventory");
	public static final EasyStateFlag HEAL_BLEEDING_FLAG = new EasyStateFlag("heal-bleeding");
	public static final EasyStateFlag HEAL_INFECTION_FLAG = new EasyStateFlag("heal-infection");
	public static final EasyStateFlag HEAL_THIRST_FLAG = new EasyStateFlag("heal-thirst");
	public static final EasyStateFlag OLD_CD_BUILD_FLAG = new EasyStateFlag("cd-build");
	public static final EasyStateFlag FLY_FLAG = new EasyStateFlag("fly");
	public static final EasyStateFlag CAN_RELOAD_GUNS_FLAG = new EasyStateFlag("can-reload-guns");
    public static final EasyStateFlag OPEN_CD_INVENTORY_FLAG = new EasyStateFlag("open-cd-inventory");
    public static final EasyStateFlag CLEAR_CD_INVENTORY_FLAG = new EasyStateFlag("clear-cd-inventory");
    public static final EasyStateFlag CLEAR_MINECRAFT_INVENTORY_FLAG = new EasyStateFlag("clear-minecraft-inventory");
    public static final EasyStateFlag TASER_FLAG = new EasyStateFlag("taser");
	public static final EasyStateFlag HANDCUFFS_FLAG = new EasyStateFlag("handcuffs");
	public static final EasyStringFlag PLAYER_ENTER_COMMAND_FLAG = new EasyStringFlag("player-enter-command");
	public static final EasyStringFlag PLAYER_LEAVE_COMMAND_FLAG = new EasyStringFlag("player-leave-command");
	public static final EasyStringFlag SERVER_ENTER_COMMAND_FLAG = new EasyStringFlag("server-enter-command");
	public static final EasyStringFlag SERVER_LEAVE_COMMAND_FLAG = new EasyStringFlag("server-leave-command");
    
	
	private final PlayerDataHandler playerDataHandler;
	private final UserDataProvider userDataProvider;
	
	public ModuleAddonCustomFlags() {
	    this.playerDataHandler = ArzioLib.getInstance().getPlayerDataHandler();
		this.userDataProvider = ArzioLib.getInstance().getUserDataProvider();
	}
	
	@Override
	public void onEnable() {
		INFITE_AMMO_FLAG.register();
		AMMO_RECOVERY_FLAG.register();
		HEADSHOT_ONLY_FLAG.register();
		KEEP_INVENTORY_FLAG.register();
		REPAIR_LEG_FLAG.register();
		ZERO_DAMAGE_FLAG.register();
		HEADSHOT_HITKILL_FLAG.register();
		GRENADE_THROWING_FLAG.register();
		SHOW_NAMETAGS_FLAG.register();
		DISABLE_NAMETAG_WHITELIST.register();
		CAN_FIRE_GUNS_FLAG.register();
		OPEN_ITEM_INVENTORY_FLAG.register();
		HEAL_BLEEDING_FLAG.register();
		HEAL_INFECTION_FLAG.register();
		HEAL_THIRST_FLAG.register();
		OLD_CD_BUILD_FLAG.register();
		FLY_FLAG.register();
		CAN_RELOAD_GUNS_FLAG.register();
		OPEN_CD_INVENTORY_FLAG.register();
		CLEAR_CD_INVENTORY_FLAG.register();
		CLEAR_MINECRAFT_INVENTORY_FLAG.register();
		TASER_FLAG.register();
		HANDCUFFS_FLAG.register();
		PLAYER_ENTER_COMMAND_FLAG.register();
		PLAYER_LEAVE_COMMAND_FLAG.register();
		SERVER_ENTER_COMMAND_FLAG.register();
		SERVER_LEAVE_COMMAND_FLAG.register();
	}
	
	@RepeatingTask(delay = 20L, period = 20L)
	public void healThirst() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (HEAL_THIRST_FLAG.isAllowed(player.getLocation())) {
                PlayerData data = playerDataHandler.getPlayerData(player);
                data.setWaterLevel(data.getMaxWaterLevel());
            }
        }
	}
	
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableTaserTrigger(CDGunTriggerEvent event) {
        if (CDMaterial.TASER.isTypeOf(event.getHeldGun()) && TASER_FLAG.isDenied(event.getPlayer().getLocation())){
            event.getPlayer().sendMessage("§cYou cannot use Taser here!");
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableTaserHit(CDBulletHitEvent event) {
        if (CDMaterial.TASER.isTypeOf(event.getHeldGun()) && TASER_FLAG.isDenied(event.getPlayer().getLocation())){
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void disableHandcuffs(PlayerInteractEntityEvent event) {
        // Continue only if the clicked entity is a player
        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }
        
        ItemStack stack = event.getPlayer().getItemInHand();
        
        // Check if the clicker is holding any item
        if (stack == null) {
            return;
        }
        
        if (CDMaterial.HANDCUFFS.isTypeOf(stack)) {
            if (HANDCUFFS_FLAG.isDenied(event.getPlayer().getLocation()) || HANDCUFFS_FLAG.isDenied(event.getRightClicked().getLocation())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cYou cannot use handcuffs here!");
            }
        }
    }
	
    @EventHandler
    public void cdBuildHanging(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            Player player = (Player) event.getRemover();
            
            if (player.isOp()) {
                return;
            }
            if (OLD_CD_BUILD_FLAG.isAllowed(player.getLocation())) {
                event.setCancelled(true);
            }
        }
    }
	
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCDInventory(InventoryOpenEvent event) {
        
        HumanEntity player = event.getPlayer();
        
        if (CDInventoryType.PLAYER_INVENTORY.isTypeOf(event.getView())) {
            if (OPEN_CD_INVENTORY_FLAG.isDenied(event.getPlayer().getLocation())) {
                event.setCancelled(true);
                
                if (player instanceof Player) {
                    ((Player) player).sendMessage("§cYou cannot open your inventory here.");
                }
            }
        }
    }
	
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            return;
        }
        
        Block block = event.getBlock();       
        
        if (OLD_CD_BUILD_FLAG.isAllowed(block.getLocation())) {

            if (player.getGameMode() != GameMode.CREATIVE) {
                ItemStack heldStack = player.getItemInHand();
                if (heldStack != null) {
                    CDMaterial material = CDMaterial.getFrom(heldStack.getType());
                    if (material != null && material.getHarvestType().canHarvest(block)) {
                        return; // do not cancel the event
                    }
                }
            }
            
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        if (event.getPlayer().isOp()) {
            return;
        }
        if (OLD_CD_BUILD_FLAG.isAllowed(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onGunReload(CDGunReloadEvent event) {
        if (CAN_RELOAD_GUNS_FLAG.isDenied(event.getPlayer().getLocation())) {
            event.getPlayer().sendMessage("§cYou cannot reload guns here.");
            event.setCancelled(true);
        }
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
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void toggleFlightEvent(PlayerToggleFlightEvent event) {
	    Player player = event.getPlayer();
	    
	    if (event.isFlying() && !player.isOp()) {
	        if (FLY_FLAG.isDenied(player.getLocation())) {
	            player.setAllowFlight(false);
                player.teleport(player.getLocation()); // fixes ping desync
                player.sendMessage("§cFly mode is disabled here.");
                event.setCancelled(true);
	        }
	    }
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void handleServerCommandFlag(RegionBorderEvent event){
		String commandToExecute = (event.getType() == CrossType.ENTER) ?
		 	SERVER_ENTER_COMMAND_FLAG.getValue(event.getRegion()) : SERVER_LEAVE_COMMAND_FLAG.getValue(event.getRegion());

		if (commandToExecute != null){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandToExecute.replace("/", "").replace("%player%", event.getPlayer().getName()));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void handlePlayerCommandFlag(RegionBorderEvent event){
		String commandToExecute = (event.getType() == CrossType.ENTER) ? 
			PLAYER_ENTER_COMMAND_FLAG.getValue(event.getRegion()) : PLAYER_LEAVE_COMMAND_FLAG.getValue(event.getRegion());

		if (commandToExecute != null){
			Bukkit.dispatchCommand(event.getPlayer(), commandToExecute.replace("/", ""));
		}
	}
	
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void handleFlyFlag(RegionBorderEvent event) {
        Player player = event.getPlayer();
        Location destination = event.getFutureLocation();
        
        if (FLY_FLAG.isAllowed(destination)) {
            if (!player.getAllowFlight()) {
                UserData data = UserData.getFrom(player);
                data.addFlag(UserData.FLAG_EARNED_FLY_MODE_DUE_TO_WG_FLAG);
                
                player.setAllowFlight(true);
                player.sendMessage("§6Fly mode is enabled here.");
            }
        } else {
            if (player.getAllowFlight()) {
                UserData data = UserData.getFrom(player);
                if (data.hasFlag(UserData.FLAG_EARNED_FLY_MODE_DUE_TO_WG_FLAG)) {
                    data.removeFlag(UserData.FLAG_EARNED_FLY_MODE_DUE_TO_WG_FLAG);
                    
                    player.setAllowFlight(false);
                    player.sendMessage("§6Fly mode is disabled here.");
                }
            }
        }
    }
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
	public void disableWhitelist(CDDefineNametagsEvent event) {
	    if (DISABLE_NAMETAG_WHITELIST.isAllowed(event.getPlayer().getLocation())) {
	        event.clearWhitelist();
	    }
	}
	
    @EventHandler
    public void preventNametagFlagCaching(PlayerQuitEvent event) {
        UserData data = UserData.getFrom(event.getPlayer());
        
        data.removeFlag(UserData.FLAG_SHOW_NAMETAGS);
        data.removeFlag(UserData.FLAG_WHITELIST_DISABLED);
    }
	
    @EventHandler
    public void resendNametagsOnJoin(PlayerJoinEvent event) {
        PlayerData data = playerDataHandler.getPlayerData(event.getPlayer());
        data.resendViewableNametags();
    }
    
    @EventHandler(ignoreCancelled = true)
    public void hideNameTag(RegionBorderEvent event) {
        Player player = event.getPlayer();
        Location destination = event.getFutureLocation();
        ApplicableRegionSet regionSet = Flags.getRegionSet(destination);

        if (regionSet != null) {
            UserData userData = userDataProvider.getUserData(player);

            boolean areNametagsVisibleHere = SHOW_NAMETAGS_FLAG.isAllowed(regionSet);
            boolean areNametagsShownForThePlayer = userData.hasFlag(UserData.FLAG_SHOW_NAMETAGS);
            boolean shouldUpdateNametags = false;

            if (areNametagsVisibleHere) {
                if (!areNametagsShownForThePlayer) {
                    userData.addFlag(UserData.FLAG_SHOW_NAMETAGS);
                    shouldUpdateNametags = true;
                }
            } else {
                boolean isWhitelistDisabledHere = DISABLE_NAMETAG_WHITELIST.isAllowed(regionSet);
                boolean playerHasWhitelist = !userData.hasFlag(UserData.FLAG_WHITELIST_DISABLED);

                if (areNametagsShownForThePlayer) {
                    userData.removeFlag(UserData.FLAG_SHOW_NAMETAGS);
                    shouldUpdateNametags = true;
                }
                if (isWhitelistDisabledHere && playerHasWhitelist) {
                    userData.addFlag(UserData.FLAG_WHITELIST_DISABLED);
                    shouldUpdateNametags = true;
                }
                if (!isWhitelistDisabledHere && !playerHasWhitelist) {
                    userData.removeFlag(UserData.FLAG_WHITELIST_DISABLED);
                    shouldUpdateNametags = true;
                }
            }

            if (shouldUpdateNametags) {
                playerDataHandler.getPlayerData(player)
                        .setNametagsHidden(!areNametagsVisibleHere);
            }
        }
    }
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void flagGrenadeThrowing(CDGrenadeThrowEvent event) {
		if (GRENADE_THROWING_FLAG.isDenied(event.getPlayer().getLocation())) {
			event.getPlayer().sendMessage("§cYou cannot throw grenades here.");
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void clearItems(RegionBorderEvent event) {
	    Player player = event.getPlayer();
	    
	    if (CLEAR_CD_INVENTORY_FLAG.isAllowed(event.getFutureLocation())) {
	        PlayerData data = playerDataHandler.getPlayerData(player);
	        data.getInventory().clearSpecialSlots();
	    }
        if (CLEAR_MINECRAFT_INVENTORY_FLAG.isAllowed(event.getFutureLocation())) {
            player.getInventory().clear();
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
	public void onMovement(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.hasPotionEffect(CDPotionEffectType.BROKEN_LEG)) {
			if (REPAIR_LEG_FLAG.isAllowed(event.getTo())) {
				player.removePotionEffect(CDPotionEffectType.BROKEN_LEG);
				player.removePotionEffect(PotionEffectType.BLINDNESS);
			}
		}
		if (player.hasPotionEffect(CDPotionEffectType.BLEEDING)) {
			if (HEAL_BLEEDING_FLAG.isAllowed(event.getTo())) {
				player.removePotionEffect(CDPotionEffectType.BLEEDING);
			}
		}
		if (player.hasPotionEffect(CDPotionEffectType.INFECTION)) {
			if (HEAL_INFECTION_FLAG.isAllowed(event.getTo())) {
				player.removePotionEffect(CDPotionEffectType.INFECTION);
			}
		}
	}

}
