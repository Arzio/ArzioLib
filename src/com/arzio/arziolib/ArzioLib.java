package com.arzio.arziolib;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.arzio.arziolib.api.ForgeBukkitEventManager;
import com.arzio.arziolib.api.ItemStackHelper;
import com.arzio.arziolib.api.bases.BaseProvider;
import com.arzio.arziolib.api.bases.impl.BaseProviderImpl;
import com.arzio.arziolib.api.impl.ItemStackHelperImpl;
import com.arzio.arziolib.api.util.reflection.ReflectionHelper;
import com.arzio.arziolib.api.impl.ForgeBukkitEventManagerImpl;
import com.arzio.arziolib.api.wrapper.CraftingDead;
import com.arzio.arziolib.api.wrapper.ItemProvider;
import com.arzio.arziolib.api.wrapper.LootProvider;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;
import com.arzio.arziolib.api.wrapper.impl.CraftingDeadMainImpl;
import com.arzio.arziolib.api.wrapper.impl.ItemProviderImpl;
import com.arzio.arziolib.api.wrapper.impl.LootProviderImpl;
import com.arzio.arziolib.api.wrapper.impl.PlayerDataHandlerImpl;
import com.arzio.arziolib.command.ArzioLibCommand;
import com.arzio.arziolib.command.ClothesCommand;
import com.arzio.arziolib.command.ParticlesCommand;
import com.arzio.arziolib.config.ArzioModuleManager;
import com.arzio.arziolib.config.UserDataProvider;
import com.arzio.arziolib.listener.MiscListener;
import com.arzio.arziolib.module.ModuleManager;
import com.arzio.arziolib.module.ModuleManager.ToggleAction;
import com.arzio.arziolib.module.addon.ModuleAddonBiomeChanger;
import com.arzio.arziolib.module.addon.ModuleAddonCustomBaseMaterials;
import com.arzio.arziolib.module.addon.ModuleAddonCustomFlags;
import com.arzio.arziolib.module.addon.ModuleAddonCustomGunsAndAmmos;
import com.arzio.arziolib.module.addon.ModuleAddonCustomLoots;
import com.arzio.arziolib.module.addon.ModuleAddonInfinityEnchantCompatiblity;
import com.arzio.arziolib.module.addon.ModuleAddonProjectileProtectionCompatibility;
import com.arzio.arziolib.module.addon.ModuleAddonRealSound;
import com.arzio.arziolib.module.addon.ModuleAddonZombieFollowGrenades;
import com.arzio.arziolib.module.addon.ModuleAddonZombieHearGuns;
import com.arzio.arziolib.module.core.ModuleCoreBukkitEventsForBases;
import com.arzio.arziolib.module.core.ModuleCoreCDPacketEventCaller;
import com.arzio.arziolib.module.core.ModuleCoreCallEntityJoinWorldEvent;
import com.arzio.arziolib.module.core.ModuleCoreNetworkHandler;
import com.arzio.arziolib.module.fix.ModuleFixArmorBreaking;
import com.arzio.arziolib.module.fix.ModuleFixCorpseDuplication;
import com.arzio.arziolib.module.fix.ModuleFixDamageSourceDetection;
import com.arzio.arziolib.module.fix.ModuleFixDeathDropsCompatibility;
import com.arzio.arziolib.module.fix.ModuleFixGoldenAppleDuplication;
import com.arzio.arziolib.module.fix.ModuleFixGrenadeThrowPosition;
import com.arzio.arziolib.module.fix.ModuleFixGunDamageOnServerFreeze;
import com.arzio.arziolib.module.fix.ModuleFixInvisibleEntities;
import com.arzio.arziolib.module.fix.ModuleFixItemUsageOnRegions;
import com.arzio.arziolib.module.fix.ModuleFixSniperFastShoot;
import com.arzio.arziolib.module.fix.ModuleFixSwapGunFastShoot;
import com.arzio.arziolib.module.fix.ModuleFixZombieHeight;
import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import cpw.mods.fml.common.ModContainer;

public class ArzioLib extends JavaPlugin {

	public static final ModContainer MOD_CONTAINER = ReflectionHelper.getCraftingDeadModContainer();
	public static final String MOD_RESOURCE_NAME = "craftingdead:";
	
	private static ArzioLib instance;
	private ModuleManager moduleManager;
	private ItemStackHelper itemStackHelper;
	private UserDataProvider userDataProvider;
	private BaseProvider baseProvider;
	private PlayerDataHandler playerDataHandler;
	private LootProvider lootProvider;

	// CD Wrappers
	private CraftingDead craftingDead;
	private ItemProvider itemProvider;

	// Forge-Bukkit compatible listener
	private ForgeBukkitEventManager forgeBukkitEventManager;

