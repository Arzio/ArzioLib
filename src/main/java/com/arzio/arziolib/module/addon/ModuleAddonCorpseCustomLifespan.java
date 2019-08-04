package com.arzio.arziolib.module.addon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.arzio.arziolib.api.event.EntityJoinWorldEvent;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-corpse-custom-lifespan")
public class ModuleAddonCorpseCustomLifespan extends Module{

	private YMLFile yml;
	private int lifespan;

	@Override
	public void onEnable() {
		super.onEnable();

		yml = new YMLFile(this.getPlugin(), "module_configuration/corpse_lifespan.yml");
		yml.reload();
		
		this.lifespan = yml.getValueWithDefault("corpse-max-lifespan-in-seconds", 240);
		
		yml.save();
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onCorpseJoin(EntityJoinWorldEvent event) {
		if (CDEntityType.CORPSE.isTypeOf(event.getEntity())) {
			CDClasses.entityCorpseMaxAgeTicks.setValue(CauldronUtils.getNMSEntity(event.getEntity()), this.lifespan * 20);
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

}
