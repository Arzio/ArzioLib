package com.arzio.arziolib.api.impl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
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
	
	private NBTTagCompound getItemContainerTagCompound(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		
		NBTTagCompound firstTag = CraftItemStack.asNMSCopy(stack).tag;
		
		if (firstTag != null) {
			for (String invName : STACK_INVENTORY_NAMES) {
				
				NBTTagCompound inventoryCompound = firstTag.getCompound(invName);
				
				if (inventoryCompound != null) {
					return inventoryCompound;
				}
			}
		}
		
		return null;
	}
	
	private NBTTagCompound getGunTagCompound(ItemStack stack) {
		Gun gun = ArzioLib.getInstance().getItemProvider().getStackAs(Gun.class, stack);
		
		if (gun == null) {
			return null; // Returns null if it is not a Gun
		}
		
		NBTTagCompound compound = CauldronUtils.getTagCompound(stack);
		
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

	@Override
	public boolean isContainer(ItemStack stack) {
		NBTTagCompound compound = this.getItemContainerTagCompound(stack);
		return compound != null;
	}

	@Override
	public void setGunAmmo(ItemStack stack, int amount) {
		NBTTagCompound tag = getGunTagCompound(stack);
		if (tag != null) {
			tag.setInt("gunAmmo", amount);
		}
	}

	@Override
	public int getGunAmmo(ItemStack stack) {
		NBTTagCompound tag = getGunTagCompound(stack);
		int amount = 0;
		if (tag != null) {
			amount = tag.getInt("gunAmmo");
		}
		return amount;
	}

	@Override
	public <T, R> Result<R> accessItemInventory(ItemInventoryNavigatorReturnable<R> navigator) {
		if (this.isContainer(navigator.getItemStack())) {
			NBTTagCompound inventoryCompound = this.getItemContainerTagCompound(navigator.getItemStack());
			NBTTagList content = inventoryCompound.getList("content");
			
			if (content != null) {
				
				int outerMostSlot = -1;
				
				// Get the outmost slot position from the item collection
				for (int i = 0; i < content.size(); i++) {
					NBTTagCompound compound = (NBTTagCompound) content.get(i);
					byte slot = compound.getByte("slot");
					if (slot > outerMostSlot) {
						outerMostSlot = slot;
					}
				}
				
				// Create the temporary inventory
				int rows = 1 + ((outerMostSlot - 1) / 9);
				Inventory inventory = Bukkit.createInventory(null, rows * 9);
				
				// Load the items into the temporary inventory
				for (int i = 0; i < content.size(); i++) {
					NBTTagCompound compound = (NBTTagCompound) content.get(i);
					byte slot = compound.getByte("slot");
					inventory.setItem(slot, CraftItemStack.asBukkitCopy(net.minecraft.server.v1_6_R3.ItemStack.createStack(compound)));
				}
				
				// Navigate through the temporary inventory
				R result = navigator.accessAndReturn(inventory);
				
				// After applied, we need to save the inventory in a NEW itemstack
				NBTTagList itemList = new NBTTagList();
				
				for (int i = 0; i < inventory.getContents().length; i++) {
					ItemStack stack = inventory.getContents()[i];
					if (stack != null) {
						NBTTagCompound itemCompound = new NBTTagCompound();
						itemCompound.setByte("slot", (byte) i);
						CauldronUtils.getNMSStack(stack).save(itemCompound);
						itemList.add(itemCompound);
					}
				}

				inventoryCompound.set("content", itemList);
				
				return new Result<R>(navigator.getItemStack(), result);
			}
		}
		return null;
	}

}
