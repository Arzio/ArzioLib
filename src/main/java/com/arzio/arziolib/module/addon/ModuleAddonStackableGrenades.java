package com.arzio.arziolib.module.addon;

import com.arzio.arziolib.api.util.CDMaterial;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-stackable-grenades")
public class ModuleAddonStackableGrenades extends Module{

	private YMLFile yml;

	@Override
	public void onEnable() {
        yml = new YMLFile(this.getPlugin(), "module_configuration/stackable_grenades.yml");
		yml.reload();
		
		for (CDMaterial material : CDMaterial.GRENADES) {
			int size = yml.getValueWithDefault("grenades."+material.name()+".max-stack-size", 1);
			CauldronUtils.setMaxStackSize(material.asMaterial(), size);
		}
		
		yml.save();
	}

}
