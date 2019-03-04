package com.arzio.arziolib.api.region;

import com.sk89q.worldguard.protection.flags.Flag;

public class FlagWrapper<T>{

	protected final Flag<T> flag;
	private boolean isRegistered = false;
	
	public FlagWrapper(Flag<T> flag) {
		this.flag = flag;
	}
	
	public void enable() {
		if (this.isRegistered) {
			return;
		}
		
		this.isRegistered = true;
		Flags.addCustomFlag(flag);
	}
	
	public Flag<T> getFlag() {
		return this.flag;
	}
	
}
