package com.arzio.arziolib.api.region;

import org.bukkit.Location;

import com.sk89q.worldguard.protection.flags.Flag;

public class EasyFlag<T>{

	private Flag<T> flag;

	public EasyFlag(Flag<T> flag) {
	    this.setFlag(flag);
	}
	
    public void register() {
	    if (!this.isRegistered()) {
	        Flags.addCustomFlag(this.flag);
	    }
	}
	
	public boolean isRegistered() {
	    return Flags.isFlagRegistered(flag);
	}
	
	public Flag<T> getFlag() {
	    if (!this.isRegistered()) {
	        throw new IllegalStateException("The flag "+flag.getName()+" is not registered yet! Please, call register() during the plugin load phase!");
	    }
		return this.flag;
	}
	
	@SuppressWarnings("unchecked")
    public void setFlag(Flag<T> flag) {
	    if (Flags.isFlagRegistered(flag)) {
	        flag = (Flag<T>) Flags.getFlag(flag.getName());
	    }
        this.flag = flag;
    }

    public T getValue(Location location) {
	    return Flags.getFlagValue(flag, location);
	}
	
}
