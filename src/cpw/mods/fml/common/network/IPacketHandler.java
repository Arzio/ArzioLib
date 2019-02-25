package cpw.mods.fml.common.network;

import net.minecraft.server.v1_6_R3.INetworkManager;
import net.minecraft.server.v1_6_R3.Packet250CustomPayload;

/**
 * This class is only for tricking the classpath in order to fix some compilation errors.
 * This class already exists in FML.
 * 
 * @author Arzio
 *
 */
public interface IPacketHandler {

	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player);
}
