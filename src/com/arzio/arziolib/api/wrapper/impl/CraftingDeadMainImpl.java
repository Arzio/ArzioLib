package com.arzio.arziolib.api.wrapper.impl;

import java.lang.reflect.Field;
import java.util.List;

import com.arzio.arziolib.api.exception.CDAReflectionException;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.CraftingDead;

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.registry.GameRegistry;

public class CraftingDeadMainImpl implements CraftingDead{

	@Override
	public IPlayerTracker getPlayerTracker() {
		return (IPlayerTracker) CDClasses.craftingDeadMainCommonPlayerTrackerField.getValue(CDClasses.craftingDeadMainInstanceField.getValue(null));
	}
	
	@Override
	public void setTrackerEnabled(boolean enabled) {
		if (enabled) {
			if (!this.isTrackerRegistered()) {
				GameRegistry.registerPlayerTracker(this.getPlayerTracker());
			}
		} else {
			this.getForgeTrackers().remove(this.getPlayerTracker());
		}
	}

	@Override
	public boolean isTrackerRegistered() {
		return this.getForgeTrackers().contains(this.getPlayerTracker());
	}

	@SuppressWarnings("unchecked")
	public List<IPlayerTracker> getForgeTrackers() {
		try {
			Field trackersField = GameRegistry.class.getDeclaredField("playerTrackers");
			trackersField.setAccessible(true);
			return (List<IPlayerTracker>) trackersField.get(null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new CDAReflectionException(e);
		}
	}


}
