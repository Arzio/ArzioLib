package com.arzio.arziolib.module.addon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleAddonZombieSpawnBlockBlacklist extends ListenerModule {
	
	private YMLFile yml;
	private List<Material> materialBlacklist = new ArrayList<>();
	
	public ModuleAddonZombieSpawnBlockBlacklist(ArzioLib plugin, boolean state) {
		super(plugin, state);
		this.yml = new YMLFile(plugin, "module_configuration/zombie_spawn_block_blacklist.yml");
	}
	
	@EventHandler
	public void onZombieSpawn(CreatureSpawnEvent event){
		if (event.getSpawnReason() != SpawnReason.NATURAL 
				&& event.getSpawnReason() != SpawnReason.DEFAULT
				&& event.getSpawnReason() != SpawnReason.CHUNK_GEN) {
			return;
		}
		
		for (CDEntityType type : CDEntityType.getZombieTypes()) {
			if (type.isTypeOf(event.getEntity())){
				Material materialBelowEntity = event.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
				
				if (materialBlacklist.contains(materialBelowEntity)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		yml.saveDefaultFile();
		yml.reload();
		
		List<Integer> idList = yml.getConfig().getIntegerList("blacklisted-blocks");
		materialBlacklist.clear();
		for (int id : idList) {
			materialBlacklist.add(Material.getMaterial(id));
		}
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public String getName() {
		return "addon-zombie-spawn-block-blacklist";
	}

}
