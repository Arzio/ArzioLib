package com.arzio.arziolib.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.ForgeListener;
import com.arzio.arziolib.api.UpdateChecker.UpdateState;
import com.arzio.arziolib.api.event.packet.CDPlayerDataSendEvent;
import com.arzio.arziolib.api.event.packet.CDShowBulletHitEvent;
import com.arzio.arziolib.api.util.CountdownTimer;
import com.arzio.arziolib.config.UserData;

public class MiscListener implements ForgeListener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onParticle(CDShowBulletHitEvent event) {
		if (UserData.getFrom(event.getPlayer()).isParticlesHidden()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPluginUnload(PluginDisableEvent event) {
		ArzioLib.getInstance().getTestHelper().clearTestCases(event.getPlugin());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onAdminJoin(PlayerJoinEvent event) {
		// As some servers does not uses OP, we check for any common permission
		if (event.getPlayer().isOp() || event.getPlayer().hasPermission("essentials.gamemode")) {
			final Player player = event.getPlayer();

			if (ArzioLib.getInstance().getUpdateChecker().getState() == UpdateState.NEEDS_UPDATE) {
				CountdownTimer timer = new CountdownTimer(ArzioLib.getInstance(), 5L,
						new CountdownTimer.TimeCallback() {

							@Override
							public void onStart(long durationMillis) {
							}

							@Override
							public void onEnd() {
								if (!player.isValid()) {
									return;
								}

								String latestVersion = ArzioLib.getInstance().getUpdateChecker().getLatestVersionTag();

								player.sendMessage(" ");
								player.sendMessage("§a[ArzioLib] §eA atualização " + latestVersion
										+ " está pronta para ser baixada!");
								player.sendMessage(
										"§a[ArzioLib] §e§oThe " + latestVersion + " update is ready to be downloaded!");
								player.sendMessage(
										"§a[ArzioLib] §fDownload it here NOW: §bhttps://github.com/Arzio/ArzioLib/releases");
								player.sendMessage(" ");

								player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
							}
						});

				timer.start();
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onClothes(CDPlayerDataSendEvent event) {
		if (UserData.getFrom(event.getPlayer()).isClothesHidden()) {
			if (!event.getPlayer().equals(event.getFrom())) {

				try (DataInputStream inputStream = new DataInputStream(
						new ByteArrayInputStream(event.getData().getInnerPacketData()))) {

					String playerName = inputStream.readUTF();
					boolean isAiming = inputStream.readBoolean();

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try (DataOutputStream dos = new DataOutputStream(baos)) {

						dos.writeUTF(playerName);
						dos.writeBoolean(isAiming);
						dos.write(new byte[event.getData().getInnerPacketData().length]); // writing an array full of
																							// zeros.
																							// we don't need to care
																							// about the remaining array
																							// size.
						event.getData().setInnerPacketData(baos.toByteArray());
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

}
