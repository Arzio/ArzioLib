package com.arzio.arziolib.module.addon;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;

import com.arzio.arziolib.api.util.CDLootType;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-respawn-loots-on-chunk-load", defaultState = false)
public class ModuleAddonRespawnLoots extends Module {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        for (BlockState state : event.getChunk().getTileEntities()) {
            CDLootType lootType = CDLootType.getFrom(state.getBlock());
            if (lootType != null) {
                Location location = state.getLocation();
                location.getBlock().setType(Material.AIR);
                location.getBlock().setType(lootType.getMaterial().asMaterial());
            }
        }
    }

}
