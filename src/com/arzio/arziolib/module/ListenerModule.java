package com.arzio.arziolib.module;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.ForgeListener;

public abstract class ListenerModule extends NamedModule implements ForgeListener, Listener{

	public ListenerModule(ArzioLib plugin) {
		super(plugin);
	}

	@Override
	protected void onEnable() {
		this.getPlugin().getForgeBukkitEventManager().registerEvents(this.getPlugin(), this);
		Bukkit.getPluginManager().registerEvents(this, this.getPlugin());
	}

	@Override
	protected void onDisable() {
		this.getPlugin().getForgeBukkitEventManager().unregisterEvents(this);
		HandlerList.unregisterAll(this);
	}
}
