package com.arzio.arziolib.api.region;

import org.bukkit.Location;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

public class EasyStateFlag extends EasyFlag<State>{
	
	public EasyStateFlag(Flag<State> flag) {
		super(flag);
	}
	
	public EasyStateFlag(String flagName) {
		this(new StateFlag(flagName, false));
	}
	
	public boolean isAllowed(ApplicableRegionSet regionSet) {
		return Flags.isFlagAllowed((StateFlag) this.getFlag(), regionSet);
	}
	
	public boolean isAllowed(Location location) {
		return Flags.isFlagAllowed((StateFlag) this.getFlag(), location);
	}
	
	public boolean isDenied(ApplicableRegionSet regionSet) {
		return Flags.isFlagDenied((StateFlag) this.getFlag(), regionSet);
	}
	
	public boolean isDenied(Location location) {
		return Flags.isFlagDenied((StateFlag) this.getFlag(), location);
	}
}
