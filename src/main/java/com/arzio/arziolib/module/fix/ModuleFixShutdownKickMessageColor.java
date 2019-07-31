package com.arzio.arziolib.module.fix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;

@RegisterModule(name = "fix-shutdown-kick-message-color")
public class ModuleFixShutdownKickMessageColor extends Module{

	private PacketAdapter adapter;

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event){
		// Checks if the kick reason is equals to the shutdown message
		// and then translates the color codes.
		if (event.getReason().equalsIgnoreCase(Bukkit.getServer().getShutdownMessage())){
			event.setReason(ChatColor.translateAlternateColorCodes('&', event.getReason()));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable(){
		adapter = new PacketAdapter(ArzioLib.getInstance(), ConnectionSide.SERVER_SIDE, 255) {
			
			@Override
			public void onPacketSending(PacketEvent packetEvent) {
				StructureModifier<String> structure = packetEvent.getPacket().getStrings();
				
				String kickReason = structure.read(0);

				// Checks if the kick reason is equals to the shutdown message
				// and then translates the color codes.
				if (kickReason.equalsIgnoreCase(Bukkit.getServer().getShutdownMessage())){
					structure.write(0, ChatColor.translateAlternateColorCodes('&', kickReason));
				}
			}
			
		};
		ProtocolLibrary.getProtocolManager().addPacketListener(adapter);
	}

	@Override
	public void onDisable(){
		ProtocolLibrary.getProtocolManager().removePacketListener(adapter);
	}
}
