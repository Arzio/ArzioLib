package com.arzio.arziolib.api.util;

import com.arzio.arziolib.api.util.reflection.ReflectedClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.*;

import com.arzio.arziolib.api.util.reflection.CDClasses;

public enum CDPacketType {

	GUN_RELOAD(packetGunReloadClass),
	BASE_DESTROY(packetBaseBaseRemovalClass),
	SHOW_PARTICLE(packetBulletCollisionClientClass),
	THROW_GRENADE(packetGrenadeThrowingClass),
	SWAP_GUN(packetSwitchItemClass),
	GUN_TRIGGER(packetGunTriggerClass),
	GUN_BULLET_HIT(packetBulletCollisionClass),
	PLAYER_DATA(packetPlayerDataToPlayerClass),
	FLAMETHROWER_TRIGGER(packetFTTriggerClass),
	COMBATLOG_SHOW_TIMER(packetACLCombatClass),
	SYNC_ITEMS(packetSyncItemsClass),
	NAMETAG_VISIBILITY(packetNametagVisibilityClass),
	UNDEFINED(null);
	
	private int id;
	
	CDPacketType(ReflectedClass reflectedClass){
		if (reflectedClass != null) {
			this.id = (int) CDClasses.networkManagerGetPacketIdFromClass.invoke(null, reflectedClass.getReferencedClass());
		}
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public static CDPacketType getById(int id) {
		for (CDPacketType type : CDPacketType.values()) {
			if (type.getId() == id) {
				return type;
			}
		}
		return UNDEFINED;
	}
}
