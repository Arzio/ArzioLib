package com.arzio.arziolib.module.addon;

import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.ai.PathfinderHearShoot;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.config.UserData;
import com.arzio.arziolib.config.UserDataProvider;
import com.arzio.arziolib.module.ListenerModule;

import net.minecraft.server.v1_6_R3.EntityCreature;

public class ModuleAddonZombieHearGuns extends ListenerModule{

	public ModuleAddonZombieHearGuns(ArzioLib plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		
		for (CDEntityType type : CDEntityType.getZombieTypes()) {
			if (type.isTypeOf(entity)) {
				EntityCreature creature = ((CraftCreature) event.getEntity()).getHandle();
				EntityUtil.getTargetSelector(creature).a(2, new PathfinderHearShoot(creature));
				break;
			}
		}
	}
	

	@EventHandler(priority = EventPriority.HIGH)
	public void onFire(CDGunTriggerEvent event) {
		
		Gun gun = ArzioLib.getInstance().getItemProvider().getStackAs(Gun.class, event.getPlayer().getItemInHand());
		
		if (!gun.isFireBased()) {
			return;
		}
		
		float soundLevel = gun.getSoundLevel();
		
		if (soundLevel > 2.0F){
			soundLevel = 2.0F;
		}
		
		UserData data = UserDataProvider.getInstance().getUserData(event.getPlayer());
		if (soundLevel > data.getCurrentSoundLevel()) {
			data.setCurrentSoundLevel(soundLevel);
		}
	}

	@Override
	public String getName() {
		return "addon-zombie-hear-guns";
	}
}
