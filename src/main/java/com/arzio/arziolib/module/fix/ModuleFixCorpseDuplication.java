package com.arzio.arziolib.module.fix;

import org.bukkit.event.EventHandler;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.packet.CDCombatlogShowTimer;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.module.ListenerModule;

import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;

public class ModuleFixCorpseDuplication extends ListenerModule implements IPlayerTracker{

	public ModuleFixCorpseDuplication(ArzioLib plugin) {
		super(plugin);
	}

	@Override
	public String getName() {
		return "fix-corpse-item-duplication";
	}
	
	@EventHandler
	public void onCombatLogPacket(CDCombatlogShowTimer event) {
		event.setCancelled(true);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		
		// We remove the CD tracker, and then put ours
		this.getPlugin().getCraftingDeadMain().setTrackerEnabled(false);
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
			getPlugin().getCraftingDeadMain().setTrackerEnabled(true);
		}

	}

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		
		// We trick the onPlayerLogin of CD's Tracker by making the player's gamemode
		// be CREATIVE only for this little amount of time.
		
		boolean isCreativeMode = player.abilities.canInstantlyBuild;
		player.abilities.canInstantlyBuild = true;
		this.getPlugin().getCraftingDeadMain().getPlayerTracker().onPlayerLogin(player);
		player.abilities.canInstantlyBuild = isCreativeMode;
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		
		// We trick the onPlayerLogin of CD's Tracker by making the player's gamemode
		// be CREATIVE only for this little amount of time.
		
		boolean isCreativeMode = player.abilities.canInstantlyBuild;
		player.abilities.canInstantlyBuild = true;
		this.getPlugin().getCraftingDeadMain().getPlayerTracker().onPlayerLogout(player);
		player.abilities.canInstantlyBuild = isCreativeMode;
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) { }

	@Override
	public void onPlayerRespawn(EntityPlayer player) { }

}
