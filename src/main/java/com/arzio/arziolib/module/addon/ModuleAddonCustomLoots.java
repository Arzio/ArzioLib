package com.arzio.arziolib.module.addon;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import com.arzio.arziolib.api.util.CDLootType;
import com.arzio.arziolib.api.wrapper.LootChance;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.config.YMLFile.ConfigurationVisitor;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-custom-loots")
public class ModuleAddonCustomLoots extends Module{

	private YMLFile yml;
	private final Logger logger = Logger.getLogger(ModuleAddonCustomLoots.class.getSimpleName());

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		yml = new YMLFile(this.getPlugin(), "module_configuration/custom_loots.yml");
		yml.reload();
		
		if (!yml.getFile().exists()) {
			for (CDLootType type : CDLootType.values()) {
				
				yml.getConfig().set("loots."+type.getId()+".spawnfrequency", type.getLoot().getSpawnFrequency());
				for (LootChance chance : type.getLoot().getChances()) {
					yml.getConfig().set("loots."+type.getId()+".items."+chance.getItem().getId(), chance.getChance());
				}
			}
			
			yml.save();
			yml.reload();
		}
		
		final AtomicInteger amount = new AtomicInteger(0);
		for (CDLootType type : CDLootType.values()) {
			
			final CDLootType currentType = type;
			type.getLoot().removeAll();
			type.getLoot().setSpawnFrequency(yml.getConfig().getDouble("loots."+type.getId()+".spawnfrequency"));
			yml.visit("loots."+type.getId()+".items", new ConfigurationVisitor() {
				
				@Override
				public void visit(FileConfiguration config, String path, String key) {
					currentType.getLoot().addChance(Material.getMaterial(Integer.parseInt(key)), config.getDouble(path));
					amount.incrementAndGet();
				}
			});
			
		}
		logger.log(Level.INFO, "Loaded a total of "+amount.get()+" loot chances. If you want to reset the loot chances, delete the "+yml.getFile().getName()+" file and restart the server.");
	}

}
