package com.arzio.arziolib.module;

import com.arzio.arziolib.ArzioLib;

public abstract class Module {
	
	private boolean isEnabled = false;
	private final ArzioLib plugin;
	
	public Module(ArzioLib plugin) {
		this.plugin = plugin;
	}
	
	protected abstract void onEnable();
	
	protected abstract void onDisable();
	
	protected ArzioLib getPlugin() {
		return this.plugin;
	}
	
	public boolean isEnabled() {
		return this.isEnabled;
	}
	
	public void reload() {
		this.setEnabled(false);
		this.setEnabled(true);
	}
	
	public void setEnabled(boolean state) {
		if (isEnabled != state) {
			isEnabled = state;
			
			if (isEnabled) {
				this.onEnable();
				this.plugin.getLogger().info("Module "+this.getClass().getSimpleName()+" enabled!");
			} else {
				this.onDisable();
				this.plugin.getLogger().info("Module "+this.getClass().getSimpleName()+" disabled!");
			}
		}
	}

}
