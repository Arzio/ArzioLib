package com.arzio.arziolib.module.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.handler.CDPacketReceivedEvent;
import com.arzio.arziolib.api.event.handler.CDPacketSentEvent;
import com.arzio.arziolib.api.exception.CDAReflectionException;
import com.arzio.arziolib.api.util.CDPacketDataWrapper;
import com.arzio.arziolib.module.NamedModule;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import cpw.mods.fml.common.network.IPacketHandler;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.INetworkManager;
import net.minecraft.server.v1_6_R3.Packet250CustomPayload;

public class ModuleCoreNetworkHandler extends NamedModule implements IPacketHandler{

	private List<IPacketHandler> serverHandlers = new ArrayList<>();
	private final PacketAdapter adapter;
	private Method getMethod = null;
	private Method removeAllMethod = null;
	private Method putMethod = null;
	private Object serverPacketHandlers;
	
	public ModuleCoreNetworkHandler(ArzioLib plugin) {
		super(plugin);
		try {
			this.detectMethodsAndFields();
		} catch (Exception e) {
			throw new CDAReflectionException(e);
		}
		adapter = new PacketAdapter(plugin, ListenerPriority.NORMAL, new PacketType[] { PacketType.Play.Server.CUSTOM_PAYLOAD }) {
			
			@Override
			public void onPacketSending(PacketEvent packetEvent) {
				if (!packetEvent.getPacket().getStrings().read(0).equals(ArzioLib.MOD_NETWORK_ID)) {
					return;
				}

				byte[] bruteBytes = packetEvent.getPacket().getByteArrays().read(0);
				
				CDPacketSentEvent innerEvent = new CDPacketSentEvent(packetEvent.getPlayer(), new CDPacketDataWrapper(bruteBytes));
				Bukkit.getPluginManager().callEvent(innerEvent);
				
				if (innerEvent.isCancelled()) {
					packetEvent.setCancelled(true);
				} else {
					CDPacketDataWrapper dataWrapper = innerEvent.getData();
					byte[] result = dataWrapper.buildCustomPayloadData();
					
					packetEvent.getPacket().getByteArrays().write(0, result);
					packetEvent.getPacket().getModifier().write(1, result.length);
				}
			}
			
		};
	}
	
	private void detectMethodsAndFields() throws Exception {
		Class<?> registryClass = Class.forName("cpw.mods.fml.common.network.NetworkRegistry");
		Field packetHandlerField = registryClass.getDeclaredField("serverPacketHandlers");
		packetHandlerField.setAccessible(true);
		
		Field instanceField = registryClass.getDeclaredField("INSTANCE");
		instanceField.setAccessible(true);
		
		serverPacketHandlers = packetHandlerField.get(instanceField.get(null));
		
		// Searches for the necessary methods
		for (Method m : serverPacketHandlers.getClass().getDeclaredMethods()) {
			if (m.getName().equals("get") && m.getParameterTypes().length == 1) {
				getMethod = m;
			}
			if (m.getName().equals("removeAll") && m.getParameterTypes().length == 1) {
				removeAllMethod = m;
			}
			if (m.getName().equals("put") && m.getParameterTypes().length == 2) {
				putMethod = m;
			}
		}
	}
	
	private void injectWrapper() throws Exception {
		@SuppressWarnings("unchecked")
		Collection<IPacketHandler> handlers = (Collection<IPacketHandler>) getMethod.invoke(serverPacketHandlers, ArzioLib.MOD_NETWORK_ID);
		serverHandlers.clear();
		serverHandlers.addAll(handlers);
		
		removeAllMethod.invoke(serverPacketHandlers, ArzioLib.MOD_NETWORK_ID);
		putMethod.invoke(serverPacketHandlers, ArzioLib.MOD_NETWORK_ID, this);
	}
	
	private void removeWrapper() throws Exception {
		removeAllMethod.invoke(serverPacketHandlers, ArzioLib.MOD_NETWORK_ID);
		putMethod.invoke(serverPacketHandlers, ArzioLib.MOD_NETWORK_ID, serverHandlers);
	}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, cpw.mods.fml.common.network.Player player) {
		try { // We need to try-catch the maximum possible, otherwise some players will lose connection due to plugin bugs.
			EntityPlayer entityPlayer = (EntityPlayer) player;
			
			CDPacketReceivedEvent innerEvent = new CDPacketReceivedEvent(entityPlayer.getBukkitEntity(), new CDPacketDataWrapper(packet.data));
			Bukkit.getPluginManager().callEvent(innerEvent);
			
			if (innerEvent.isCancelled()) {
				return; // Drops the packet. Forge will never knows this packet was received.
			}
			
			packet.data = innerEvent.getData().buildCustomPayloadData();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		// Calls the wrapped (children) packet handlers, including the mod's one
		for (IPacketHandler packetHandler : serverHandlers) {
			packetHandler.onPacketData(manager, packet, player);
		}
	}

	@Override
	protected void onEnable() {
		ProtocolLibrary.getProtocolManager().addPacketListener(adapter);
		try {
			this.injectWrapper();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDisable() {
		ProtocolLibrary.getProtocolManager().removePacketListener(adapter);
		try {
			this.removeWrapper();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return "core-network-handler";
	}
}
