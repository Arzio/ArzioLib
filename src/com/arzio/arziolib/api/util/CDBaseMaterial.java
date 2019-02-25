package com.arzio.arziolib.api.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.arzio.arziolib.api.util.reflection.CDClasses;

public class CDBaseMaterial {

	public static final List<CDBaseMaterial> MATERIAL_SET = new ArrayList<CDBaseMaterial>();
	
	public static final CDBaseMaterial BASE_CENTER = addMaterial("BASE_CENTER", 1220);
	public static final CDBaseMaterial PLANKS = addMaterial("PLANKS", 5);
	public static final CDBaseMaterial STONE_BRICK = addMaterial("STONE_BRICK", 98);
	public static final CDBaseMaterial CHEST = addMaterial("CHEST", 54);
	public static final CDBaseMaterial TRAPPED_CHEST = addMaterial("TRAPPED_CHEST", 146);
	public static final CDBaseMaterial BARBED_WIRE = addMaterial("BARBED_WIRE", 1226);
	public static final CDBaseMaterial REINFORCED_BARBED_WIRE = addMaterial("REINFORCED_BARBED_WIRE", 1227);
	public static final CDBaseMaterial CAMPFIRE = addMaterial("CAMPFIRE", 1225);
	public static final CDBaseMaterial SAND_BARRIER = addMaterial("SAND_BARRIER", 1228);
	public static final CDBaseMaterial REINFORCED_CONCRETE = addMaterial("REINFORCED_CONCRETE", 1231);
	public static final CDBaseMaterial WHITE_STONE_BRICK = addMaterial("WHITE_STONE_BRICK", 1250);
	public static final CDBaseMaterial SAND_BAG = addMaterial("SAND_BAG", 1251);
	public static final CDBaseMaterial WOODEN_DOOR = addMaterial("WOODEN_DOOR", 64);
	
	private int id;
	private String name;
	
	public CDBaseMaterial(String name, int id){
		this.id = id;
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CDBaseMaterial other = (CDBaseMaterial) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@SuppressWarnings("deprecation")
	public static CDBaseMaterial getMaterial(Block block) {
		return getMaterial(block.getTypeId());
	}
	
	public static CDBaseMaterial getMaterial(int blockId) {
		for (CDBaseMaterial material : MATERIAL_SET) {
			if (material.getId() == blockId) {
				return material;
			}
		}
		return null;
	}
	
	public static CDBaseMaterial getMaterial(String blockName) {
		for (CDBaseMaterial material : MATERIAL_SET) {
			if (material.getName().equals(blockName)) {
				return material;
			}
		}
		return null;
	}
	
	public static boolean isBaseMaterial(Block block) {
		return getMaterial(block) != null;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isCenter(Material material) {
		return isCenter(material.getId());
	}
	
	public static boolean isCenter(BlockState state) {
		return isCenter(state.getBlock());
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isCenter(Block block) {
		return isCenter(block.getTypeId());
	}
	
	public static boolean isCenter(int id) {
		return id == BASE_CENTER.getId();
	}
	
	public static CDBaseMaterial addMaterial(String name, int id) {
		CDBaseMaterial material = new CDBaseMaterial(name, id);
		MATERIAL_SET.add(material);
		return material;
	}
	
	public static int[] getBaseMaterialIds() {
		int[] array = new int[CDBaseMaterial.MATERIAL_SET.size()];
		
		for (int i = 0; i < array.length; i++) {
			array[i] = CDBaseMaterial.MATERIAL_SET.get(i).getId();
		}
		
		return array;
	}
	
	public static void pushMaterialsToCD() {
		CDClasses.blockManagerBaseMaterialIds.setValue(null, getBaseMaterialIds());
	}
}
