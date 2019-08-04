package com.arzio.arziolib.api.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.CDDefineNametagsEvent;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.CDSharedItem;
import com.arzio.arziolib.api.wrapper.impl.CDSharedItemImpl;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import net.minecraft.server.v1_6_R3.Packet;

public class CDPacketHelper {

	public static boolean sendNametagPacket(Player player, boolean isHidden) {
		Validate.notNull(player, "Player must not be null!");
		Set<String> whitelist = new HashSet<>();
		
		CDDefineNametagsEvent event = new CDDefineNametagsEvent(player, isHidden, whitelist);
		Bukkit.getPluginManager().callEvent(event);
		
		if (event.isCancelled()) {
			return false;
		}
		
		try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				DataOutputStream data = new DataOutputStream(bytes)){
			
			int packetId = (int) CDClasses.networkManagerGetPacketIdFromClass.invoke(null, CDClasses.packetNametagVisibilityClass.getReferencedClass());
			
			data.write(packetId);
			data.writeBoolean(isHidden);
			data.writeByte(whitelist.size());
			for (String playerName : whitelist) {
				StringHelper.writeString(data, playerName.toLowerCase());
			}
			
			sendCustomPayload(player, bytes.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static void sendCustomPayload(Player player, byte[] data) {
		Validate.notNull(player, "Player must not be null!");
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD);
		packet.getStrings().write(0, ArzioLib.MOD_NETWORK_ID);
		packet.getByteArrays().write(0, data);
		packet.getIntegers().write(0, data.length);

		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendPacket(Player player, Packet packet) {
		Validate.notNull(player, "Player must not be null!");
		CraftPlayer cPlayer = (CraftPlayer) player;
		cPlayer.getHandle().playerConnection.sendPacket(packet);
	}
	
	public static void updateSharedItems(Player player, CDSharedItem... items) {
		Validate.notNull(player, "Player must not be null!");
		List<CDSharedItem> itemsToBuild = new ArrayList<>();
		
		for (CDSharedItem item : items) {
			itemsToBuild.add(item);
		}
		
		updateSharedItems(player, itemsToBuild);
	}
	
	public static void updateSharedItems(Player player, List<CDSharedItem> items) {
		Validate.notNull(player, "Player must not be null!");
		List builtItems = new ArrayList<>();
		
		for (CDSharedItem item : items) {
			builtItems.add(((CDSharedItemImpl) item).getItemInstance());
		}
		
		Packet packet = (Packet) CDClasses.packetSyncItemsBuildItemsMethod.invoke(null, builtItems);
		CDPacketHelper.sendPacket(player, packet);
	}
}
