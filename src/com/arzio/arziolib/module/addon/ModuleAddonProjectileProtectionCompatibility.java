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
import com.arzio.arziolib.module.ListenerModule;

public class ModuleAddonProjectileProtectionCompatibility extends ListenerModule {
	
	private PlayerDataHandler dataHandler;
	private YMLFile yml;
	private double projectileProtectionRatio = 2F;
	
	public ModuleAddonProjectileProtectionCompatibility(ArzioLib plugin) {
		super(plugin);
		this.yml = new YMLFile(plugin, "module_configuration/projectile_protection_ratio.yml");
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		this.dataHandler = this.getPlugin().getPlayerDataHandler();
		this.projectileProtectionRatio = this.yml.getValueWithDefault("protection-ratio-per-level", this.projectileProtectionRatio);
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
			
			ItemStack stackHat = inventory.getStackInSpecialSlot(victim, CDSpecialSlot.HAT);
			ItemStack stackVest = inventory.getStackInSpecialSlot(victim, CDSpecialSlot.VEST);
			ItemStack stackClothing = inventory.getStackInSpecialSlot(victim, CDSpecialSlot.CLOTHING);
		
			int totalProtectionLevel = stackHat.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE)
										+ stackVest.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE)
										+ stackClothing.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
			
			double protectionPercentage = (totalProtectionLevel * projectileProtectionRatio);
			double amountNow = event.getDamage() * (1D - (protectionPercentage / 100.0D));
			
			if (amountNow < 0D) {
				amountNow = 0D;
			}
			
			event.setDamage(amountNow);
		}
	}

	@Override
	public String getName() {
		return "addon-projectile-protection-compatibility";
	}

}
