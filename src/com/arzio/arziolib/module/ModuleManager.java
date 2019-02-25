package com.arzio.arziolib.module;

import java.util.Collection;
import java.util.List;

public interface ModuleManager {

	public void registerModule(NamedModule module);
	
	public void toggleModule(Class<? extends NamedModule> moduleClass, ToggleAction type);
	
	public <T extends NamedModule> T getModule(Class<T> moduleClass);
	
	public Collection<NamedModule> getModules();
	
	public List<NamedModule> getModulesByPartialName(String partialName);
	
	public void toggleAll(ToggleAction type);

	public static enum ToggleAction {
		TOGGLE_ON,
		TOGGLE_OFF,
		TOGGLE_FROM_CONFIG;
	}
}
