package com.arzio.arziolib.api.util;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;

import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.TileEntity;

public class TileEntityUtils {

	public boolean hasTile(Block block){
		return getTile(block) != null;
	}

	public static TileEntity getTile(Block from) {
		CraftWorld cWorld = (CraftWorld) from.getWorld();
		return cWorld.getTileEntityAt(from.getX(), from.getY(), from.getZ());
	}
	
	public static NBTTagCompound getTagCompound(Block block) {
		return getTagCompound(getTile(block));
	}
	
	public static NBTTagCompound getTagCompound(TileEntity tile) {
		NBTTagCompound compound = new NBTTagCompound();
		tile.b(compound);
		return compound;
	}
	
	public static void setTagCompound(NBTTagCompound compound, Block block) {
		setTagCompound(compound, getTile(block));
	}
	
	public static void setTagCompound(NBTTagCompound compound, TileEntity tile) {
		tile.a(compound);
	}
}
