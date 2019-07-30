package com.arzio.arziolib.api.wrapper.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.arzio.arziolib.api.exception.CDAReflectionException;
import com.arzio.arziolib.api.util.CDLootType;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.Loot;
import com.arzio.arziolib.api.wrapper.LootProvider;

public class LootProviderImpl implements LootProvider{

	private Map<CDLootType, Loot> lootMap = new ConcurrentHashMap<CDLootType, Loot>();
	
	@Override
	public Loot getLoot(CDLootType type) {
		Loot loot = lootMap.get(type);
		
		if (loot == null) {
			
			Object instance = null;
			
			switch (type) {
				case MEDICAL:
					instance = CDClasses.lootManagerLootMedicalInstance.getValue(null);
					break;
				case MILITARY:
					instance = CDClasses.lootManagerLootMilitaryInstance.getValue(null);
					break;
				case POLICE:
					instance = CDClasses.lootManagerLootPoliceInstance.getValue(null);
					break;
				case RESIDENTIAL:
					instance = CDClasses.lootManagerLootResidentialInstance.getValue(null);
					break;
				case RESIDENTIAL_RARE:
					instance = CDClasses.lootManagerLootResidentialRareInstance.getValue(null);
					break;
			}
			
			if (instance == null) {
				throw new CDAReflectionException("Loot instance not found!");
			}
			
			loot = new LootImpl(instance, type);
			lootMap.put(type, loot);
		}
		
		return loot;
	}

}
