package com.arzio.arziolib.module.addon;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCreativeEvent;

import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-restrict-gm-inventory-with-permission")
public class ModuleAddonRestrictGMInventoryWithPermission extends Module {

    @EventHandler
    public void onCreativeInventory(InventoryCreativeEvent event) {
        HumanEntity human = event.getWhoClicked();

        boolean hasPermission = human.hasPermission("craftingdead.restrict.gm")
                || human.hasPermission("arziolib.restrictgm");

        if (hasPermission && !human.isOp()) {
            event.setCancelled(true);
        }
    }

}
