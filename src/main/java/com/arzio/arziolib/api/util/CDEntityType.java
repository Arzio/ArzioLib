package com.arzio.arziolib.api.util;

import java.lang.reflect.Constructor;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.exception.CDAReflectionException;

/**
 * Enum for CD entity types.
 * @author BlackFairy
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
	
	/** Head entity does not exists at server side anymore due to CD 1.2.8 update. */
	@Deprecated
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
			ArzioLib.getInstance().getLogger().log(Level.WARNING, "Bukkit entity type for the CD Entity '"+this.getName()+"' is deprecated or could not be found. This entity will not be detected properly by plugins in this CD version.");
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getBukkitName() {
		return ArzioLib.MOD_CONTAINER.getModId()+"_"+this.getName();
	}
	
	public EntityType asBukkitType() {
		return this.type;
	}
	
	public boolean isTypeOf(Entity entity) {
		return getTypeOf(entity) == this;
	}
	
	public Entity spawnEntity(World world, Location location) throws CDAReflectionException{
		Class<? extends Entity> entityClass = this.asBukkitType().getEntityClass();
		try {
			Constructor<?> constructor = entityClass.getDeclaredConstructor(net.minecraft.server.v1_6_R3.World.class);
			
			CraftWorld craftWorld = (CraftWorld) world;
			net.minecraft.server.v1_6_R3.Entity entity = (net.minecraft.server.v1_6_R3.Entity) constructor.newInstance(craftWorld.getHandle());
			
			craftWorld.getHandle().addEntity(entity, SpawnReason.CUSTOM);
			return entity.getBukkitEntity();
		} catch (Exception e) {
			throw new CDAReflectionException("Failed to spawn a custom entity", e);
		}
	}
	
	public static CDEntityType getTypeOf(Entity entity) {
        if (entity == null)
            return null;
        
        String entityTypeName = entity.getType().name();
        for (CDEntityType type : CDEntityType.values()) {
        	EntityType bukkitType = type.asBukkitType();
        	
        	// Prevents nullpointer if the entity type does not exists anymore.
        	if (bukkitType != null && bukkitType.name().equalsIgnoreCase(entityTypeName)) {
        		return type;
        	}
        }
        
        // Entity type not found with a simple search.
        // We need to search it deeper.
        EntityType bukkitType = EntityUtil.getCompatibleEntityType(entity);
        
        if (bukkitType == null) {
        	return null;
        }
        
        String bukkitEntityTypeName = bukkitType.name();
        for (CDEntityType type : CDEntityType.values()) {
        	EntityType entityType = type.asBukkitType();
        	
        	// Prevents nullpointer if the entity type does not exists anymore.
        	if (entityType != null && entityType.name().equalsIgnoreCase(bukkitEntityTypeName)) {
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
