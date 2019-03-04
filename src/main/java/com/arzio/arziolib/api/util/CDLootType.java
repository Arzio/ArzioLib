package com.arzio.arziolib.api.util;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.wrapper.Loot;

public enum CDLootType {

	RESIDENTIAL("residential"),
	RESIDENTIAL_RARE("residentialrare"),
	MEDICAL("medical"),
	MILITARY("military"),
	POLICE("police");
	
	private String id;
	
	private CDLootType(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public Loot getLoot() {
		return ArzioLib.getInstance().getLootProvider().getLoot(this);
	}

}
