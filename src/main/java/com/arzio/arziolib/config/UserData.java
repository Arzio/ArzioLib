package com.arzio.arziolib.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.wrapper.Gun;

public class UserData {

	public static final int FLAG_GUN_TRIGGER_HAS_RECOVERED_AMMO_LAST_TIME = 1;
	public static final int FLAG_GUN_BULLET_HIT_HAS_RECOVERED_AMMO_LAST_TIME = 2;
	
	private int playerFlags;
	
	private String playerName;
	private File configFile;
	private FileConfiguration config;
	
	private Boolean hideParticles = false;
	private Boolean hideClothes = false;

	private float currentSoundLevel = 0F;
	private Map<Gun, Long> lastShootMap = new HashMap<>();
	
	public UserData(String playerName) {
		this.playerName = playerName;
		
		File userdataFolder = new File(ArzioLib.getInstance().getDataFolder(), "userdata");
		if (!userdataFolder.exists()) {
			userdataFolder.mkdirs();
		}
		
		this.configFile = new File(userdataFolder, this.playerName);
		this.config = YamlConfiguration.loadConfiguration(this.configFile);
	}
	
	public void loadData() {
		if (this.config.contains("hideClothes")) {
			this.hideClothes = this.config.getBoolean("hideClothes");
		}
		
		if (this.config.contains("hideParticles")) {
			this.hideParticles = this.config.getBoolean("hideParticles");
		}
		
	}
	
	public void saveData() {
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public float getCurrentSoundLevel() {
		return this.currentSoundLevel;
	}
	
	public void setCurrentSoundLevel(float value) {
		if (value < 0) {
			value = 0;
		}
		this.currentSoundLevel = value;
	}
	
	public void setClothesHidden(boolean state) {
		this.hideClothes = state;
		this.config.set("hideClothes", this.hideClothes);
		this.saveData();
	}
	
	public boolean isClothesHidden() {
		return this.hideClothes;
	}
	
	public void setParticlesHidden(boolean state) {
		this.hideParticles = state;
		this.config.set("hideParticles", this.hideParticles);
		this.saveData();
	}
	
	public boolean isParticlesHidden() {
		return this.hideParticles;
	}
	
	public long getNextCooldownTimestamp(Gun gun) {
		Long nextUsage = this.lastShootMap.get(gun);
		
		return nextUsage == null ? -1L : nextUsage;
	}
	
	public void removeFlag(int flag) {
		this.playerFlags ^= flag;
	}
	
	public void addFlag(int flag) {
		this.playerFlags |= flag;
	}
	
	public boolean hasFlag(int flag) {
		return (this.playerFlags & flag) != 0;
	}
	
	public void setNextCooldownTimestamp(Gun gun, Long nextTime) {
		this.lastShootMap.put(gun, nextTime);
	}
	
	public static UserData getFrom(Player player) {
		return getFrom(player.getName());
	}
	
	public static UserData getFrom(String playerName) {
		return UserDataProvider.getInstance().getUserData(playerName);
	}
	
}
