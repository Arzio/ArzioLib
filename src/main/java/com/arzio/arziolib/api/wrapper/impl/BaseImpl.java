package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.event.CDBaseDestroyEvent;
import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.arziolib.api.util.TileEntityUtils;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.Base;

import net.minecraft.server.v1_6_R3.NBTTagCompound;

public class BaseImpl implements Base {

	private final Location location;

	public BaseImpl(Location location) {
		this.location = location;
	}

	@Override
	public String getOwnerName() {
		return TileEntityUtils.getTagCompound(this.location.getBlock()).getString("owner");
	}

	@Override
	public Player getOwner() {
		return Bukkit.getPlayerExact(this.getOwnerName());
	}

	@Override
	public boolean isValid() {
		return CDBaseMaterial.isCenter(location.getBlock());
	}

	@Override
	public void destroy() {

		if (!this.isValid()) {
			return;
		}

		CDBaseDestroyEvent event = new CDBaseDestroyEvent(this);
		Bukkit.getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			return;
		}

		Block block = location.getBlock();
		CraftWorld cw = (CraftWorld) block.getWorld();

		CDClasses.blockBaseCenterDestroy.invoke(
				net.minecraft.server.v1_6_R3.Block.byId[CDBaseMaterial.BASE_CENTER.getId()],
				(net.minecraft.server.v1_6_R3.World) cw.getHandle(), block.getX(), block.getY(), block.getZ(), null);
	}

	@Override
	public boolean isPartOfBase(Block block) {

		// TNT nunca faz parte de base
		if (block.getType() == Material.TNT) {
			return false;
		}

		Block base = this.location.getBlock();

		if (!block.getWorld().equals(block.getWorld())) {
			return false;
		}

		// Checking location
		if (Math.abs(base.getX() - block.getX()) <= 7 && Math.abs(base.getY() - block.getY()) <= 7
				&& Math.abs(base.getZ() - block.getZ()) <= 7) {

			return CDBaseMaterial.isBaseMaterial(block);
		}

		return false;
	}

	@Override
	public boolean isInsideBase(Location location) {
		return this.isPartOfBase(location.getBlock());
	}

	@Override
	public Location getLocation() {
		return this.location;
	}

	@Override
	public int getTimeIdle() {
		return TileEntityUtils.getTagCompound(this.location.getBlock()).getInt("timeIdled");
	}

	@Override
	public void setTimeIdle(int time) {
		NBTTagCompound compound = TileEntityUtils.getTagCompound(this.location.getBlock());

		compound.setInt("timeIdled", time);

		TileEntityUtils.setTagCompound(compound, this.location.getBlock());
	}

	@Override
	public String[] getMembers() {

		NBTTagCompound compound = TileEntityUtils.getTagCompound(this.location.getBlock());
		int size = compound.getInt("members");
		
		String[] memberArray = new String[size];
		for (int i = 0; i < size; i++) {
			String str = compound.getString("member" + i);
			memberArray[i] = str;
		}

		return memberArray;
	}

	@Override
	public void setMembers(String[] members) {
		NBTTagCompound compound = TileEntityUtils.getTagCompound(this.location.getBlock());

		compound.setInt("members", members.length);
		for (int i = 0; i < members.length; i++) {
			compound.setString("member"+i, members[i]);
		}

		TileEntityUtils.setTagCompound(compound, this.location.getBlock());
	}

}
