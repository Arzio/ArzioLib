package com.arzio.arziolib.module.addon;

import org.bukkit.configuration.file.FileConfiguration;

import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.config.YMLFile.ConfigurationVisitor;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-custom-base-materials", defaultState = false)
public class ModuleAddonCustomBaseMaterials extends Module{

	private YMLFile yml;

	@Override
	public void onEnable() {
		this.yml = new YMLFile(this.getPlugin(), "module_configuration/base_materials.yml");
		
		for (CDBaseMaterial material : CDBaseMaterial.MATERIAL_SET){
			material.setId(yml.getValueWithDefault("material."+material.getName()+".id", material.getId()));
		}
		
		yml.visit("material", new ConfigurationVisitor() {
			
			@Override
			public void visit(FileConfiguration config, String path, String key) {
				if (CDBaseMaterial.getMaterial(key) == null) {
					CDBaseMaterial.addMaterial(key, config.getInt(path+".id"));
				}
			}
		});
		
		yml.save();
		CDBaseMaterial.pushMaterialsToCD();
	}

}
