package com.arzio.arziolib.module.addon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_6_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginDisableEvent;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-kick-players-before-server-stop")
public class ModuleAddonKickPlayersBeforeServerStop extends Module {

	private YMLFile yml;
	private String kickMessage;

	@Override
	public void onEnable() {
        yml = new YMLFile(this.getPlugin(), "module_configuration/kick-players-before-server-stop.yml");
		this.kickMessage = yml.getValueWithDefault("kick-message", "Server restart");
		yml.save();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPluginDisable(PluginDisableEvent event){
		CraftServer craftServer = (CraftServer) Bukkit.getServer();
		boolean isServerStopping = !craftServer.getServer().isRunning();

		if (isServerStopping && Bukkit.getOnlinePlayers().length > 0){
			for (Player player : Bukkit.getOnlinePlayers()){
				player.kickPlayer(ChatColor.translateAlternateColorCodes('&', this.kickMessage));
			}
		}
	}

	public void setKickMessage(String kickMessage){
		this.kickMessage = kickMessage;
	}

	public String getKickMessage(){
		return this.kickMessage;
	}

}
