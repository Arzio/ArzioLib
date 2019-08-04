package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.util.CDPacketHelper;
import com.arzio.arziolib.api.wrapper.CDSharedItem;

public class CDSharedItemImpl extends CDItemImpl implements CDSharedItem{

	public CDSharedItemImpl(Material material) {
		super(material);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateStats(Player player) {
		CDPacketHelper.updateSharedItems(player, this);
	}

}
