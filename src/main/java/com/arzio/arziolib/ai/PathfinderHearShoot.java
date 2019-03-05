package com.arzio.arziolib.ai;

import java.util.List;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.config.UserData;

import net.minecraft.server.v1_6_R3.EntityCreature;
import net.minecraft.server.v1_6_R3.EntityInsentient;
import net.minecraft.server.v1_6_R3.EntityLiving;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.GenericAttributes;
import net.minecraft.server.v1_6_R3.PathfinderGoal;

public class PathfinderHearShoot extends PathfinderGoal {

	private double range = 50D;
	private EntityLiving targetEntity;
	private EntityInsentient holder;
	private final ArzioLib plugin;

	public PathfinderHearShoot(ArzioLib plugin, EntityCreature entitycreature) {
		this.plugin = plugin;
		this.holder = entitycreature;
	}

	public boolean a() {
		if (plugin.isEnabled()) {
			@SuppressWarnings("unchecked")
			List<EntityPlayer> list = this.holder.world.a(EntityPlayer.class,this.holder.boundingBox.grow(this.range, this.range, this.range));
			if (!list.isEmpty()) {
				for (EntityPlayer player : list) {
					int distance = (int) this.holder.d(player);

					UserData data = UserData.getFrom(player.getName());
					if (data.getCurrentSoundLevel() >= distance) {
						this.targetEntity = player;
						return true;
					}
				}
			}
		}
		
		return false;
	}

	public void c() {
		this.holder.setGoalTarget(this.targetEntity);
		this.holder.getAttributeInstance(GenericAttributes.b).setValue(range);
		super.c();
	}

}
