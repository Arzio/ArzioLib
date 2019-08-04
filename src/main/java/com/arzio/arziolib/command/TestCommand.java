package com.arzio.arziolib.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.TestCase;
import com.arzio.arziolib.api.TestHelper;
import com.arzio.arziolib.api.util.StringHelper;
import com.arzio.arziolib.api.util.TestException;

public class TestCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (!(sender.hasPermission("arziolib.test"))) {
			sender.sendMessage("§cYou don't have permission.");
			return true;
		}
		
		if (args.length < 1) {
			sender.sendMessage("§aTest commands:");
			sender.sendMessage("§f/test <TestCase name> §8- §fTests this test case");
			sender.sendMessage("§f/test group <group name> §8- §fTests all test cases from a group");
			sender.sendMessage("§f/test all <plugin name> §8- §fTests all test cases from a plugin");
			sender.sendMessage("§aAlias: §f/te");
			return true;
		}
		
		TestHelper testHelper = ArzioLib.getInstance().getTestHelper();
		String whichTest = args[0];
		
		if (whichTest.equalsIgnoreCase("all")) {
			
			if (args.length < 2) {
				sender.sendMessage("§cWrong input. Try '/test all <plugin name>'");
				return true;
			}
			
			String pluginName = StringHelper.appendStringFromIndex(args, 1, args.length);
			Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
			
			if (plugin == null) {
				sender.sendMessage("§cPlugin not found.");
				return true;
			}
			
			try {
				sender.sendMessage("§7Running all test cases from "+plugin.getName()+"...");
				testHelper.runTestCases(plugin, sender);
				sender.sendMessage("§aPassed all test cases from plugin "+plugin.getName()+"!");
			} catch (TestException e) {
				e.printStackTrace(sender);
			}
			
		} else if (whichTest.equalsIgnoreCase("group")){
		
			if (args.length < 2) {
				sender.sendMessage("§cWrong input. Try '/test group <group name>'");
				return true;
			}
			
			String groupName = StringHelper.appendStringFromIndex(args, 1, args.length);
			
			try {
				sender.sendMessage("§7Running all test cases from group "+groupName+"...");
				testHelper.runTestCases(groupName, sender);
				sender.sendMessage("§aPassed all test cases from group "+groupName+"!");
			} catch (TestException e) {
				e.printStackTrace(sender);
			}
		
		} else {
			TestCase testCase = testHelper.getTestCase(whichTest);
			if (testCase == null) {
				sender.sendMessage("§cTest case not found.");
				return true;
			}
			
			try {
				sender.sendMessage("§7Running test "+testCase.getCommandName()+"...");
				testCase.run(sender);
				sender.sendMessage("§aTest "+testCase.getCommandName()+" passed!");
			} catch (TestException e) {
				e.printStackTrace(sender);
			}
		}
		
		return true;
	}

}
