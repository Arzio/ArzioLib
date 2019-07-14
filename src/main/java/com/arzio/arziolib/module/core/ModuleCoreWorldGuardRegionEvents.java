package com.arzio.arziolib.module.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.arzio.arziolib.api.event.RegionBorderEvent;
import com.arzio.arziolib.api.event.RegionBorderEvent.CrossType;
import com.arzio.arziolib.api.region.Flags;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

@RegisterModule(name = "core-worldguard-region-events")
public class ModuleCoreWorldGuardRegionEvents extends Module{

	private Map<Player, Set<ProtectedRegion>> playerRegions = new HashMap<>();
	
	public Set<ProtectedRegion> getRegionsAtPlayer(Player player){
	    Set<ProtectedRegion> regions = this.playerRegions.get(player);
	    
	    if (regions == null) {
	        regions = new HashSet<>();
	        this.playerRegions.put(player, regions);
	    }
	    
	    return regions;
	}
	
	private void handleMovement(PlayerMoveEvent event) {
	    Location to = event.getTo();
	    
	    Set<ProtectedRegion> currentPlayerRegions = this.getRegionsAtPlayer(event.getPlayer());
	    Set<ProtectedRegion> futureRegions = new HashSet<>();
	    
	    // Detecting Enter event
        for (ProtectedRegion futureRegion : Flags.getRegionSet(to)) {
            futureRegions.add(futureRegion);
            
            if (!currentPlayerRegions.contains(futureRegion)) {
                currentPlayerRegions.add(futureRegion);
                
                RegionBorderEvent borderEvent = new RegionBorderEvent(futureRegion, CrossType.ENTER, event.getPlayer(), event.getFrom(), to);
                Bukkit.getPluginManager().callEvent(borderEvent);
            }
        }
	    
        // Detecting Leave event
        Iterator<ProtectedRegion> itCurrentPlayerRegions = currentPlayerRegions.iterator();
        while (itCurrentPlayerRegions.hasNext()) {
            ProtectedRegion currentRegion = itCurrentPlayerRegions.next();
            
            if (!futureRegions.contains(currentRegion)) {
                itCurrentPlayerRegions.remove();
                
                RegionBorderEvent borderEvent = new RegionBorderEvent(currentRegion, CrossType.LEAVE, event.getPlayer(), event.getFrom(), to);
                Bukkit.getPluginManager().callEvent(borderEvent);
            }
        }
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onMove(PlayerMoveEvent event) {
	    this.handleMovement(event);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event) {
        this.handleMovement(event);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPortal(PlayerPortalEvent event) {
        this.handleMovement(event);
    }
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
	    // This needs to happen. Otherwise, it will cause a memory leak.
	    this.playerRegions.remove(event.getPlayer());
	}
	
    @Override
    public void onEnable() {
        super.onEnable();
        
        this.playerRegions.clear();
    }
	
	@Override
	public void onDisable() {
	    super.onDisable();
	    
	    this.playerRegions.clear();
	}

}
