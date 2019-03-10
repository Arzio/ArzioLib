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
	SUPPRESSOR(9335),
	FLASH_GRENADE(9906),
	DECOY_GRENADE(9907),
	SMOKE_GRENADE(9908),
	FIRE_GRENADE(9909),
	GRENADE(9910),
	PIPE_GRENADE(9911),
	GAS_GRENADE(9912),
	SPETSNAZ_CLOTHING(9559),
	RESIDENTIAL_LOOT(1200),
	RARE_RESIDENTIAL_LOOT(1201),
	MEDICAL_LOOT(1202),
	POLICE_LOOT(1203),
	MILITARY_LOOT(1204);
	
	public static CDMaterial[] GRENADES = new CDMaterial[] { FLASH_GRENADE, DECOY_GRENADE, SMOKE_GRENADE, FIRE_GRENADE, GRENADE, PIPE_GRENADE, GAS_GRENADE };

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
