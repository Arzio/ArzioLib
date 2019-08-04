package com.arzio.arziolib;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.Test;
import com.arzio.arziolib.api.TestCase;
import com.arzio.arziolib.api.TestHelper;
import com.arzio.arziolib.api.util.CDDamageCause;
import com.arzio.arziolib.api.util.CDEntityType;
import com.arzio.arziolib.api.util.TestException;

public class ArzioLibTests {

	public static void registerTests() {
		ArzioLib pluginFrom = ArzioLib.getInstance();
		TestHelper helper = pluginFrom.getTestHelper();
		
		// Tests CDEntityType
		helper.addTestCase(new TestCase(pluginFrom, "entitiesTest", new Test() {
			
			@Override
			public void test(CommandSender tester, Player player, TestHelper helper) throws Throwable {
				
				String testWorldName = "arziolib-testworld";
				World testWorld = Bukkit.getWorld(testWorldName);
				
				if (testWorld == null) {
					tester.sendMessage("§6Skipping CD Entity test: '"+testWorldName+"' not found.");
				} else {
					tester.sendMessage("§eFound test world "+testWorldName);
				}
				
				for (CDEntityType type : CDEntityType.values()) {
					
					if (type.asBukkitType() == null) {
						fail("Bukkit type not found for CDEntityType "+type.name());
					}
					
					if (type.getNMSClass() == null) {
						fail("NMS class not found for CDEntityType "+type.name());
					}
					
					if (type.getConstructor() == null) {
						fail("Constructor null?!");
					}
					
					if (testWorld != null) {
						Entity entity = type.spawnEntity(testWorld.getSpawnLocation());
						
						if (!entity.isValid()) {
							fail("Entity is not valid!");
						}
						if (!type.isTypeOf(entity)) {
							fail("The CD type "+type.name()+" is not the type of "+entity.getType());
						}
						
						entity.remove();
						if (entity.isValid()) {
							fail("The CD type "+type.name()+" is valid while dead!");
						}
					}
				}
			}
			
		}));
		
		// Tests damage causes
		helper.addTestCase(new TestCase(pluginFrom, "testDamageCauses", new Test() {
			
			@Override
			public void test(CommandSender tester, Player player, TestHelper helper) throws Throwable {
				for (CDDamageCause cause : CDDamageCause.values()) {
					if (cause.asBukkitDamageCause() == null) {
						fail("Bukkit damage cause not found for CDDamageCause "+cause.name());
					}
				}
			}
			
		}));
	}
	
	public static void runTests(CommandSender sender) throws TestException {
		sender.sendMessage("§7Running ArzioLib demo tests...");
		ArzioLib pluginFrom = ArzioLib.getInstance();
		TestHelper helper = pluginFrom.getTestHelper();
		
		helper.runTestCases(helper.getTestCases(pluginFrom), sender);
		sender.sendMessage("§aAll tests passed!");
	}
}
