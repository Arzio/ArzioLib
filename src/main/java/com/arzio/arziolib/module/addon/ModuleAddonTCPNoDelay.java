package com.arzio.arziolib.module.addon;

import java.net.Socket;
import java.net.SocketException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-use-tcp-nodelay")
public class ModuleAddonTCPNoDelay extends Module {
    
	@EventHandler(ignoreCancelled = true)
	public void disableTCPNagleAlgorithm(PlayerJoinEvent event){
	    Socket socket = CauldronUtils.getPlayerSocket(event.getPlayer());
		
		try {
		    socket.setTcpNoDelay(true);
		} catch (SocketException e) {
		    e.printStackTrace();
		}
	}

}
