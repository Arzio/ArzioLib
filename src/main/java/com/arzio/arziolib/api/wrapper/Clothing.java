package com.arzio.arziolib.api.wrapper;

import com.arzio.arziolib.api.util.CDProtectionLevel;

public interface Clothing extends CDItem {
	
	public CDProtectionLevel getProtectionLevel();
	
	public void setProtectionLevel(CDProtectionLevel level);

}