package com.arzio.arziolib.api.region;

import java.lang.reflect.Field;

import org.bukkit.Location;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.reflection.ReflectionHelper;
import com.mewin.WGCustomFlags.FlagManager;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

public class Flags {

	public static boolean isFlagAllowed(StateFlag flag, Location location) {
		return isFlagInState(flag, location, State.ALLOW);
	}
	
	public static boolean isFlagDenied(StateFlag flag, Location location) {
		return isFlagInState(flag, location, State.DENY);
	}
	
	public static boolean isFlagInState(StateFlag flag, Location location, State state) {
		ApplicableRegionSet set = ArzioLib.getInstance().getWorldGuard().getRegionManager(location.getWorld())
				.getApplicableRegions(location);
		if (set != null) {
			return set.getFlag(flag) == state;
		}
		return false;
	}

	public static boolean canRegionHavePvP(Location location) {

		if (isFlagDenied(DefaultFlag.PVP, location)) {
			return false;
		}

		if (isFlagAllowed(DefaultFlag.INVINCIBILITY, location)) {
			return false;
		}

		return true;
	}

	public static void addCustomFlag(Flag<?> flag) {
		try {
			Field flagField = DefaultFlag.class.getField("flagsList");
			Flag<?>[] flags = new Flag[DefaultFlag.flagsList.length + 1];
			System.arraycopy(DefaultFlag.flagsList, 0, flags, 0, DefaultFlag.flagsList.length);
			flags[DefaultFlag.flagsList.length] = flag;
			ReflectionHelper.setFinalStatic(flagField, flags);
			
			FlagManager.customFlags.put(flag.getName(), flag);
			ArzioLib.getInstance().getWGCustomFlags().loadAllWorlds();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
