package com.arzio.arziolib.module.addon;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.wrapper.Ammo;
import com.arzio.arziolib.api.wrapper.CDItem;
import com.arzio.arziolib.api.wrapper.CDSharedItem;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

import net.minecraft.server.v1_6_R3.Item;

@RegisterModule(name = "addon-custom-gun-and-ammo-configurations", defaultState = false)
public class ModuleAddonCustomGunsAndAmmos extends Module {

	private YMLFile yml;

	@Override
	public void onEnable() {
		yml = new YMLFile(this.getPlugin(), "module_configuration/guns_and_ammos.yml");
		yml.reload();
		
		for (Item item : Item.byId) {
			if (item == null) {
				continue;
			}

			@SuppressWarnings("deprecation")
			Material material = Material.getMaterial(item.id);
			CDItem cdItem = ArzioLib.getInstance().getItemProvider().getCDItemFrom(material);
			
			if (cdItem == null) {
				continue;
			}
			
			if (cdItem instanceof Gun) {
				Gun gun = (Gun) cdItem;
				
				gun.setAccuracy(yml.getValueWithDefault("guns."+gun.getName()+".accuracy", gun.getAccuracy()));
				gun.setBodyDamage(yml.getValueWithDefault("guns."+gun.getName()+".body_damage", gun.getBodyDamage()));
				gun.setBulletsPerRound(yml.getValueWithDefault("guns."+gun.getName()+".bullets_per_round", gun.getBulletsPerRound()));
				gun.setCompatibleMagazines(CauldronUtils.intListToMaterialArray(
						yml.getValueWithDefault("guns."+gun.getName()+".compatible_ammos", 
								CauldronUtils.materialArrayToIntegerList(gun.getCompatibleAmmos()))));
				gun.setHeadshotDamage(yml.getValueWithDefault("guns."+gun.getName()+".headshot_damage", gun.getHeadshotDamage()));
				gun.setMaximumBulletDistance(yml.getValueWithDefault("guns."+gun.getName()+".maximum_bullet_distance", gun.getMaximumBulletDistance()));
				gun.setMovementPenalty(yml.getValueWithDefault("guns."+gun.getName()+".movement_penalty", gun.getMovementPenalty()));
				gun.setRecoil(yml.getValueWithDefault("guns."+gun.getName()+".recoil", gun.getRecoil()));
				gun.setRPM(yml.getValueWithDefault("guns."+gun.getName()+".rpm", gun.getRPM()));
				gun.setZoomLevel(yml.getValueWithDefault("guns."+gun.getName()+".zoom_level", gun.getZoomLevel()));
				gun.setSpreadWhileAiming(yml.getValueWithDefault("guns."+gun.getName()+".spread_while_aiming", gun.canSpreadWhileAiming()));
				gun.setCrosshair(yml.getValueWithDefault("guns."+gun.getName()+".crosshair", gun.hasCrosshair()));
				
			} else if (cdItem instanceof Ammo) {
				Ammo ammo = (Ammo) cdItem;
				
				ammo.setBulletAmount(yml.getValueWithDefault("ammo."+ammo.getName()+".bullet_amount", ammo.getBulletAmount()));
				ammo.setPenetration(yml.getValueWithDefault("ammo."+ammo.getName()+".penetration", ammo.getPenetration()));
			}
			
			if (cdItem instanceof CDSharedItem) {
				CDSharedItem sharedItem = (CDSharedItem) cdItem;
				
				// Updates item stats for all players
				for (Player player : Bukkit.getOnlinePlayers()) {
					sharedItem.updateStats(player);
				}
			}
		}
		
		yml.save();
	}

}
