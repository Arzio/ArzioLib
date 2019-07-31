package com.arzio.arziolib.module.addon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_6_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginDisableEvent;
import org.spigotmc.SpigotConfig;

import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-kick-players-before-server-stop")
public class ModuleAddonKickPlayersBeforeServerStop extends Module {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPluginDisable(PluginDisableEvent event){
		CraftServer craftServer = (CraftServer) Bukkit.getServer();
		boolean isServerStopping = !craftServer.getServer().isRunning();

		if (isServerStopping){
			for (Player player : Bukkit.getOnlinePlayers()){
				player.kickPlayer(ChatColor.translateAlternateColorCodes('&', SpigotConfig.restartMessage));
			}
		}
	}

}
