package com.arzio.arziolib.api.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

public enum CDHarvestType {
	WOOD(new Material[] { Material.LOG }, new CDBaseMaterial[] { CDBaseMaterial.PLANKS, CDBaseMaterial.CHEST, CDBaseMaterial.WOODEN_DOOR, CDBaseMaterial.BARBED_WIRE, CDBaseMaterial.REINFORCED_BARBED_WIRE }),
	STONE(new Material[] { Material.STONE }, new CDBaseMaterial[] { CDBaseMaterial.STONE_BRICK, CDBaseMaterial.CAMPFIRE, CDBaseMaterial.SAND_BARRIER }),
	NONE;
	
	private Material[] sourceBlocks;
	private CDBaseMaterial[] baseBlocks;
	
	private CDHarvestType(Material[] sourceBlocks, CDBaseMaterial[] baseBlocks) {
		this.sourceBlocks = sourceBlocks;
		this.baseBlocks = baseBlocks;
	}
	
	private CDHarvestType() {
		this(new Material[0], new CDBaseMaterial[0]);
	}
	
	public boolean canHarvest(Block block) {
		return this.canHarvest(block.getType());
	}
	
	public boolean canHarvest(Material material) {
		for (Material mat : sourceBlocks) {
			if (mat == material) {
				return true;
			}
		}
		
		for (CDBaseMaterial baseMaterial : baseBlocks) {
			if (baseMaterial.asMaterial() == material) {
				return true;
			}
		}
		return false;
	}

}
