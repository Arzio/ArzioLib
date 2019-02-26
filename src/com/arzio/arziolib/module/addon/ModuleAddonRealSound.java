package com.arzio.arziolib.module.addon;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.api.util.CDAttachmentType;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.config.YMLFile.ConfigurationVisitor;
import com.arzio.arziolib.module.ListenerModule;
import com.arzio.arziolib.module.addon.ModuleAddonRealSound.SoundData.SoundTuple;
import com.arzio.arziolib.module.addon.ModuleAddonRealSound.SoundData.SoundType;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;

public class ModuleAddonRealSound extends ListenerModule{

	private final YMLFile yml;
	private Map<String, SoundData> gunSoundDataMap = new HashMap<>();
	private Logger logger = Logger.getLogger(ModuleAddonRealSound.class.getSimpleName());
	
	public ModuleAddonRealSound(ArzioLib plugin) {
		super(plugin);
		this.yml = new YMLFile(plugin, "module_configuration/sound_calibration.yml");
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		yml.saveDefaultFile();
		yml.reload();
		
		this.gunSoundDataMap.clear();
		yml.visit("guns", new ConfigurationVisitor() {
			
			@Override
			public void visit(FileConfiguration config, String path, String key) {
				SoundData data = new SoundData();
				
				if (config.contains(path+".normal")) {
					SoundType type = SoundType.NORMAL;
					
					data.set(type, config.getDouble(path+".normal.max"), config.getDouble(path+".normal.min"), config.getDouble(path+".normal.range"));
				}
				
				if (config.contains(path+".silenced")) {
					SoundType type = SoundType.SILENCED;
					
					data.set(type, config.getDouble(path+".silenced.max"), config.getDouble(path+".silenced.min"), config.getDouble(path+".silenced.range"));
				}
				
				gunSoundDataMap.put(key.toLowerCase(), data);
			}
		});
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	@EventHandler
	public void onTrigger(CDGunTriggerEvent event) {
		
		Gun gun = event.getHeldGun();
		SoundData sound = gunSoundDataMap.get(gun.getName().toLowerCase());
		
		if (sound == null) {
			logger.log(Level.WARNING, "Sound Calibration not found for gun "+gun.getName()+" in "+yml.getFile().getName()+" file. You can add it by yourself.");
			return;
		}
		
		Player player = event.getPlayer();
		
		
		boolean hasSilencer = ArzioLib.getInstance().getItemStackHelper().hasAttachment(event.getPlayer().getItemInHand(), CDAttachmentType.MUZZLE);
		SoundType soundType = hasSilencer ? SoundType.SILENCED : SoundType.NORMAL;
		
		SoundTuple tuple = sound.getSound(soundType);
		
		if (tuple == null) {
			logger.log(Level.WARNING, "Sound Calibration not found for gun "+gun.getName()+" in "+soundType.name()+" mode. Add the mode in "+yml.getFile().getName()+" config file by yourself.");
			return;
		}
		
		String soundToPlay = hasSilencer ? gun.getSilencedSound() : gun.getShootSound();
		final float maxVolume = tuple.getMax();
		final float minimumVolume = tuple.getMin();
		final float range = tuple.getRange();
		final float pitch = 1F;
		
		final double x = player.getLocation().getX();
		final double y = player.getLocation().getY();
		final double z = player.getLocation().getZ();
		
		for (Player p : player.getWorld().getPlayers()) {
			Location location = p.getLocation();
			
			if (Math.abs(location.getX() - x) <= range && Math.abs(location.getZ() - z) <= range){
				float distance = (float) player.getLocation().distance(p.getLocation());
				if (range > distance){
					float volumeBaixo = minimumVolume;

					float louderArea = 65F;
					if (distance < louderArea) {
						float volumeFar = ((float) 3.75F * ((float) ((float) louderArea - distance) / louderArea)) + 1F;
						volumeBaixo = minimumVolume * volumeFar;
					}

					volumeBaixo = Math.max(volumeBaixo, minimumVolume);
					if (p != player){
						sendSoundAtEntity(p, soundToPlay, distance, (int) x, (int) y, (int) z, maxVolume, pitch, volumeBaixo);
					}
				}
			}
		}
	}
	
	public static void sendSoundAtEntity(Player player, String sound, double distance, int x, int y,
			int z, double volumeMax, double pitch, double volumeMin) {
		String s = sound;
		double d0 = (double) x;
		double d1 = (double) y;
		double d2 = (double) z;
		double d3 = volumeMax;
		double d4 = pitch;
		double d5 = volumeMin;

		double d6 = d3 > 1.0D ? d3 * 16.0D : 16.0D;
		double d7 = distance;
		
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.NAMED_SOUND_EFFECT);

		if (d7 > d6) {
			if (d5 > 0.0D) {
				double d8 = d0 - player.getLocation().getX();
				double d9 = d1 - player.getLocation().getY();
				double d10 = d2 - player.getLocation().getZ();
				double d11 = Math.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
				double d12 = player.getLocation().getX();
				double d13 = player.getLocation().getY();
				double d14 = player.getLocation().getZ();
				if (d11 > 0.0D) {
					d12 += d8 / d11 * 2.0D;
					d13 += d9 / d11 * 2.0D;
					d14 += d10 / d11 * 2.0D;
				}
				StructureModifier<String> strings = packet.getStrings();
				StructureModifier<Integer> integers = packet.getIntegers();
				StructureModifier<Float> floats = packet.getFloat();
				
				strings.write(0, s);
				
				integers.write(0, (int) d12 * 8);
				integers.write(1, (int) d13 * 8);
				integers.write(2, (int) d14 * 8);
				
				floats.write(0, (float) d5);
				integers.write(3, (int) ((d4 * 63.0F) - (distance - d6)/7F));
				
				try {
					ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} else {
			StructureModifier<String> strings = packet.getStrings();
			StructureModifier<Integer> integers = packet.getIntegers();
			StructureModifier<Float> floats = packet.getFloat();
			
			strings.write(0, s);
			
			integers.write(0, (int) d0 * 8);
			integers.write(1, (int) d1 * 8);
			integers.write(2, (int) d2 * 8);
			
			floats.write(0, (float) d3);
			integers.write(3, (int) (d4 * 63.0F));
			
			try {
				ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getName() {
		return "addon-real-sound";
	}

	public static class SoundData {
		
		public static enum SoundType {
			NORMAL, SILENCED;
		}
		
		private Map<SoundType, SoundTuple> map = new HashMap<>();
		
		public static class SoundTuple {
			private float max;
			private float min;
			private float range;
			
			public SoundTuple(double max, double min, double range) {
				this.max = (float) max;
				this.min = (float) min;
				this.range = (float) range;
			}
			
			public float getMax() {
				return this.max;
			}
			
			public float getMin() {
				return this.min;
			}
			
			public float getRange() {
				return this.range;
			}
		}
		
		
		public void set(SoundType type, double max, double min, double range) {
			this.map.put(type, new SoundTuple(max, min, range));
		}
		
		public SoundTuple getSound(SoundType type) {
			return map.get(type);
		}
	}
}
