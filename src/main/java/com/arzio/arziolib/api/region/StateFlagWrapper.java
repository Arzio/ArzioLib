package com.arzio.arziolib.api.region;

import org.bukkit.Location;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

public class StateFlagWrapper extends FlagWrapper<State>{

	public StateFlagWrapper(Flag<State> flag) {
		super(flag);
	}
	
	public boolean isAllowed(Location location) {
		return Flags.isFlagAllowed((StateFlag) flag, location);
	}
	
	public boolean isDenied(Location location) {
		return Flags.isFlagDenied((StateFlag) flag, location);
	}
}
