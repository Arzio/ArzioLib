package com.arzio.arziolib.api.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CDAttachment {

	private static final List<CDAttachment> ATTACHMENTS = new ArrayList<>();
	public static final CDAttachment RED_DOT_SIGHT = createNew(1, CDMaterial.RED_DOT_SIGHT, CDAttachmentType.SCOPE);
	public static final CDAttachment ACOG = createNew(2, CDMaterial.ACOG, CDAttachmentType.SCOPE);
	public static final CDAttachment EOTECH = createNew(3, CDMaterial.EOTECH, CDAttachmentType.SCOPE);
	public static final CDAttachment LP_SCOPE = createNew(4, CDMaterial.LP_SCOPE, CDAttachmentType.SCOPE);
	public static final CDAttachment HP_SCOPE = createNew(5, CDMaterial.HP_SCOPE, CDAttachmentType.SCOPE);
	public static final CDAttachment TACTICAL_GRIP = createNew(10, CDMaterial.TACTICAL_GRIP, CDAttachmentType.GRIP);
	public static final CDAttachment BIPOD = createNew(11, CDMaterial.BIPOD, CDAttachmentType.GRIP);
	public static final CDAttachment SUPPRESSOR = createNew(20, CDMaterial.SUPPRESSOR, CDAttachmentType.MUZZLE);
	
	private final int id;
	private final CDMaterial material;
	private final CDAttachmentType type;
	
	private CDAttachment(int id, CDMaterial material, CDAttachmentType type) {
		this.id = id;
		this.material = material;
		this.type = type;
	}
	
	public CDAttachmentType getType() {
		return this.type;
	}
	
	public int getId() {
		return this.id;
	}
	
	public CDMaterial getMaterial() {
		return this.material;
	}
	
	private static CDAttachment createNew(int id, CDMaterial material, CDAttachmentType type) {
		CDAttachment attachment = new CDAttachment(id, material, type);
		ATTACHMENTS.add(attachment);
		return attachment;
	}
	
	public static CDAttachment getById(int id) {
		for (CDAttachment attach : ATTACHMENTS) {
			if (attach.getId() == id) {
				return attach;
			}
		}
		return null;
	}
	
	public static CDAttachment getFromMaterial(CDMaterial material) {
		return getFromMaterial(material.asMaterial());
	}
	
	public static CDAttachment getFromMaterial(Material material) {
		for (CDAttachment attach : ATTACHMENTS) {
			if (attach.getMaterial().asMaterial() == material) {
				return attach;
			}
		}
		return null;
	}
	
	public static CDAttachment getFrom(ItemStack stack) {
		if (stack == null) {
			return null;
		}
		return getFromMaterial(stack.getType());
	}
	
	public static CDAttachment[] getAll() {
		return ATTACHMENTS.toArray(new CDAttachment[0]);
	}
}
