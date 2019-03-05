package com.arzio.arziolib.api.wrapper.impl;

import com.arzio.arziolib.api.util.CauldronUtils;
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
			CauldronUtils.getForgePlayerTrackers().remove(this.getPlayerTracker());
		}
	}

	@Override
	public boolean isTrackerRegistered() {
		return CauldronUtils.getForgePlayerTrackers().contains(this.getPlayerTracker());
	}


}
