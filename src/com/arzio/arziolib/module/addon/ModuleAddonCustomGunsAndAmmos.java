package com.arzio.arziolib.module.addon;

import org.bukkit.Material;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.wrapper.Ammo;
import com.arzio.arziolib.api.wrapper.CDItem;
import com.arzio.arziolib.api.wrapper.Gun;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.NamedModule;

import net.minecraft.server.v1_6_R3.Item;

public class ModuleAddonCustomGunsAndAmmos extends NamedModule{

	private final YMLFile yml;
	
	public ModuleAddonCustomGunsAndAmmos(ArzioLib plugin) {
		super(plugin);
		yml = new YMLFile(plugin, "module_configuration/guns_and_ammos.yml");
	}

	@Override
	public void onEnable() {
		yml.reload();
		
		for (Item item : Item.byId) {
			if (item == null) {
				continue;
			}

			@SuppressWarnings("deprecation")
			Material material = Material.getMaterial(item.id);
			CDItem cdItem = getPlugin().getItemProvider().getCDItemFrom(material);
			
			if (cdItem == null) {
				continue;
			}
			
			if (cdItem instanceof Gun) {
				Gun gun = (Gun) cdItem;
				
				gun.setBodyDamage(yml.getValueWithDefault("guns."+gun.getName()+".body_damage", gun.getBodyDamage()));
				gun.setHeadshotDamage(yml.getValueWithDefault("guns."+gun.getName()+".headshot_damage", gun.getHeadshotDamage()));
				gun.setCompatibleMagazines(CauldronUtils.intListToMaterialArray(
						yml.getValueWithDefault("guns."+gun.getName()+".compatible_ammos", 
								CauldronUtils.materialArrayToIntegerList(gun.getCompatibleAmmos()))));
				
			} else if (cdItem instanceof Ammo) {
				Ammo ammo = (Ammo) cdItem;
				
				ammo.setBulletAmount(yml.getValueWithDefault("ammo."+ammo.getName()+".bullet_amount", ammo.getBulletAmount()));
				ammo.setPenetration(yml.getValueWithDefault("ammo."+ammo.getName()+".penetration", ammo.getPenetration()));
			}
		}
		
		yml.save();
	}

	@Override
	public void onDisable() {
	}

	@Override
	public String getName() {
		return "addon-custom-gun-and-ammo-configurations";
	}

}
