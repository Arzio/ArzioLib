package com.arzio.arziolib.module.addon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.EntityJoinWorldEvent;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.ListenerModule;

public class ModuleAddonCorpseCustomLifespan extends ListenerModule{

	private final YMLFile yml;
	private int lifespan;
	
	public ModuleAddonCorpseCustomLifespan(ArzioLib plugin) {
		super(plugin);
		yml = new YMLFile(plugin, "module_configuration/corpse_lifespan.yml");
	}

	@Override
	public void onEnable() {
		super.onEnable();
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

	@Override
	public String getName() {
		return "addon-corpse-custom-lifespan";
	}

}
