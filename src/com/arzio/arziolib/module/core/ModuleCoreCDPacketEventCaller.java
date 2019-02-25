package com.arzio.arziolib.module.core;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.bases.Base;
import com.arzio.arziolib.api.event.PostEvent;
import com.arzio.arziolib.api.event.handler.CDPacketReceivedEvent;
import com.arzio.arziolib.api.event.handler.CDPacketSentEvent;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent;
import com.arzio.arziolib.api.event.packet.CDCombatlogShowTimer;
import com.arzio.arziolib.api.event.packet.CDFlamethrowerTriggerEvent;
import com.arzio.arziolib.api.event.packet.CDGrenadeThrowEvent;
import com.arzio.arziolib.api.event.packet.CDGunReloadEvent;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.api.event.packet.CDPlayerDataSendEvent;
import com.arzio.arziolib.api.event.packet.CDRequestBaseDestroyEvent;
import com.arzio.arziolib.api.event.packet.CDShowBulletHitEvent;
import com.arzio.arziolib.api.event.packet.CDSwapGunEvent;
import com.arzio.arziolib.api.event.packet.PayloadPacketEvent;
import com.arzio.arziolib.api.util.CDPacketType;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.module.ListenerModule;

import net.minecraft.server.v1_6_R3.Entity;

public class ModuleCoreCDPacketEventCaller extends ListenerModule{

	public ModuleCoreCDPacketEventCaller(ArzioLib plugin) {
		super(plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPacketSending(CDPacketSentEvent event) {
		CDPacketType type = event.getPacketType();
		
		PayloadPacketEvent innerEvent = null;
		
		try(DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(event.getData()))) {
			
			switch (type) {
				case SHOW_PARTICLE:
					innerEvent = new CDShowBulletHitEvent(event.getPlayer(), event.getData());
					break;
				case PLAYER_DATA:
					inputStream.read(); // Skips the first byte
					Player from = Bukkit.getPlayerExact(inputStream.readUTF());
					
					innerEvent = new CDPlayerDataSendEvent(from, event.getPlayer(), event.getData());
					break;
				case COMBATLOG_SHOW_TIMER:
					innerEvent = new CDCombatlogShowTimer(event.getPlayer(), event.getData());
					break;
				default:
					break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (innerEvent != null) {
			event.callInnerPacket(innerEvent);
			
			if (!innerEvent.isCancelled() && (innerEvent instanceof PostEvent)) {
				((PostEvent) innerEvent).afterPost();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPacketReceiving(CDPacketReceivedEvent event) {
		ArzioLib plugin = ArzioLib.getInstance();
		Player sender = event.getPlayer();
		
		CDPacketType type = event.getPacketType();
		byte[] data = event.getData();
		
		PayloadPacketEvent innerEvent = null;
		
		try(DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(data))) {
			
			@SuppressWarnings("unused")
			int ignoredByte = inputStream.read(); // Not used. DO NOT ERASE IT.
			Gun heldGun = plugin.getItemProvider().getStackAs(Gun.class, sender.getItemInHand());
			
			switch(type) {
				case GUN_RELOAD:
					innerEvent = new CDGunReloadEvent(sender, heldGun, data);
					break;
				case THROW_GRENADE:
					innerEvent = new CDGrenadeThrowEvent(sender, data);
					break;
				case BASE_DESTROY:
					Base base = plugin.getBaseProvider().getBaseFromPlayer(sender);
					
					if (base == null) {
						event.setCancelled(true);
						return;
					}
					
					innerEvent = new CDRequestBaseDestroyEvent(sender, base, data);
					
					break;
				case GUN_BULLET_HIT:
					if (heldGun == null) {
						event.setCancelled(true);
						return;
					}
					
					// Checks if the player can fire.
					// This is uses the same way as CD.
					if (!plugin.getItemStackHelper().canGunFire(sender.getItemInHand())) {
						return;
					}
					
			        boolean hitTypeIsEntity = inputStream.readBoolean();

			        if (hitTypeIsEntity) {
			        	int entity = inputStream.readInt();
			        	boolean isHeadshot = inputStream.readBoolean();
			        	
				        Entity entityHit = ((CraftPlayer) sender).getHandle().world.getEntity(entity);
				        
				        if (entityHit != null) {
				        	innerEvent = new CDBulletHitEvent(sender, heldGun, entityHit.getBukkitEntity(), isHeadshot, data);
				        }
			        } else {
			            @SuppressWarnings("unused")
						int blockId = inputStream.readInt(); // Not used. DO NOT ERASE IT.
			            double x = inputStream.readDouble();
			            double y = inputStream.readDouble();
			            double z = inputStream.readDouble();
			            @SuppressWarnings("unused")
			            int side = inputStream.readInt();  // Not used. DO NOT ERASE IT.
			            double vectorX = inputStream.readDouble();
			            double vectorY = inputStream.readDouble();
			            double vectorZ = inputStream.readDouble();
			            
			            innerEvent = new CDBulletHitEvent(sender, heldGun, new Location(sender.getWorld(), vectorX, vectorY, vectorZ), sender.getWorld().getBlockAt((int) x, (int) y, (int) z), data);
			        }
					
					break;
				case GUN_TRIGGER:
					if (heldGun == null) {
						event.setCancelled(true);
						return;
					}
					
					innerEvent = new CDGunTriggerEvent(sender, heldGun, data);
					break;
				case SWAP_GUN:
					if (heldGun == null) {
						event.setCancelled(true);
						return;
					}
					
					innerEvent = new CDSwapGunEvent(sender, heldGun, data);
					break;
				case FLAMETHROWER_TRIGGER:
					innerEvent = new CDFlamethrowerTriggerEvent(sender, data);
					break;
				default:
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (innerEvent != null) {
			event.callInnerPacket(innerEvent);
			
			if (!innerEvent.isCancelled() && (innerEvent instanceof PostEvent)) {
				((PostEvent) innerEvent).afterPost();
			}
		}
	}

	@Override
	public String getName() {
		return "core-cd-packet-event-caller";
	}

}
