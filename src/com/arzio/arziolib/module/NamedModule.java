package com.arzio.arziolib.module;

import com.arzio.arziolib.ArzioLib;

public abstract class NamedModule extends Module {
	
	public NamedModule(ArzioLib plugin) {
		super(plugin);
	}

	public abstract String getName();

}
