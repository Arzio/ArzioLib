package com.arzio.arziolib.api.util;

public enum CDAttachmentType {

	SCOPE(0),
	GRIP(1),
	MUZZLE(2);
	
	private int id;
	
	private CDAttachmentType(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
}
