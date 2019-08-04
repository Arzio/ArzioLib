package com.arzio.arziolib.api.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.arzio.arziolib.api.wrapper.PlayerData;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;

public class PlayerDataHandlerImpl implements PlayerDataHandler, Listener {

	private Map<Player, PlayerDataImpl> playerDataMap = new ConcurrentHashMap<>();
	
	public PlayerDataHandlerImpl(Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@Override
	public PlayerData getPlayerData(Player player) {
		Validate.notNull(player, "Player must not be null!");
		PlayerDataImpl found = playerDataMap.get(player);
		
		if (found == null) {
			found = new PlayerDataImpl(player);
			playerDataMap.put(player, found);
		}
		
		return found;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event){
		playerDataMap.remove(event.getPlayer());
	}

}
