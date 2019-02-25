package com.arzio.arziolib.api.util;

import org.bukkit.Material;

public enum CDMaterial {
	BLOODBAG(9931),
	HANDCUFFS(9717),
	M4A1(9256),
	M1_GARAND_MAGAZINE(9323),
	MOSSBERG(9297),
	CROSSBOW(9305),
	TASER(9265),
	M1_GARAND(9289),
	RED_DOT_SIGHT(9331),
	ACOG(9332),
	EOTECH(9956),
	LP_SCOPE(9333),
	HP_SCOPE(9334),
	TACTICAL_GRIP(9336),
	BIPOD(9337),
	SUPPRESSOR(9335);

	private int id;
	
	private CDMaterial(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	@SuppressWarnings("deprecation")
	public Material asMaterial() {
		return Material.getMaterial(this.id);
	}
}