	@Override
	public void onEnable() {
		instance = this;
		
		this.userDataProvider = new UserDataProvider();
		this.playerDataHandler = new PlayerDataHandlerImpl();
		this.forgeBukkitEventManager = new ForgeBukkitEventManagerImpl(this);
		this.itemProvider = new ItemProviderImpl();
		this.craftingDead = new CraftingDeadMainImpl();
		this.itemStackHelper = new ItemStackHelperImpl();
		this.baseProvider = new BaseProviderImpl();
		this.lootProvider = new LootProviderImpl();
		
		// Initiate the module manager
		this.moduleManager = new ArzioModuleManager(this);
		
		// Addons
		this.moduleManager.registerModule(new ModuleAddonBiomeChanger(this));
		this.moduleManager.registerModule(new ModuleAddonCustomBaseMaterials(this));
		this.moduleManager.registerModule(new ModuleAddonCustomFlags(this));
		this.moduleManager.registerModule(new ModuleAddonCustomGunsAndAmmos(this));
		this.moduleManager.registerModule(new ModuleAddonCustomLoots(this));
		this.moduleManager.registerModule(new ModuleAddonInfinityEnchantCompatiblity(this));
		this.moduleManager.registerModule(new ModuleAddonProjectileProtectionCompatibility(this));
		this.moduleManager.registerModule(new ModuleAddonRealSound(this));
		this.moduleManager.registerModule(new ModuleAddonZombieFollowGrenades(this));
		this.moduleManager.registerModule(new ModuleAddonZombieHearGuns(this));
		
		// Core - DO NOT DISABLE THEM UNLESS YOU KNOW YOU ARE DOING
		this.moduleManager.registerModule(new ModuleCoreBukkitEventsForBases(this, baseProvider));
		this.moduleManager.registerModule(new ModuleCoreCallEntityJoinWorldEvent(this));
		this.moduleManager.registerModule(new ModuleCoreCDPacketEventCaller(this));
		this.moduleManager.registerModule(new ModuleCoreNetworkHandler(this));
		
		// Fixes
		this.moduleManager.registerModule(new ModuleFixArmorBreaking(this));
		this.moduleManager.registerModule(new ModuleFixCorpseDuplication(this));
		this.moduleManager.registerModule(new ModuleFixDamageSourceDetection(this));
		this.moduleManager.registerModule(new ModuleFixDeathDropsCompatibility(this));
		this.moduleManager.registerModule(new ModuleFixGoldenAppleDuplication(this));
		this.moduleManager.registerModule(new ModuleFixGrenadeThrowPosition(this));
		this.moduleManager.registerModule(new ModuleFixGunDamageOnServerFreeze(this));
		this.moduleManager.registerModule(new ModuleFixInvisibleEntities(this));
		this.moduleManager.registerModule(new ModuleFixItemUsageOnRegions(this));
		this.moduleManager.registerModule(new ModuleFixSniperFastShoot(this));
		this.moduleManager.registerModule(new ModuleFixSwapGunFastShoot(this));
		this.moduleManager.registerModule(new ModuleFixZombieHeight(this));
		
		// Toggle them all!
		this.moduleManager.toggleAll(ToggleAction.TOGGLE_FROM_CONFIG);
		
		this.getServer().getPluginManager().registerEvents(new MiscListener(), this);
		
		this.getCommand("arziolib").setExecutor(new ArzioLibCommand(this));
		this.getCommand("particle").setExecutor(new ParticlesCommand());
		this.getCommand("clothes").setExecutor(new ClothesCommand());
		
		this.getLogger().info("Loading done! :3");
		this.getLogger().info("This plugin was made by Arzio <3");
		this.getLogger().info("Please, use '/arziolib' to check all available commands");
	}
	
	@Override
	public void reloadConfig() {
		super.reloadConfig();
		this.moduleManager.toggleAll(ToggleAction.TOGGLE_FROM_CONFIG);
	}

	@Override
	public void onDisable() {
		this.moduleManager.toggleAll(ToggleAction.TOGGLE_OFF);
		this.forgeBukkitEventManager.unregisterAll();
		instance = null;
		this.getLogger().info("Plugin unloaded: " + this.getName() + " by Arzio");
	}
	
	public WGCustomFlagsPlugin getWGCustomFlags() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WGCustomFlags");

		if (!(plugin instanceof WGCustomFlagsPlugin)) {
			return null;
		}

		return (WGCustomFlagsPlugin) plugin;
	}

	public WorldGuardPlugin getWorldGuard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

		if (!(plugin instanceof WorldGuardPlugin)) {
			return null;
		}

		return (WorldGuardPlugin) plugin;
	}
	
	public PlayerDataHandler getPlayerDataHandler() {
		return this.playerDataHandler;
	}

	public BaseProvider getBaseProvider() {
		return this.baseProvider;
	}
	
	public LootProvider getLootProvider() {
		return this.lootProvider;
	}

	public ItemProvider getItemProvider() {
		return this.itemProvider;
	}

	public CraftingDead getCraftingDeadMain() {
		return this.craftingDead;
	}

	public ItemStackHelper getItemStackHelper() {
		return itemStackHelper;
	}

	public UserDataProvider getUserDataProvider() {
		return userDataProvider;
	}

	public ForgeBukkitEventManager getForgeBukkitEventManager() {
		return forgeBukkitEventManager;
	}

	public ModuleManager getModuleManager() {
		return this.moduleManager;
	}

	public static ArzioLib getInstance() {
		if (instance == null) throw new IllegalStateException("The plugin is not enabled yet! Maybe you need to make your plugin wait until the Lib loads. Add this plugin to your plugin's dependencies in your plugin.yml");
		return instance;
	}
}
