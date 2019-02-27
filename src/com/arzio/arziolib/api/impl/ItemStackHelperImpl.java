package com.arzio.arziolib.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.ItemStackHelper;
import com.arzio.arziolib.api.util.CDAttachment;
import com.arzio.arziolib.api.util.CDAttachmentType;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.wrapper.Gun;

import net.minecraft.server.v1_6_R3.Item;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;

public class ItemStackHelperImpl implements ItemStackHelper {
	
	public static final String[] STACK_INVENTORY_NAMES = { "tanks", "backpackInv", "vestinv" };
	
	public static boolean isFromSameClass(Item item, int itemId) {
		Item itemById = Item.byId[itemId];
		return itemById.getClass().equals(item.getClass());
	}
	
	@Override
	public List<ItemStack> getStacksFromBag(ItemStack stack){
		List<ItemStack> listStacks = new ArrayList<ItemStack>();
		
		for (String invName : STACK_INVENTORY_NAMES) {
			NBTTagCompound firstTag = CraftItemStack.asNMSCopy(stack).getTag();
			
			if (firstTag != null) {
				NBTTagCompound inventoryCompound = firstTag.getCompound(invName);
				
				if (inventoryCompound != null) {
					NBTTagList content = inventoryCompound.getList("content");
					
					if (content != null) {
						for (int i = 0; i < content.size(); i++) {
							NBTTagCompound compound = (NBTTagCompound) content.get(i);
							listStacks.add(CraftItemStack.asBukkitCopy(net.minecraft.server.v1_6_R3.ItemStack.createStack(compound)));
						}
					}
				}
			}
		}
		
		return listStacks;
	}

	@Override
	public void setAmmoInGun(ItemStack itemstack, int amount) {
		NBTTagCompound tag = getGunTagCompound(itemstack);
		if (tag != null) {
			tag.setInt("gunAmmo", amount);
		}
	}

	@Override
	public int getAmmoInGun(ItemStack itemstack) {
		NBTTagCompound tag = getGunTagCompound(itemstack);
		int amount = 0;
		if (tag != null) {
			amount = tag.getInt("gunAmmo");
		}
		return amount;
	}
	
	private NBTTagCompound getGunTagCompound(ItemStack paramItemStack) {
		Gun gun = ArzioLib.getInstance().getItemProvider().getStackAs(Gun.class, paramItemStack);
		
		if (gun == null) {
			return null; // Returns null if it is not a Gun
		}
		
		NBTTagCompound compound = CauldronUtils.getTagCompound(paramItemStack);
		
		if (compound == null) {
			return null;
		}
		
		String tagCompoundName = "cdagun";
		
		if (!compound.hasKey(tagCompoundName)) {
			compound.set(tagCompoundName, new NBTTagCompound(tagCompoundName));
		}
		return (NBTTagCompound) compound.get(tagCompoundName);
	}

	@Override
	public boolean canGunFire(ItemStack stack) {
		NBTTagCompound tag = getGunTagCompound(stack);
		return tag.getInt("fired") > 0;
	}

	@Override
	public Material getAttachment(ItemStack stack, CDAttachmentType type) {
		NBTTagCompound compound = getGunTagCompound(stack);
		if ((compound != null) && (compound.hasKey("attachmentSlot" + type.getId()))) {
			int id = compound.getInt("attachmentSlot" + type.getId());
			
			CDAttachment attach = CDAttachment.getById(id);
			
			if (attach == null) {
				return null;
			}
			
			return attach.getMaterial().asMaterial();
		}
		return null;
	}

	@Override
	public boolean hasAttachment(ItemStack stack, CDAttachmentType type) {
		return getAttachment(stack, type) != null;
	}

	@Override
	public void setAttachment(ItemStack stack, CDAttachmentType type, CDAttachment attach) {
		NBTTagCompound compound = getGunTagCompound(stack);
		if (compound == null) {
			return;
		}
		
		compound.setInt("attachmentSlot"+type.getId(), attach.getId());
	}

}
