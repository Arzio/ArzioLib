package com.arzio.arziolib.api.wrapper.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.wrapper.PlayerData;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;

public class PlayerDataHandlerImpl implements PlayerDataHandler {

	private Map<String, PlayerData> playerDataMap = new HashMap<>();
	
	@Override
	public PlayerData getPlayerData(Player player) {
	    Validate.notNull(player, "Player must not be null!");
		PlayerData found = playerDataMap.get(player.getName());
		
        if (found == null) {
            found = new PlayerDataImpl(player);
            playerDataMap.put(player.getName(), found);
        }
		
		return found;
	}

}
