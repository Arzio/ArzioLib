package com.arzio.arziolib.module.fix;

import org.bukkit.event.EventHandler;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.packet.CDCombatlogShowTimer;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.CraftingDead;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.v1_6_R3.PlayerAbilities;

@RegisterModule(name = "fix-corpse-item-duplication")
public class ModuleFixCorpseDuplication extends Module implements IPlayerTracker{
	
    private CraftingDead craftingDead;
    
	@EventHandler
	public void onCombatLogPacket(CDCombatlogShowTimer event) {
		event.setCancelled(true);
	}

	@Override
	public void onEnable() {
	    this.craftingDead = ArzioLib.getInstance().getCraftingDeadMain();
		
		// We remove the CD tracker, and then put ours
		craftingDead.setTrackerEnabled(false);
		if (!CauldronUtils.getForgePlayerTrackers().contains(this)) {
			GameRegistry.registerPlayerTracker(this);
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();
		
		// Only disable this module if the plugin IS NOT being disabled
		// So we remove our tracker, and put back the CD's tracker
		if (this.getPlugin().isEnabled()) {
			CauldronUtils.getForgePlayerTrackers().remove(this);
			craftingDead.setTrackerEnabled(true);
		}

	}

	@Override
	public void onPlayerLogin(EntityPlayer player) {
	    try {
	        // We trick the onPlayerLogin of CD's Tracker by making the player's gamemode
	        // be CREATIVE only for this little amount of time.
	        
	        PlayerAbilities abilities = (PlayerAbilities) CDClasses.entityHumanPlayerAbilitiesField.getValue(player);
	        
	        boolean isCreativeMode = abilities.canInstantlyBuild;
	        abilities.canInstantlyBuild = true;
	        craftingDead.getPlayerTracker().onPlayerLogin(player);
	        abilities.canInstantlyBuild = isCreativeMode;
	    } catch (Throwable t) {
	        t.printStackTrace();
	    }
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		try {
	        // We trick the onPlayerLogin of CD's Tracker by making the player's gamemode
	        // be CREATIVE only for this little amount of time.
	        
		    PlayerAbilities abilities = (PlayerAbilities) CDClasses.entityHumanPlayerAbilitiesField.getValue(player);
		    
	        boolean isCreativeMode = abilities.canInstantlyBuild;
	        abilities.canInstantlyBuild = true;
	        craftingDead.getPlayerTracker().onPlayerLogout(player);
	        abilities.canInstantlyBuild = isCreativeMode;
		} catch (Throwable t) {
		    t.printStackTrace();
		}
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) { }

	@Override
	public void onPlayerRespawn(EntityPlayer player) { }

}
