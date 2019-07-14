package com.arzio.arziolib;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.Test;
import com.arzio.arziolib.api.TestCase;
import com.arzio.arziolib.api.TestHelper;
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
                for (CDEntityType type : CDEntityType.values()) {
                    
                    if (type.asBukkitType() == null) {
                        fail("Bukkit type not found for CDEntityType "+type.name());
                    }
                    
                    if (type.getNMSClass() == null) {
                        fail("NMS class not found for CDEntityType "+type.name());
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
