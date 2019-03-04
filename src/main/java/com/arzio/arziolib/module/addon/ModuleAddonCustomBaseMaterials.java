package com.arzio.arziolib.module.addon;

import org.bukkit.configuration.file.FileConfiguration;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDBaseMaterial;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.config.YMLFile.ConfigurationVisitor;
import com.arzio.arziolib.module.NamedModule;

public class ModuleAddonCustomBaseMaterials extends NamedModule{

	private final YMLFile yml;
	
	public ModuleAddonCustomBaseMaterials(ArzioLib plugin) {
		super(plugin);
		yml = new YMLFile(plugin, "module_configuration/base_materials.yml");
	}

	@Override
	public void onEnable() {
		
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

	@Override
	public void onDisable() {
		// 
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "addon-custom-base-materials";
	}
	
	

}
