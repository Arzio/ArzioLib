package com.arzio.arziolib.api.util;

import java.util.logging.Level;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.arzio.arziolib.ArzioLib;

/**
 * Enum for CD entity types.
 * @author BlackFairy
 *
 */
public enum CDEntityType {

	CD_ZOMBIE("cdzombie"),
	CD_ZOMBIE_FAST("cdzombiefast"),
	CD_ZOMBIE_TANK("cdzombietank"),
	CD_ZOMBIE_WEAK("cdzombieweak"),
	GRENADE("grenade"),
	GRENADE_FLASH("grenadeflash"),
	GRENADE_DECOY("grenadedecoy"),
	GRENADE_SMOKE("grenadesmoke"),
	GRENADE_FIRE("grenadefire"),
	GRENADE_PIPE_BOMB("grenadepipebomb"),
	GRENADE_GAS("grenadegas"),
	CORPSE("corpse"),
	GROUND_ITEM("grounditem"),
	HEAD("head"),
	C4("c4"),
	FLAME_THROWER_FIRE("flamethrowerfire"),
	SUPPLY_DROP("supplydrop");
	
	private final String name;
	private EntityType type;
	
	private CDEntityType(String name) {
		this.name = name;
		try {
			this.type = EntityType.valueOf(this.getBukkitName().toUpperCase());
		} catch (Exception e) {
			ArzioLib.getInstance().getLogger().log(Level.WARNING, "Bukkit entity type for the CD Entity '"+this.getName()+"' could not be found. This entity will not be detected properly by plugins.", e);
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getBukkitName() {
		return ArzioLib.MOD_ID+"_"+this.getName();
	}
	
	public EntityType asBukkitType() {
		return this.type;
	}
	
	public boolean isTypeOf(Entity entity) {
		return getTypeOf(entity) == this;
	}
	
	public static CDEntityType getTypeOf(Entity entity) {
        if (entity == null)
            return null;
        
        EntityType bukkitType = EntityUtil.getCompatibleEntityType(entity);
        
        if (bukkitType == null) {
        	return null;
        }
        
        for (CDEntityType type : CDEntityType.values()) {
        	if (type.asBukkitType() == bukkitType) {
        		return type;
        	}
        }
        return null;
    }
    
    public static CDEntityType[] getZombieTypes() {
    	return new CDEntityType[] { CD_ZOMBIE, CD_ZOMBIE_FAST, CD_ZOMBIE_TANK, CD_ZOMBIE_WEAK };
    }
    
    public static CDEntityType[] getGrenadeTypes() {
    	return new CDEntityType[] { GRENADE, GRENADE_DECOY, GRENADE_FIRE, GRENADE_FLASH, GRENADE_GAS, GRENADE_PIPE_BOMB, GRENADE_SMOKE };
    }
}
