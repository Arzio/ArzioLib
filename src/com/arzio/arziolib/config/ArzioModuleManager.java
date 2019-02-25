package com.arzio.arziolib.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.module.NamedModule;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.ModuleManager;

public class ArzioModuleManager implements ModuleManager{

	private Map<Class<? extends NamedModule>, NamedModule> moduleMap = new HashMap<>();
	private final YMLFile yml;
	private final ArzioLib plugin;
	
	public ArzioModuleManager(ArzioLib plugin) {
		this.yml = new YMLFile(plugin, "modules.yml");
		this.plugin = plugin;
	}
	
	@Override
	public void registerModule(NamedModule module) {
		if (moduleMap.containsKey(module.getClass())) {
			return;
		}
		
		if (module.getName() == null) {
			throw new IllegalArgumentException("Module "+module.getClass().getSimpleName()+"'s getName() method is null!");
		}
		
		moduleMap.put(module.getClass(), module);
	}
	
	@Override
	public void toggleModule(Class<? extends NamedModule> moduleClass, ToggleAction type) {
		if (!moduleMap.containsKey(moduleClass)) {
			throw new IllegalArgumentException("The module '"+moduleClass.getSimpleName()+"' is not registered in this Module Manager!");
		}
		
		if (type == ToggleAction.TOGGLE_OFF) {
			moduleMap.get(moduleClass).setEnabled(false);
			return;
		}
		
		NamedModule module = moduleMap.get(moduleClass);
		
		boolean shouldEnable = true;
		if (type == ToggleAction.TOGGLE_FROM_CONFIG) {
			
			yml.reload();
			
			String moduleNameWithCategory = module.getName()
					.replaceAll("^fix-", "fix.fix-")
					.replaceAll("^addon-", "addon.")
					.replaceAll("^core-", "core.");
			
			shouldEnable = yml.getValueWithDefault("enable-modules."+moduleNameWithCategory, true);
			
			yml.save();
		}

		module.setEnabled(shouldEnable);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends NamedModule> T getModule(Class<T> moduleClass) {
		return (T) moduleMap.get(moduleClass);
	}

	@Override
	public Collection<NamedModule> getModules() {
		return Collections.unmodifiableCollection(moduleMap.values());
	}
	
	@Override
	public void toggleAll(ToggleAction type) {
		for (NamedModule module : this.getModules()) {
			try {
				this.toggleModule(module.getClass(), type);
			} catch (Exception e) {
				plugin.getLogger().log(Level.SEVERE, "There was an error while toggling module "+module.getClass().getSimpleName()+" with action "+type.name(), e);
			}
		}
	}

	@Override
	public List<NamedModule> getModulesByPartialName(String partialName) {
		List<NamedModule> found = new ArrayList<>();
		
		for (Module module : plugin.getModuleManager().getModules()) {
			if (module instanceof NamedModule) {
				NamedModule namedModule = (NamedModule) module;
				if (namedModule.getName().contains(partialName) || namedModule.getName().replace("-", "").contains(partialName)) {
					found.add(namedModule);
				}
			}
		}
		return found;
	}
	
}
