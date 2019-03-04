package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.InventoryCDA;
import com.arzio.arziolib.api.wrapper.PlayerData;
import com.craftingdead.server.API;

public class PlayerDataImpl implements PlayerData{

	public static final int MAX_WATER_LEVEL = 36000;
	public static final int WATER_LEVEL_RATIO_DIFFERENCE = MAX_WATER_LEVEL / 20;
	private String playerName;
	private final InventoryCDAImpl inventoryCDA;
	
	public PlayerDataImpl(Player player) {
		this(player.getName());
	}
	
	public PlayerDataImpl(String playerName) {
		this.playerName = playerName;
		this.inventoryCDA = new InventoryCDAImpl(this);
	}
	
	public Object getPlayerDataInstance() {
		return CDClasses.playerDataHandlerGetPlayerData.invoke(null, this.playerName);
	}
	
	@Override
	public String getPlayerName() {
		return playerName;
	}

	@Override
	public Player getPlayer() {
		return Bukkit.getPlayerExact(playerName);
	}

	@Override
	public int getBaseX() {
		return CDClasses.playerDataBaseXField.getValue(this.getPlayerDataInstance());
	}

	@Override
	public int getBaseY() {
		return CDClasses.playerDataBaseYField.getValue(this.getPlayerDataInstance());
	}

	@Override
	public int getBaseZ() {
		return CDClasses.playerDataBaseZField.getValue(this.getPlayerDataInstance());
	}

	@Override
	public void setCanViewNameTags(boolean isPossible) {
		API.setCanViewPlayerTag(this.playerName, isPossible);
	}

	@Override
	public boolean hasBase() {
		return this.getBaseLocation() != null;
	}

	@Override
	public Location getBaseLocation() {
		for (World world : Bukkit.getServer().getWorlds()) {
    		if (CDBaseMaterial.isCenter(world.getBlockAt(this.getBaseX(), this.getBaseY(), this.getBaseZ()))) {
    			return new Location(world, this.getBaseX(), this.getBaseY(), this.getBaseZ());
    		}
    	}
		return null;
	}

	@Override
	public InventoryCDA getInventory() {
		return inventoryCDA;
	}

	@Override
	public int getWaterLevel() {
		return CDClasses.waterLevelsValue.getValue(
				CDClasses.playerDataWaterLevels.getValue(this.getPlayerDataInstance())) / WATER_LEVEL_RATIO_DIFFERENCE;
	}

	@Override
	public void setWaterLevel(int amount) {
		int result = amount * WATER_LEVEL_RATIO_DIFFERENCE;
		if (result > MAX_WATER_LEVEL) {
			result = MAX_WATER_LEVEL;
		}
		if (result < 0) {
			result = 0;
		}
		
		CDClasses.waterLevelsValue.setValue(
			CDClasses.playerDataWaterLevels.getValue(this.getPlayerDataInstance()), result);
	}

}
