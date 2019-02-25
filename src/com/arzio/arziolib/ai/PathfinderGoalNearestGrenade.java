package com.arzio.arziolib.ai;

import java.util.List;

import net.minecraft.server.v1_6_R3.EntityCreature;
import net.minecraft.server.v1_6_R3.EntityProjectile;
import net.minecraft.server.v1_6_R3.PathfinderGoal;

public class PathfinderGoalNearestGrenade extends PathfinderGoal {
	private EntityCreature holder;
	private double b;
	private double c;
	private double d;
	private double e;
	private double f;
	private double g;
	private EntityProjectile h;
	private boolean j;
	private boolean l;
	private boolean m;

	public PathfinderGoalNearestGrenade(EntityCreature paramEntityCreature, double paramDouble, boolean paramBoolean) {
		this.holder = paramEntityCreature;
		this.b = paramDouble;
		this.l = paramBoolean;
		a(3);
	}

	@SuppressWarnings("unchecked")
	public boolean a() {
		List<EntityProjectile> list = this.holder.world.a(EntityProjectile.class, this.holder.boundingBox.grow(15, 15, 15));
		if (list.isEmpty()) {
			return false;
		}
		this.h = list.get(0);
		if (this.h == null) {
			return false;
		}
		return true;
	}

	public boolean b() {
		if (this.l) {
			if (this.holder.e(this.h) < 36.0D) {
				if (this.h.e(this.c, this.d, this.e) > 0.010000000000000002D) {
					return false;
				}
				if ((Math.abs(this.h.pitch - this.f) > 5.0D) || (Math.abs(this.h.yaw - this.g) > 5.0D)) {
					return false;
				}
			} else {
				this.c = this.h.locX;
				this.d = this.h.locY;
				this.e = this.h.locZ;
			}
			this.f = this.h.pitch;
			this.g = this.h.yaw;
		}
		return a();
	}

	public void c() {
		this.c = this.h.locX;
		this.d = this.h.locY;
		this.e = this.h.locZ;
		this.j = true;
		this.m = this.holder.getNavigation().a();
		this.holder.getNavigation().a(false);
	}

	public void d() {
		this.h = null;
		this.holder.getNavigation().h();
		this.j = false;
		this.holder.getNavigation().a(this.m);
	}

	public void e() {
		this.holder.getControllerLook().a(this.h, 30.0F, this.holder.bp());
		if (this.holder.e(this.h) < 2.25D) {
			this.holder.getNavigation().h();
		} else {
			this.holder.getNavigation().a(this.h, this.b);
		}
	}

	public boolean f() {
		return this.j;
	}
}
