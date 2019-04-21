package com.arzio.arziolib.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arzio.arziolib.ArzioLib;

public class ThirstCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cThis command can only be used by players.");
			return true;
		}
		
		if (!sender.hasPermission("arziolib.thirst")) {
			sender.sendMessage("§cYou do not have permission.");
			return true;
		}
		
		Player player = (Player) sender;
		player.playSound(player.getLocation(), Sound.DRINK, 1F, 1F);
		
		ArzioLib.getInstance().getPlayerDataHandler().getPlayerData(player).setWaterLevel(20);

		return true;
	}
}
