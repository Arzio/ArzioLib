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

	private final Block block;

	public BaseImpl(Block block) {
		this.block = block;
	}

	@Override
	public String getOwnerName() {
		return TileEntityUtils.getTagCompound(this.block).getString("owner");
	}

	@Override
	public Player getOwner() {
		return Bukkit.getPlayerExact(this.getOwnerName());
	}

	@Override
	public boolean isValid() {
		return CDBaseMaterial.isCenter(this.block);
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

		CraftWorld cw = (CraftWorld) block.getWorld();

		CDClasses.blockBaseCenterDestroy.invoke(
				net.minecraft.server.v1_6_R3.Block.byId[CDBaseMaterial.BASE_CENTER.getId()],
				(net.minecraft.server.v1_6_R3.World) cw.getHandle(), this.getBlock().getX(), this.getBlock().getY(), this.getBlock().getZ(), null);
	}

	@Override
	public boolean isPartOfBase(Block otherBlock) {

		// TNT nunca faz parte de base
		if (otherBlock.getType() == Material.TNT) {
			return false;
		}

		if (!otherBlock.getWorld().equals(this.getBlock().getWorld())) {
			return false;
		}

		// Checking location
		if (Math.abs(otherBlock.getX() - this.getBlock().getX()) <= 7
				&& Math.abs(otherBlock.getY() - this.getBlock().getY()) <= 7
				&& Math.abs(otherBlock.getZ() - this.getBlock().getZ()) <= 7) {

			return CDBaseMaterial.isBaseMaterial(otherBlock);
		}

		return false;
	}

	@Override
	public boolean isInsideBase(Location location) {
		return this.isPartOfBase(location.getBlock());
	}

	@Override
	public Location getLocation() {
		return this.block.getLocation();
	}

	@Override
	public int getTimeIdle() {
		return TileEntityUtils.getTagCompound(this.block).getInt("timeIdled");
	}

	@Override
	public void setTimeIdle(int time) {
		NBTTagCompound compound = TileEntityUtils.getTagCompound(this.block);

		compound.setInt("timeIdled", time);

		TileEntityUtils.setTagCompound(compound, this.block);
	}

	@Override
	public String[] getMembers() {

		NBTTagCompound compound = TileEntityUtils.getTagCompound(this.block);
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
		NBTTagCompound compound = TileEntityUtils.getTagCompound(this.block);

		compound.setInt("members", members.length);
		for (int i = 0; i < members.length; i++) {
			compound.setString("member" + i, members[i]);
		}

		TileEntityUtils.setTagCompound(compound, this.block);
	}

	@Override
	public Block getBlock() {
		return this.block;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((block == null) ? 0 : block.hashCode());
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
		BaseImpl other = (BaseImpl) obj;
		if (block == null) {
			if (other.block != null)
				return false;
		} else if (!block.equals(other.block))
			return false;
		return true;
	}

	@Override
	public boolean hasOwner() {
		String ownerName = this.getOwnerName();
		return ownerName != null && ownerName.length() > 0;
	}

	@Override
	public boolean hasPermission(Player player) {
		if (!this.hasOwner()) {
			return false;
		}
		
		boolean hasPermission = this.getOwnerName().equalsIgnoreCase(player.getName());
		
		if (!hasPermission) {
			for (String member : this.getMembers()) {
				if (member.equalsIgnoreCase(player.getName())) {
					hasPermission = true;
					break;
				}
			}
		}
		
		return hasPermission;
	}

	@Override
	public boolean isOwner(Player player) {
		return player.getName().equals(this.getOwnerName());
	}

}
