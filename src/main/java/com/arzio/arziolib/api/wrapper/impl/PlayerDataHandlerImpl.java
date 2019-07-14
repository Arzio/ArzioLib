package com.arzio.arziolib.api.wrapper.impl;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.arzio.arziolib.api.wrapper.PlayerData;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;

public class PlayerDataHandlerImpl implements PlayerDataHandler {

	private Map<String, PlayerData> playerDataMap = new HashMap<>();
	
	@Override
	public PlayerData getPlayerData(Player player) {
		PlayerData found = playerDataMap.get(player.getName());
		
        if (found == null) {
            found = new PlayerDataImpl(player);
            playerDataMap.put(player.getName(), found);
        }
		
		return found;
	}

}
