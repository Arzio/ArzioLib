package com.arzio.arziolib.api.util;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.minecraft.server.v1_6_R3.DamageSource;
import net.minecraftforge.common.EnumHelper;

public enum CDDamageCause {

	BLEEDING("bleeding"),
	RBI("infection"),
	BULLET_BODY("bullet"),
	BULLET_HEADSHOT("bullethead"),
	MELEE("knife"),
	THIRST("thirst"),
	ZOMBIE("zombie");
	
	private String id;
	private DamageCause cause;
	
	private CDDamageCause(String id) {
		this.id = id;
		this.cause = EnumHelper.addEnum(new Class[][] { {DamageCause.class} } , DamageCause.class, this.name());
	}
	
	public boolean isTypeOf(EntityDamageEvent event) {
		return this.isTypeOf(event.getCause());
	}
	
	public boolean isTypeOf(DamageCause cause) {
		return this.asBukkitDamageCause() == cause;
	}
	
	public DamageCause asBukkitDamageCause() {
		return cause;
	}
	
	public String getId() {
		return this.id;
	}
	
	public boolean isTypeOf(DamageSource source) {
		return source.translationIndex.equals(this.getId());
	}
	
	public static boolean isBullet(EntityDamageEvent event) {
		return isBullet(event.getCause());
	}
	
	public static boolean isBullet(DamageCause cause) {
		return CDDamageCause.BULLET_BODY.isTypeOf(cause) || CDDamageCause.BULLET_HEADSHOT.isTypeOf(cause);
	}
	
	public static CDDamageCause getFrom(DamageSource source) {
		for (CDDamageCause cause : CDDamageCause.values()) {
			if (cause.isTypeOf(source)) {
				return cause;
			}
		}
		return null;
	}
	
	public static CDDamageCause getFrom(DamageCause bukkitCause) {
		for (CDDamageCause cause : CDDamageCause.values()) {
			if (cause.isTypeOf(bukkitCause)) {
				return cause;
			}
		}
		return null;
	}
}
