package com.arzio.arziolib.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.module.NamedModule;

public class ArzioLibCommand implements CommandExecutor{

	private final ArzioLib plugin;
	
	public ArzioLibCommand(ArzioLib plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (!(sender.hasPermission("arziolib.reload"))) {
					sender.sendMessage("§cYou don't have permission to use this command.");
					return true;
				}
				
				if (args.length > 1) {
					
					List<NamedModule> modulesFound = plugin.getModuleManager().getModulesByPartialName(args[1]);
					
					if (modulesFound.isEmpty()) {
						sender.sendMessage("§cModule not found.");
						return true;
					}
					
					if (modulesFound.size() > 1) {
						sender.sendMessage("§cFound more than 1 module with this partial name.");
						return true;
					}
					
					modulesFound.get(0).reload();
					sender.sendMessage("§aModule reloaded! Check console for possible errors.");
				} else {
					sender.sendMessage("§aReloading configuration...");
					plugin.reloadConfig();
					sender.sendMessage("§aConfiguration reloaded!");
				}
				return true;
			}

			if (args[0].equalsIgnoreCase("clearground")) {
				if (!(sender.hasPermission("arziolib.clearground"))) {
					sender.sendMessage("§cYou don't have permission to use this command.");
					return true;
				}

				int amount = 0;
				for (World world : Bukkit.getWorlds()) {
					for (Entity entity : world.getEntities()) {
						CDEntityType type = CDEntityType.getTypeOf(entity);
						if (type == CDEntityType.GROUND_ITEM || type == CDEntityType.CORPSE) {
							entity.remove();
							amount++;
						}
					}
				}
				
				sender.sendMessage("§aAffected a total amount of "+amount+" entities");
				
				return true;
			}
			if (args[0].equalsIgnoreCase("clearall")) {
				if (!(sender.hasPermission("arziolib.clearall"))) {
					sender.sendMessage("§cYou don't have permission to use this command.");
					return true;
				}

				int amount = 0;
				for (World world : Bukkit.getWorlds()) {
					for (Entity entity : world.getEntities()) {
						CDEntityType type = CDEntityType.getTypeOf(entity);
						if (type != null) {
							entity.remove();
							amount++;
						}
					}
				}
				
				sender.sendMessage("§aAffected a total amount of "+amount+" entities");
				
				return true;
			}
		}
		
		sender.sendMessage("§aCommands for ArzioLib:");
		sender.sendMessage("§f/arziolib reload §8- §fReloads the module.yml file");
		sender.sendMessage("§f/arziolib reload <partial module name>§8- §fReloads the module");
		sender.sendMessage("§f/arziolib clearground §8- §fRemoves corpses and ground items");
		sender.sendMessage("§f/arziolib clearall §8- §fRemoves every CD entity");
		sender.sendMessage(" ");
		sender.sendMessage("§aMiscellaneous commands:");
		sender.sendMessage("§f/clothes §8- §fToggles other players clothes for you (FPS+)");
		sender.sendMessage("§f/particles §8- §fToggles bullet particles for you (FPS+)");
		
		return true;
	}

}
