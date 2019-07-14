package com.arzio.arziolib.api.region;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.World;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.reflection.ReflectionHelper;
import com.mewin.WGCustomFlags.FlagManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class Flags {
    
    public static WorldGuardPlugin getWorldGuard() {
        return ArzioLib.getInstance().getWorldGuard();
    }
    
    public static RegionManager getRegionManager(World world) {
        return getWorldGuard().getRegionManager(world);
    }

    public static ApplicableRegionSet getRegionSet(Location location) {
        return getRegionManager(location.getWorld()).getApplicableRegions(location);
    }
    
	public static boolean isFlagAllowed(StateFlag flag, Location location) {
		return isFlagInState(flag, location, State.ALLOW);
	}
	
    public static boolean isFlagAllowed(StateFlag flag, ApplicableRegionSet regionSet) {
        return isFlagInState(flag, regionSet, State.ALLOW);
    }
	
	public static boolean isFlagDenied(StateFlag flag, Location location) {
		return isFlagInState(flag, location, State.DENY);
	}
	
    public static boolean isFlagDenied(StateFlag flag, ApplicableRegionSet regionSet) {
        return isFlagInState(flag, regionSet, State.DENY);
    }
	
	public static <T> T getFlagValue(Flag<T> flag, Location location) {
	    ApplicableRegionSet set = getRegionSet(location);
		return set == null ? null : set.getFlag(flag);
	}
	
    public static boolean isFlagInState(StateFlag flag, ApplicableRegionSet regionSet, State state) {
        State currentState = regionSet.getFlag(flag);
        return currentState == null ? false : currentState == state;
    }
	
	public static boolean isFlagInState(StateFlag flag, Location location, State state) {
		ApplicableRegionSet regionSet = getRegionSet(location);
		return regionSet == null ? false : isFlagInState(flag, regionSet, state);
	}
	
    public static boolean flagExists(Flag<?> flag, Location location) {
        return getFlagValue(flag, location) != null;
    }

	public static boolean canRegionHavePvP(Location location) {

		if (isFlagDenied(DefaultFlag.PVP, location)) {
			return false;
		}

		if (isFlagAllowed(DefaultFlag.INVINCIBILITY, location)) {
			return false;
		}
		
		if (!location.getWorld().getPVP()) {
		    return false;
		}

		return true;
	}
	
	public static boolean isFlagRegistered(Flag<?> flag) {
	    return getFlag(flag.getName()) != null;
	}
	
    public static Flag<?> getFlag(String flagName) {
        for (Flag<?> f : DefaultFlag.flagsList) {
            if (flagName.equalsIgnoreCase(f.getName())) {
                return f;
            }
        }
        return null;
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
