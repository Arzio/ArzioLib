package com.arzio.arziolib.api.util;

import java.util.HashMap;
import java.util.Map;

public class CDProtectionLevel {

	/** Cache for protection levels. We will use only one instance per protection level. */
	private static Map<Integer, CDProtectionLevel> PROTECTION_CACHE = new HashMap<>();
	public static final CDProtectionLevel NONE = getFromLevel(0);
	public static final CDProtectionLevel LOW = getFromLevel(1);
	public static final CDProtectionLevel MEDIUM = getFromLevel(2);
	public static final CDProtectionLevel HIGH = getFromLevel(3);
	
	private final int protectionLevel;
	
	private CDProtectionLevel(int protectionLevel) {
		this.protectionLevel = protectionLevel;
	}
	
	public int getLevel() {
		return this.protectionLevel;
	}
	
	public static CDProtectionLevel getFromLevel(int level) {
		// Level below zero means NONE protection.
		if (level < 0) {
			level = 0;
		}
		
		CDProtectionLevel protection = PROTECTION_CACHE.get(level);
		
		// Could not find the protection level from the cache, so we need
		// to create a new instance and put it on the cache
		if (protection == null) {
			protection = new CDProtectionLevel(level);
			PROTECTION_CACHE.put(level, protection);
		}
		
		return protection;
	}
}
