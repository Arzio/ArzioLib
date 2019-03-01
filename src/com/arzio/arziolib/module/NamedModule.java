package com.arzio.arziolib.module;

import com.arzio.arziolib.ArzioLib;

public abstract class NamedModule extends Module {
	
	public NamedModule(ArzioLib plugin) {
		this(plugin, true);
	}
	
	public NamedModule(ArzioLib plugin, boolean defaultState) {
		super(plugin, defaultState);
	}

	public abstract String getName();

}
