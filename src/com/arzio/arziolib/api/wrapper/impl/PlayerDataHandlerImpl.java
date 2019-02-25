package com.arzio.arziolib.api.wrapper.impl;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

import com.arzio.arziolib.api.wrapper.PlayerData;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;

public class PlayerDataHandlerImpl implements PlayerDataHandler {

	private List<PlayerData> playerDataList = new LinkedList<>();
	
	@Override
	public PlayerData getPlayerData(Player player) {
		PlayerData found = null;
		
		for (PlayerData data : playerDataList) {
			if (data.getPlayerName().equals(player.getName())) {
				found = data;
			}
		}
		
		if (found == null) {
			found = new PlayerDataImpl(player);
			playerDataList.add(found);
		}
		
		return found;
	}

}
