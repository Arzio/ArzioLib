package com.arzio.arziolib.api.util;

import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.util.reflection.ReflectionHelper;

import net.minecraft.server.v1_6_R3.EntityInsentient;
import net.minecraft.server.v1_6_R3.EntityTrackerEntry;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_6_R3.WorldServer;

public class EntityUtil {

	public static PathfinderGoalSelector getTargetSelector(EntityInsentient entity) {
		return ReflectionHelper.getValueFromFieldName(PathfinderGoalSelector.class, 
				EntityInsentient.class, entity, "field_70715_bh");
	}
	
	public static PathfinderGoalSelector getGoalSelector(EntityInsentient entity) {
		return ReflectionHelper.getValueFromFieldName(PathfinderGoalSelector.class, 
				EntityInsentient.class, entity, "field_70714_bg");
	}
	
	@SuppressWarnings("deprecation")
	public static EntityType getCompatibleEntityType(Entity entity) {
		return EntityType.fromId(CauldronUtils.getEntityTypeIDfromClass(((CraftEntity) entity).getHandle()));
	}

	public static void setEntitySize(Entity entity, float x, float y) {
		Method m = ReflectionHelper.findMethodWithTypes(net.minecraft.server.v1_6_R3.Entity.class, "func_70105_a", null, new Class<?>[] { float.class, float.class });
		try {
			m.invoke(((CraftEntity) entity).getHandle(), x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void refreshEntityTrackers(Entity entity) {
		WorldServer worldServer = ((CraftWorld) entity.getWorld()).getHandle();
		EntityTrackerEntry entry = (EntityTrackerEntry) worldServer.tracker.trackedEntities.get(entity.getEntityId());
		List<?> nmsPlayers = worldServer.players;

		// Remove the players from the observer list
		entry.trackedPlayers.removeAll(nmsPlayers);
		
		// Add the players again to the observer list
		entry.scanPlayers(nmsPlayers);
	}
	
	public static String getC4OwnerName(Entity entity) {
		String name = null;
		
		CraftEntity cEntity = (CraftEntity) entity;
		NBTTagCompound compound = new NBTTagCompound();
		cEntity.getHandle().e(compound);
		name = compound.getString("user");
		
		return name;
	}
	
	public static Player getClosestPlayer(Entity entity) {
		return getClosestPlayerInRadius(entity, Double.MAX_VALUE);
	}
	
	public static Player getClosestPlayerInRadius(Entity entity, double radius) {
		Player result = null;
		double closest = radius;
		for (Player player : entity.getWorld().getPlayers()) {
			double distance = player.getLocation().distance(entity.getLocation());
			if (distance < closest) {
				closest = distance;
				result = player;
			}
		}
		return result;
	}
}
