package com.arzio.arziolib.module.addon;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.Plugin;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.ai.PathfinderHearShoot;
import com.arzio.arziolib.api.ItemStackHelper;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.api.util.CDAttachmentType;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.EntityUtil;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.api.wrapper.ItemProvider;
import com.arzio.arziolib.config.UserData;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;
import com.arzio.arziolib.module.RepeatingTask;

import net.minecraft.server.v1_6_R3.EntityCreature;

@RegisterModule(name = "addon-zombie-hear-guns")
public class ModuleAddonZombieHearGuns extends Module{
	
	private Plugin plugin;
	private ItemProvider itemProvider;
	private ItemStackHelper itemStackHelper;
	
	@Override
	public void onEnable() {
		this.plugin = ArzioLib.getInstance();
		this.itemProvider = ArzioLib.getInstance().getItemProvider();
		this.itemStackHelper = ArzioLib.getInstance().getItemStackHelper();
	}
	
	@RepeatingTask(delay = 10L, period = 10L)
	public void updateSoundLevel() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			UserData data = UserData.getFrom(player);
			data.setCurrentSoundLevel(data.getCurrentSoundLevel() - 20F);
		}
	}
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		
		for (CDEntityType type : CDEntityType.getZombieTypes()) {
			if (type.isTypeOf(entity)) {
				EntityCreature creature = ((CraftCreature) event.getEntity()).getHandle();
				EntityUtil.getTargetSelector(creature).a(2, new PathfinderHearShoot(this.plugin, creature));
				break;
			}
		}
	}
	

	@EventHandler(priority = EventPriority.HIGH)
	public void onFire(CDGunTriggerEvent event) {
		
		Gun gun = this.itemProvider.getStackAs(Gun.class, event.getPlayer().getItemInHand());
		
		if (!gun.isFireBased()) {
			return;
		}
		
		boolean hasSilencer = this.itemStackHelper.hasAttachment(event.getPlayer().getItemInHand(), CDAttachmentType.MUZZLE);
		
		if (hasSilencer) {
			return;
		}
		
		double soundLevel = gun.getSoundLevel();
		
		if (soundLevel > 2.0F){
			soundLevel = 2.0F;
		}
		
		// Increases the sound level of the gun by 30 times.
		soundLevel *= 30F;
		
		UserData data = UserData.getFrom(event.getPlayer());
		if (soundLevel > data.getCurrentSoundLevel()) {
			data.setCurrentSoundLevel(soundLevel);
		}
	}
}
