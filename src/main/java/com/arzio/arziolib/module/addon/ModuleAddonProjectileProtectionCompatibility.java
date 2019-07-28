package com.arzio.arziolib.module.addon;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDDamageCause;
import com.arzio.arziolib.api.util.CDSpecialSlot;
import com.arzio.arziolib.api.wrapper.InventoryCDA;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;
import com.arzio.arziolib.config.YMLFile;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

@RegisterModule(name = "addon-projectile-protection-compatibility")
public class ModuleAddonProjectileProtectionCompatibility extends Module {
	
	private PlayerDataHandler dataHandler;
	private YMLFile yml;
	private double projectileProtectionRatio = 1.5F;
	private boolean considerBoots;
	
	@Override
	public void onEnable() {
		super.onEnable();
		
        this.yml = new YMLFile(this.getPlugin(), "module_configuration/projectile_protection_ratio.yml");
		yml.reload();
		
		this.dataHandler = ArzioLib.getInstance().getPlayerDataHandler();
		this.projectileProtectionRatio = this.yml.getValueWithDefault("protection-ratio-per-enchantment-level", this.projectileProtectionRatio);
		this.considerBoots = this.yml.getValueWithDefault("consider-boots", true);
		
		yml.save();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBulletDamage(EntityDamageByEntityEvent event){
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		if (event.getCause() == CDDamageCause.BULLET_BODY.asBukkitDamageCause() 
				|| event.getCause() == CDDamageCause.BULLET_HEADSHOT.asBukkitDamageCause()) {
			
			Player victim = (Player) event.getEntity();
			InventoryCDA inventory = this.dataHandler.getPlayerData(victim).getInventory();
			
			ItemStack stackHat = inventory.getStackInSpecialSlot(CDSpecialSlot.HAT);
			ItemStack stackVest = inventory.getStackInSpecialSlot(CDSpecialSlot.VEST);
			ItemStack stackClothing = inventory.getStackInSpecialSlot(CDSpecialSlot.CLOTHING);
			ItemStack stackBoots = victim.getInventory().getBoots();
			
			int totalProtectionLevel = 0;
			
			if (stackHat != null) {
			    totalProtectionLevel += stackHat.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
			}
			if (stackVest != null) {
			    totalProtectionLevel += stackVest.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
			}
			if (stackClothing != null) {
			    totalProtectionLevel += stackClothing.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
			}
			if (this.considerBoots && stackBoots != null) {
			    totalProtectionLevel += stackBoots.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
			}
			
			double protectionPercentage = (totalProtectionLevel * projectileProtectionRatio);
			double amountNow = event.getDamage() * (1D - (protectionPercentage / 100.0D));
			
			if (amountNow < 0D) {
				amountNow = 0D;
			}
			
			event.setDamage(amountNow);
		}
	}

    public double getProjectileProtectionRatio() {
        return projectileProtectionRatio;
    }

    public void setProjectileProtectionRatio(double projectileProtectionRatio) {
        this.projectileProtectionRatio = projectileProtectionRatio;
    }

    public boolean shouldConsiderBoots() {
        return considerBoots;
    }

    public void setConsiderBoots(boolean considerBoots) {
        this.considerBoots = considerBoots;
    }

}
