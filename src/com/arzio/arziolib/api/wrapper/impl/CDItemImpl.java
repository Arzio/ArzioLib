package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.Material;

import com.arzio.arziolib.api.wrapper.CDItem;

import net.minecraft.server.v1_6_R3.Item;

public class CDItemImpl implements CDItem {

	private Material material;
	
	public CDItemImpl(Material material) {
		this.material = material;
	}

	@Override
	public Material getItem() {
		return material;
	}
	
	@SuppressWarnings("deprecation")
	public Item getItemInstance() {
		return Item.byId[material.getId()];
	}

	@Override
	public String getName() {
		return getItemInstance().getName().replace("item.", "");
	}
}
