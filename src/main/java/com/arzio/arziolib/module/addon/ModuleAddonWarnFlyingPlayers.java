package com.arzio.arziolib.module.addon;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.minecraft.server.v1_6_R3.AxisAlignedBB;
import net.minecraft.server.v1_6_R3.Block;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.MathHelper;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.config.UserData;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;
import com.arzio.arziolib.module.RepeatingTask;

@RegisterModule(name = "addon-warn-flying-players")
public class ModuleAddonWarnFlyingPlayers extends Module {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		UserData.getFrom(event.getPlayer()).resetFlightDetectionCounter();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onMove(PlayerMoveEvent event) {
		double futureYdifference = event.getTo().getY() - event.getFrom().getY();
		UserData data = UserData.getFrom(event.getPlayer());

		// This is hardcoded value in Vanilla.
		if (futureYdifference >= -0.03125D) {
			data.addFlag(UserData.FLAG_LATEST_MOVEMENT_HEIGHT_WAS_ABOVE_MC_DETECTION_THRESHOLD);
		} else {
			data.removeFlag(UserData.FLAG_LATEST_MOVEMENT_HEIGHT_WAS_ABOVE_MC_DETECTION_THRESHOLD);
		}

		// Adds the HAS_MOVED flag if the distance between 'from' and 'to' is higher
		// than 0
		if (event.getFrom().getWorld() == event.getTo().getWorld()) {
			if (event.getFrom().distance(event.getTo()) > 0D) {
				data.addFlag(UserData.FLAG_HAS_MOVED_SINCE_LAST_FLY_CHECK);
			}
		}

	}

	@RepeatingTask(delay = 5L, period = 5L)
	public void checkTask() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			UserData data = UserData.getFrom(player);

			if (!isCollidedWithAnyBlock(player) && !player.getAllowFlight()) {
				boolean wasAbove = data.hasFlag(UserData.FLAG_LATEST_MOVEMENT_HEIGHT_WAS_ABOVE_MC_DETECTION_THRESHOLD);
				boolean hasMoved = data.hasFlag(UserData.FLAG_HAS_MOVED_SINCE_LAST_FLY_CHECK);

				if (wasAbove && hasMoved) {
					data.removeFlag(UserData.FLAG_HAS_MOVED_SINCE_LAST_FLY_CHECK);

					data.increaseFlightDetectionCounter();
					if (data.getFlightDetectionCounter() > 8) {
						data.resetFlightDetectionCounter();
						for (Player playerToWarn : CauldronUtils.getPlayersWithPermission("arziolib.warnflyhackers")) {
							playerToWarn.sendMessage(" ");
							playerToWarn.sendMessage(" " + player.getName() + " is flyhacking!");
							playerToWarn.sendMessage(" ");

							playerToWarn.playSound(playerToWarn.getLocation(), Sound.WITHER_SHOOT, 2, 2);
						}

						Bukkit.getConsoleSender().sendMessage(player.getName() + " is flyhacking!");
					}
				}
			} else {
				data.resetFlightDetectionCounter();
			}
		}
	}

	public boolean isCollidedWithAnyBlock(Player player) {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

		// Constant MC value. I don't know why it have this value,
		// but the game uses it to check if the player is flying.
		float f4 = 0.0625F;
		AxisAlignedBB axisalignedbb = entityPlayer.boundingBox.clone().grow(f4, f4, f4).a(0.0D, -0.55D, 0.0D);

		int minX = MathHelper.floor(axisalignedbb.a);
		int maxX = MathHelper.floor(axisalignedbb.d + 1.0D);
		int minY = MathHelper.floor(axisalignedbb.b);
		int maxY = MathHelper.floor(axisalignedbb.e + 1.0D);
		int minZ = MathHelper.floor(axisalignedbb.c);
		int maxZ = MathHelper.floor(axisalignedbb.f + 1.0D);

		// MC logic. Again, don't ask me.
		if (axisalignedbb.a < 0.0D) {
			minX--;
		}
		if (axisalignedbb.b < 0.0D) {
			minY--;
		}
		if (axisalignedbb.c < 0.0D) {
			minZ--;
		}

		World world = player.getWorld();

		for (int x = minX; x < maxX; x++) {
			for (int y = minY - 2; y < maxY; y++) {
				for (int z = minZ; z < maxZ; z++) {
					Block block = Block.byId[world.getBlockTypeIdAt(x, y, z)];

					if (block != null) {
						int blockHeightTop = y + (int) block.x();

						// Block height touches the minY
						if (blockHeightTop >= minY) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
