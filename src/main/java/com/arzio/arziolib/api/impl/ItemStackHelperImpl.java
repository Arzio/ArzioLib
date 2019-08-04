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
import com.arzio.arziolib.api.util.CDMaterial;
import com.arzio.arziolib.api.util.CDSpecialSlot;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.Ammo;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.api.wrapper.InventoryCDA;
import com.arzio.arziolib.api.wrapper.ItemProvider;

import net.minecraft.server.v1_6_R3.Item;
import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.NBTTagList;

public class ItemStackHelperImpl implements ItemStackHelper {
	
	public static final String[] STACK_INVENTORY_NAMES = { "tanks", "backpackInv", "vestinv" };
	
	public static boolean isFromSameClass(Item item, int itemId) {
		Item itemById = Item.byId[itemId];
		return itemById.getClass().equals(item.getClass());
	}
	
	public NBTTagCompound getItemContainerTagCompound(ItemStack stack) {
		NBTTagCompound firstTag = CauldronUtils.getTagCompound(stack);
		
		if (firstTag == null) {
			return null;
		}
		
		for (String invName : STACK_INVENTORY_NAMES) {
			if (firstTag.hasKey(invName)) {
				return firstTag.getCompound(invName);
			}
		}
		
		return null;
	}
	
	public NBTTagCompound getGunTagCompound(ItemStack stack) {
		Gun gun = ArzioLib.getInstance().getItemProvider().getStackAs(Gun.class, stack);
		
		if (gun == null) {
			return null; // Returns null if its not a Gun
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
	public CDAttachment getAttachment(ItemStack stack, CDAttachmentType type) {
		NBTTagCompound compound = getGunTagCompound(stack);
		if ((compound != null) && (compound.hasKey("attachmentSlot" + type.getId()))) {
			int id = compound.getInt("attachmentSlot" + type.getId());
			
			CDAttachment attach = CDAttachment.getById(id);
			
			return attach;
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
		
		int id = 0;
		if (attach != null) {
			id = attach.getId();
		}
		
		compound.setInt("attachmentSlot"+type.getId(), id);
	}

	@Override
	public boolean hasInventory(ItemStack stack) {
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
	
	private void setGunClipId(ItemStack stack, int id) {
		NBTTagCompound tag = getGunTagCompound(stack);
		if (tag != null) {
			tag.setInt("gunLoadedClip", id);
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
	public <T, R> AccessResult<R> accessItemInventory(ItemInventoryNavigator<R> navigator) {
		if (this.hasInventory(navigator.getItemStack())) {
			NBTTagCompound inventoryCompound = this.getItemContainerTagCompound(navigator.getItemStack());
			
			NBTTagList content = inventoryCompound.getList("content");
			
			if (content != null) {
				int outermostSlot = -1;
				
				// Get the outmost slot position from the item collection
				for (int i = 0; i < content.size(); i++) {
					NBTTagCompound compound = (NBTTagCompound) content.get(i);
					byte slot = compound.getByte("slot");
					if (slot > outermostSlot) {
						outermostSlot = slot;
					}
				}
				
				// Create the temporary inventory
				int rows = 1 + ((outermostSlot) / 9);
				Inventory inventory = Bukkit.createInventory(null, rows * 9);
				
				// Load the items into the temporary inventory
				for (int i = 0; i < content.size(); i++) {
					NBTTagCompound compound = (NBTTagCompound) content.get(i);
					byte slot = compound.getByte("slot");
					inventory.setItem(slot, CraftItemStack.asBukkitCopy(net.minecraft.server.v1_6_R3.ItemStack.createStack(compound)));
				}
				
				// Navigate through the temporary inventory
				R resultValue = navigator.accessAndReturn(inventory);
				
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
				
				return new AccessResult<R>(navigator.getItemStack(), resultValue);
			}
		}
		return null;
	}

	@Override
	public <T, R> AccessResult<R> accessItemInventory(InventoryCDA inventory, CDSpecialSlot slot, final InventoryNavigator<R> navigator) {
		
		ItemStack stack = inventory.getStackInSpecialSlot(slot);
		if (stack == null) {
			return null;
		}
		
		AccessResult<R> result = this.accessItemInventory(new ItemInventoryNavigator<R>(stack) {

			@Override
			public R accessAndReturn(Inventory inventory) {
				return navigator.accessAndReturn(inventory);
			}
			
		});
		
		if (result == null) {
			return null;
		}
		
		inventory.setStackInSpecialSlot(slot, result.getStack());
		return result;
	}

	@Override
	public void setGunClip(ItemStack gun, ItemStack clip) {
		ItemProvider provider = ArzioLib.getInstance().getItemProvider();
		
		if (provider.getStackAs(Gun.class, gun) == null) {
			throw new IllegalArgumentException("The gun stack must be a gun item!");
		}
		
		if (clip == null) {
			clip = new ItemStack(Material.AIR);
		}
		
		if (clip.getType() == Material.AIR) {			
			this.setGunClipId(gun, 0);
			this.setGunAmmo(gun, 0);
		} else {
			if (provider.getStackAs(Ammo.class, clip) == null) {
				throw new IllegalArgumentException("The ammo stack must be a ammo item or AIR or null!");
			}
			CDClasses.itemGunSetClipMethod.invoke(Item.byId[gun.getTypeId()], null, CauldronUtils.getNMSStack(gun), CauldronUtils.getNMSStack(clip));
		}
	}

	@Override
	public ItemStack getGunClip(ItemStack gun) {
		net.minecraft.server.v1_6_R3.ItemStack nmsStack = (net.minecraft.server.v1_6_R3.ItemStack) CDClasses.itemGunGetClipMethod.invoke(Item.byId[CDMaterial.M4A1.getId()], CauldronUtils.getNMSStack(gun));
		return CraftItemStack.asBukkitCopy(nmsStack);
	}

}
