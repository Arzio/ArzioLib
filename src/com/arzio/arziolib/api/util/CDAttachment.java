package com.arzio.arziolib.api.util;

import java.util.ArrayList;
import java.util.List;

public class CDAttachment {

	private static final List<CDAttachment> ATTACHMENTS = new ArrayList<>();
	public static final CDAttachment RED_DOT_SIGHT = createNew(1, CDMaterial.RED_DOT_SIGHT);
	public static final CDAttachment ACOG = createNew(2, CDMaterial.RED_DOT_SIGHT);
	public static final CDAttachment EOTECH = createNew(3, CDMaterial.RED_DOT_SIGHT);
	public static final CDAttachment LP_SCOPE = createNew(4, CDMaterial.RED_DOT_SIGHT);
	public static final CDAttachment HP_SCOPE = createNew(5, CDMaterial.RED_DOT_SIGHT);
	public static final CDAttachment TACTICAL_GRIP = createNew(10, CDMaterial.RED_DOT_SIGHT);
	public static final CDAttachment BIPOD = createNew(11, CDMaterial.RED_DOT_SIGHT);
	public static final CDAttachment SUPPRESSOR = createNew(20, CDMaterial.RED_DOT_SIGHT);
	
	private final int id;
	private final CDMaterial material;
	
	private CDAttachment(int id, CDMaterial material) {
		this.id = id;
		this.material = material;
	}
	
	public int getId() {
		return this.id;
	}
	
	public CDMaterial getMaterial() {
		return this.material;
	}
	
	private static CDAttachment createNew(int id, CDMaterial material) {
		CDAttachment attachment = new CDAttachment(id, material);
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
}
