package com.arzio.arziolib.module.addon;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;
import com.arzio.arziolib.module.RepeatingTask;
import com.earth2me.essentials.Essentials;

@RegisterModule(name = "addon-create-timings-on-low-tps")
public class ModuleAddonCreateTimingsOnLowTPS extends Module {

	private YMLFile yml;
	private double threshold;
	private Essentials essentials;

	private boolean isMonitoring = false;

	@Override
	public void onEnable() {
		this.essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

		this.yml = new YMLFile(this.getPlugin(), "module_configuration/create-timings-on-low-tps.yml");
		this.threshold = this.yml.getValueWithDefault("tps-threshold", 18.3D);
		this.yml.save();
	}

	@RepeatingTask(delay = 60L, period = 60L)
	public void tick(){
		if (this.essentials.getTimer().getAverageTPS() > threshold){
			if (this.isMonitoring){
				this.isMonitoring = false;
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timings merged");

				for (Player player : CauldronUtils.getPlayersWithPermission("arziolib.warntimings")){
					player.sendMessage("§7[ArzioLib] TPS is stable again. Timings are now OFF.");
				}

				// Renames the latest timingsNUMBER.txt file
				try {
					File latestTimingsFile = getLatestTimingsFile();
					FileUtils.copyFile(latestTimingsFile, getNextAvailableLowTPSTimingsFile());
					latestTimingsFile.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			if (!this.isMonitoring){
				this.isMonitoring = true;
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timings on");

				for (Player player : CauldronUtils.getPlayersWithPermission("arziolib.warntimings")){
					player.sendMessage("§7[ArzioLib] TPS is low. Timings are now enabled.");
				}
			}
		}
	}

	private File getLatestTimingsFile(){
		File timings = new File("timings", "timings.txt");
		File lastFound = null;

		int index = 1;
		while (timings.exists()) {
			lastFound = timings;
			timings = new File("timings", "timings" + (index++) + ".txt");
		}

		return lastFound;
	}

	public File getNextAvailableLowTPSTimingsFile(){
		File file = null;
		int index = 0;
		do {
			file = new File("timings", "low-tps-timings"+ (index++) +".txt");
		} while (file.exists());
		return file;
	}

}
