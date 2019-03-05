package com.arzio.arziolib.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.exception.CDAReflectionException;

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.registry.GameRegistry;
import guava10.com.google.common.primitives.Ints;
import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.EntityTypes;
import net.minecraft.server.v1_6_R3.IInventory;
import net.minecraft.server.v1_6_R3.Item;
import net.minecraft.server.v1_6_R3.NBTTagCompound;

public class CauldronUtils {

	private static Field handleField = null;
	
	public static Entity getNMSEntity(org.bukkit.entity.Entity entity) {
		return ((CraftEntity) entity).getHandle();
	}
	
	@SuppressWarnings("unchecked")
	public static List<IPlayerTracker> getForgePlayerTrackers() {
		try {
			Field trackersField = GameRegistry.class.getDeclaredField("playerTrackers");
			trackersField.setAccessible(true);
			return (List<IPlayerTracker>) trackersField.get(null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new CDAReflectionException(e);
		}
	}
	
	public static NBTTagCompound getTagCompound(ItemStack stack) {
		net.minecraft.server.v1_6_R3.ItemStack resultStack = getNMSStack(stack);
		
		if (resultStack == null) {
			return null;
		}
		
		return resultStack.tag;
	}
	
	public static boolean isPluginLoaded(String pluginName) {
		return Bukkit.getPluginManager().getPlugin(pluginName) != null;
	}
	
	public static net.minecraft.server.v1_6_R3.ItemStack getNMSStack(ItemStack stack){
		if (!(stack instanceof CraftItemStack)) {
			return null;
		}
		
		CraftItemStack craftStack = (CraftItemStack) stack;
		
		try {
			if (handleField == null) {
				handleField = CraftItemStack.class.getDeclaredField("handle");
				handleField.setAccessible(true);
			}

			net.minecraft.server.v1_6_R3.ItemStack nmsStack = (net.minecraft.server.v1_6_R3.ItemStack) handleField.get(craftStack);
			
			return nmsStack;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static IInventory getNMSInventory(Inventory inventory) {
		if (inventory instanceof CraftInventory) {
			CraftInventory cInventory = (CraftInventory) inventory;
			return cInventory.getInventory();
		}
		return null;
	}
	
	public static int getEntityTypeIDfromClass(Entity entity) {
		int entityClassToIdValue = EntityTypes.a(entity);
		
		if (entityClassToIdValue != 0) { // The non-present value for the mapping is 0
			return entityClassToIdValue;
		}

		Class<? extends Entity> entityClass = entity.getClass();
		return -Math.abs(entityClass.getName().hashCode() ^ entityClass.getName().hashCode() >>> 16);
	}
	
	public static void setMaxStackSize(Material material, int amount) {
		try {
			
			@SuppressWarnings("deprecation")
			int id = material.getId();
			
			Item item = Item.byId[id];
			if (item == null) {
				throw new IllegalArgumentException("Item not found: ID "+id);
			}
			
			Field f = Material.class.getDeclaredField("maxStack");
			f.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);

			f.set(material, amount);
			
			Field fieldMaxItem = Item.class.getDeclaredField("maxStackSize");
			fieldMaxItem.setAccessible(true);
			fieldMaxItem.set(item, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static Material[] intArrayToMaterialArray(int[] ids) {
		Material[] materialArray = new Material[ids.length];
		
		for (int i = 0; i < ids.length; i++) {
			materialArray[i] = Material.getMaterial(ids[i]);
		}
		return materialArray;
	}
	
	public static Material[] intListToMaterialArray(List<Integer> ids){
		return intArrayToMaterialArray(Ints.toArray(ids));
	}
	
	@SuppressWarnings("deprecation")
	public static int[] materialArrayToIntArray(Material[] materials) {
		int[] intArray = new int[materials.length];
		
		for (int i = 0; i < materials.length; i++) {
			intArray[i] = materials[i].getId();
		}
		return intArray;
	}
	
	public static List<Integer> materialArrayToIntegerList(Material[] materials){
		return Ints.asList(materialArrayToIntArray(materials));
	}
}
