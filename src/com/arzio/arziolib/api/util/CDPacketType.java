package com.arzio.arziolib.api.util;

import com.arzio.arziolib.api.util.reflection.ReflectedClass;
import static com.arzio.arziolib.api.util.reflection.CDClasses.*;

import com.arzio.arziolib.api.util.reflection.CDClasses;

public enum CDPacketType {

	GUN_RELOAD(packetGunReload),
	BASE_DESTROY(packetBaseBaseRemovalClass),
	SHOW_PARTICLE(packetBulletCollisionClient),
	THROW_GRENADE(packetGrenadeThrowing),
	SWAP_GUN(packetSwitchItem),
	GUN_TRIGGER(packetGunTrigger),
	GUN_BULLET_HIT(packetBulletCollision),
	PLAYER_DATA(packetPlayerDataToPlayer),
	FLAMETHROWER_TRIGGER(packetFTTrigger),
	COMBATLOG_SHOW_TIMER(packetACLCombat),
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
