package com.arzio.arziolib.module.fix;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.arzio.arziolib.api.event.CDPlayerPreDropEvent;
import com.arzio.arziolib.api.util.reflection.ReflectionHelper;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

@RegisterModule(name = "fix-death-drops-compatibility")
public class ModuleFixDeathDropsCompatibility extends Module{
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().startsWith("/gamerule")) {
			if (event.getMessage().contains("keepInventory") || event.getMessage().contains("keepinventory")) {
				event.getPlayer().sendMessage("§cPLease, use '/rg flag keep-inventory' instead of /gamerule.");
				event.setCancelled(true);
			}
		}
	}

	@ForgeSubscribe(priority = EventPriority.HIGHEST)
	public void onDeath(LivingDeathEvent event) {
		Entity entity = ReflectionHelper.getValueFromEvent(event, Entity.class);
		
		if (!(entity instanceof EntityPlayer)) {
			return;
		}
		
		EntityPlayer ePlayer = (EntityPlayer) entity;
		Player player = ePlayer.getBukkitEntity();
		
		CDPlayerPreDropEvent innerEvent = new CDPlayerPreDropEvent(player);
		Bukkit.getPluginManager().callEvent(innerEvent);
		
		player.getWorld().setGameRuleValue("keepInventory", Boolean.toString(innerEvent.shouldKeepInventory()));
	}

}
