package com.arzio.arziolib.command;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;

public class ThirstAllCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("arziolib.thirstall")) {
			sender.sendMessage("§cYou do not have permission.");
			return true;
		}

		PlayerDataHandler handler = ArzioLib.getInstance().getPlayerDataHandler();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.playSound(player.getLocation(), Sound.DRINK, 1F, 1F);
			handler.getPlayerData(player).setWaterLevel(20);
			player.sendMessage("§a"+sender.getName()+" healed your water level.");
		}

		return true;
	}

}
