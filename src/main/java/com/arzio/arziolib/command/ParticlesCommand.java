package com.arzio.arziolib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arzio.arziolib.config.UserData;
import com.arzio.arziolib.config.UserDataProvider;

public class ParticlesCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cOnly players can use this command.");
			return true;
		}
		
		Player player = (Player) sender;
		UserData userData = UserDataProvider.getInstance().getUserData(player);
		
		if (userData.isParticlesHidden()) {
			player.sendMessage("§aBullet particles are now visible for you.");
			userData.setParticlesHidden(false);
		} else {
			player.sendMessage("§eBullet particles are now invisible for you.");
			userData.setParticlesHidden(true);
		}
		return true;
	}

}
