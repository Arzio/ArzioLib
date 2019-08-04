package cpw.mods.fml.common;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Another tricky way to make Forge classes work fine in Cauldron.
 */
public interface IPlayerTracker {
	void onPlayerLogin(EntityPlayer player);

	void onPlayerLogout(EntityPlayer player);

	void onPlayerChangedDimension(EntityPlayer player);

	void onPlayerRespawn(EntityPlayer player);
}
