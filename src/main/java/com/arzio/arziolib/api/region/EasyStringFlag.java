package com.arzio.arziolib.api.region;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StringFlag;

public class EasyStringFlag extends EasyFlag<String>{
    
	public EasyStringFlag(Flag<String> flag) {
		super(flag);
	}
	
    public EasyStringFlag(String flagName) {
        this(new StringFlag(flagName));
	}

}
