package com.arzio.arziolib.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import com.arzio.arziolib.api.ForgeListener;
import com.arzio.arziolib.api.event.packet.CDPlayerDataSendEvent;
import com.arzio.arziolib.api.event.packet.CDShowBulletHitEvent;
import com.arzio.arziolib.api.util.CountdownTimer;
import com.arzio.arziolib.config.UserData;

public class MiscListener implements ForgeListener{
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onParticle(CDShowBulletHitEvent event) {
		if (UserData.getFrom(event.getPlayer()).isParticlesHidden()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onAdminJoin(PlayerJoinEvent event) {
		if (event.getPlayer().isOp()){
			final Player player = event.getPlayer();
			
			CountdownTimer timer = new CountdownTimer(null, 5L, new CountdownTimer.TimeCallback() {
				
				@Override
				public void onStart(long durationMillis) { }
				
				@Override
				public void onEnd() {
					player.sendMessage(" ");
					player.sendMessage("§a[ArzioLib] §eCheque todo dia por novas versões em meu GitHub!");
					player.sendMessage("§a[ArzioLib] §e§oCheck for new releases everyday on my GitHub!");
					player.sendMessage("§a[ArzioLib] §fhttps://github.com/Arzio/ArzioLib/releases");
					player.sendMessage(" ");
				}
			});
			
			timer.start();
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onClothes(CDPlayerDataSendEvent event) {
		if (UserData.getFrom(event.getPlayer()).isClothesHidden()) {
			if (!event.getPlayer().equals(event.getFrom())) {
				
				try(DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(event.getData()))) {
					
					inputStream.read(); // skips the first byte
					String playerName = inputStream.readUTF();
					boolean isAiming = inputStream.readBoolean();
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try (DataOutputStream dos = new DataOutputStream(baos)){
						
						dos.writeUTF(playerName);
						dos.writeBoolean(isAiming);
						dos.write(new byte[event.getData().length]); // writring an array full of zeros.
																	 // we don't need to care about the remaining array size.
						event.setData(baos.toByteArray());
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}
