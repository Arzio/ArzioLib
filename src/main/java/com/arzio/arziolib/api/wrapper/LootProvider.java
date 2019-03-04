package com.arzio.arziolib.api.wrapper;

import com.arzio.arziolib.api.util.CDLootType;

public interface LootProvider {

	public Loot getLoot(CDLootType type);

}
