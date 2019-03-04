package com.arzio.arziolib.module.addon;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDMaterial;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.NamedModule;

public class ModuleAddonStackableGrenades extends NamedModule{

	private final YMLFile yml;
	
	public ModuleAddonStackableGrenades(ArzioLib plugin) {
		super(plugin);
		yml = new YMLFile(plugin, "module_configuration/stackable_grenades.yml");
	}

	@Override
	public void onEnable() {
		yml.reload();
		
		for (CDMaterial material : CDMaterial.GRENADES) {
			int size = yml.getValueWithDefault("grenades."+material.name()+".max-stack-size", 1);
			CauldronUtils.setMaxStackSize(material.asMaterial(), size);
		}
		
		yml.save();
	}

	@Override
	public void onDisable() {
	}

	@Override
	public String getName() {
		return "addon-stackable-grenades";
	}

}
