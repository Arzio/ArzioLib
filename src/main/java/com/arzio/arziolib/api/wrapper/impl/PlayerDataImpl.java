package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.arziolib.api.util.CDPacketHelper;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.InventoryCDA;
import com.arzio.arziolib.api.wrapper.PlayerData;

public class PlayerDataImpl implements PlayerData{

    public static final int MAX_NORMALIZED_WATER_LEVEL = 20;
	public static final int MAX_WATER_LEVEL = 36000;
	public static final int WATER_LEVEL_RATIO_DIFFERENCE = MAX_WATER_LEVEL / MAX_NORMALIZED_WATER_LEVEL;
	private String playerName;
	private final InventoryCDAImpl inventoryCDA;
	private boolean isNametagsHidden = true;
	
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
	public void setNametagsHidden(boolean isPossible) {
	    if (CDPacketHelper.sendNametagPacket(this.getPlayer(), isPossible)) {
	        this.isNametagsHidden = isPossible;
	    }
	}
	
    @Override
    public boolean isNametagsHidden() {
        return this.isNametagsHidden;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerDataImpl other = (PlayerDataImpl) obj;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		return true;
	}

    @Override
    public void resendViewableNametags() {
        this.setNametagsHidden(this.isNametagsHidden);
    }

    @Override
    public int getMaxWaterLevel() {
        return MAX_NORMALIZED_WATER_LEVEL;
    }

}
