package com.arzio.arziolib.config;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.arzio.arziolib.ArzioLib;

public class UserDataProvider {

	private Map<String, UserData> dataInCache = new HashMap<String, UserData>();
	
	public UserData getUserData(Player player) {
		return this.getUserData(player.getName());
	}
	
	public UserData getUserData(String playerName) {
		UserData data = dataInCache.get(playerName);
		
		if (data == null) {
			data = new UserData(playerName);
			data.loadData();
			
			dataInCache.put(playerName, data);
		}
		
		return data;
	}
	
	public static UserDataProvider getInstance() {
		return ArzioLib.getInstance().getUserDataProvider();
	}
}
