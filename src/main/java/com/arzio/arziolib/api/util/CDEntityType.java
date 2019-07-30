package com.arzio.arziolib.api.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.exception.CDAReflectionException;

import net.minecraft.server.v1_6_R3.EntityTypes;

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
	C4("c4"),
	FLAME_THROWER_FIRE("flamethrowerfire"),
	SUPPLY_DROP("supplydrop");
	
	private final String name;
	private EntityType type;
	private Class<? extends net.minecraft.server.v1_6_R3.Entity> entityClass;
	
	private CDEntityType(String name) {
		this.name = name;
		try {
			this.type = EntityType.valueOf(this.getBukkitName().toUpperCase());
		} catch (IllegalArgumentException e) {
			ArzioLib.getInstance().getLogger().log(Level.WARNING, "Bukkit entity type for the CD Entity '"+this.getName()+"' is deprecated or could not be found. This entity will not be detected properly by plugins in this CD version.");
			return; // Do not continue.
		}
		this.findEntityClass();
	}
	
	private void findEntityClass() {
        try {
            Field mapEntityNameToClass = EntityTypes.class.getDeclaredField("b");
            mapEntityNameToClass.setAccessible(true);
            Map map = (Map) mapEntityNameToClass.get(null);
            
            // First try
            this.entityClass = (Class<? extends net.minecraft.server.v1_6_R3.Entity>) map.get(this.getName());
            
            // Second try
            if (this.entityClass == null) {
                this.entityClass = (Class<? extends net.minecraft.server.v1_6_R3.Entity>) map.get(this.getEntityModName());
            }
            
            // Failed.
            if (this.entityClass == null) {
                throw new ClassNotFoundException("Entity class of CD Entity "+this.getName()+" not found!");
            }
        } catch (Exception e) {
            ArzioLib.getInstance().getLogger().log(Level.WARNING, "Failed to get the class of the CD Entity "+this.getName()+".");
            e.printStackTrace();
        }
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getEntityModName() {
	    return String.format("%s.%s", ArzioLib.MOD_ID, this.getName());
	}
	
	public String getBukkitName() {
		return ArzioLib.MOD_CONTAINER.getModId()+"_"+this.getName();
	}
	
	@Deprecated
	public EntityType asBukkitType() {
		return this.type;
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends net.minecraft.server.v1_6_R3.Entity> getNMSClass(){
	    return entityClass;
	}
	
	public Constructor<? extends net.minecraft.server.v1_6_R3.Entity> getConstructor() throws CDAReflectionException {
        try {
            return this.getNMSClass().getDeclaredConstructor(net.minecraft.server.v1_6_R3.World.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new CDAReflectionException(e);
        }
	}
	
	public boolean isTypeOf(Entity entity) {
		return getTypeOf(entity) == this;
	}
	
	public Entity spawnEntity(Location location) throws CDAReflectionException{
		try {
			
			CraftWorld craftWorld = (CraftWorld) location.getWorld();
			net.minecraft.server.v1_6_R3.Entity entity = (net.minecraft.server.v1_6_R3.Entity) this.getConstructor().newInstance(craftWorld.getHandle());
			entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
			
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
	
	public static boolean isZombie(Entity entity) {
	    for (CDEntityType zombieType : getZombieTypes()) {
	        if (zombieType.isTypeOf(entity)) {
	            return true;
	        }
	    }
	    return false;
	}
	
    public static boolean isGrenade(Entity entity) {
        for (CDEntityType zombieType : getGrenadeTypes()) {
            if (zombieType.isTypeOf(entity)) {
                return true;
            }
        }
        return false;
    }
    
    public static CDEntityType[] getZombieTypes() {
    	return new CDEntityType[] { CD_ZOMBIE, CD_ZOMBIE_FAST, CD_ZOMBIE_TANK, CD_ZOMBIE_WEAK };
    }
    
    public static CDEntityType[] getGrenadeTypes() {
    	return new CDEntityType[] { GRENADE, GRENADE_DECOY, GRENADE_FIRE, GRENADE_FLASH, GRENADE_GAS, GRENADE_PIPE_BOMB, GRENADE_SMOKE };
    }
}
