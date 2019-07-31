package com.arzio.arziolib;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.arzio.arziolib.api.BaseProvider;
import com.arzio.arziolib.api.ForgeBukkitEventManager;
import com.arzio.arziolib.api.ItemStackHelper;
import com.arzio.arziolib.api.TestHelper;
import com.arzio.arziolib.api.UpdateChecker;
import com.arzio.arziolib.api.UpdateChecker.CheckMethod;
import com.arzio.arziolib.api.impl.BaseProviderImpl;
import com.arzio.arziolib.api.impl.ForgeBukkitEventManagerImpl;
import com.arzio.arziolib.api.impl.GitHubUpdateChecker;
import com.arzio.arziolib.api.impl.ItemStackHelperImpl;
import com.arzio.arziolib.api.impl.TestHelperImpl;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.util.TestException;
import com.arzio.arziolib.api.util.reflection.ReflectionHelper;
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
import com.arzio.arziolib.command.NoDelayCommand;
import com.arzio.arziolib.command.ParticlesCommand;
import com.arzio.arziolib.command.PingCommand;
import com.arzio.arziolib.command.TestCommand;
import com.arzio.arziolib.command.ThirstAllCommand;
import com.arzio.arziolib.command.ThirstCommand;
import com.arzio.arziolib.config.UserDataProvider;
import com.arzio.arziolib.listener.MiscListener;
import com.arzio.arziolib.module.ArzioModuleManager;
import com.arzio.arziolib.module.ModuleContainer;
import com.arzio.arziolib.module.ModuleManager;
import com.arzio.arziolib.module.ModuleManager.ToggleAction;
import com.arzio.arziolib.module.addon.ModuleAddonBiomeChanger;
import com.arzio.arziolib.module.addon.ModuleAddonCorpseCustomLifespan;
import com.arzio.arziolib.module.addon.ModuleAddonCustomBaseMaterials;
import com.arzio.arziolib.module.addon.ModuleAddonCustomFlags;
import com.arzio.arziolib.module.addon.ModuleAddonCustomGunsAndAmmos;
import com.arzio.arziolib.module.addon.ModuleAddonCustomLoots;
import com.arzio.arziolib.module.addon.ModuleAddonDisableLegBreak;
import com.arzio.arziolib.module.addon.ModuleAddonEquipGunsFromInventory;
import com.arzio.arziolib.module.addon.ModuleAddonInfinityEnchantCompatiblity;
import com.arzio.arziolib.module.addon.ModuleAddonKeepEXPAfterDeath;
import com.arzio.arziolib.module.addon.ModuleAddonKickPlayersBeforeServerStop;
import com.arzio.arziolib.module.addon.ModuleAddonProjectileProtectionCompatibility;
import com.arzio.arziolib.module.addon.ModuleAddonRealSound;
import com.arzio.arziolib.module.addon.ModuleAddonRespawnAtBase;
import com.arzio.arziolib.module.addon.ModuleAddonRespawnLoots;
import com.arzio.arziolib.module.addon.ModuleAddonRestrictGMInventoryWithPermission;
import com.arzio.arziolib.module.addon.ModuleAddonSimpleClansFlags;
import com.arzio.arziolib.module.addon.ModuleAddonSimpleClansNametags;
import com.arzio.arziolib.module.addon.ModuleAddonStackableGrenades;
import com.arzio.arziolib.module.addon.ModuleAddonStepEmeraldHeal;
import com.arzio.arziolib.module.addon.ModuleAddonTCPNoDelay;
import com.arzio.arziolib.module.addon.ModuleAddonZombieFollowGrenades;
import com.arzio.arziolib.module.addon.ModuleAddonZombieHearGuns;
import com.arzio.arziolib.module.addon.ModuleAddonZombieSpawnBlockBlacklist;
import com.arzio.arziolib.module.core.ModuleCoreBukkitEventsForBases;
import com.arzio.arziolib.module.core.ModuleCoreCDPacketEventCaller;
import com.arzio.arziolib.module.core.ModuleCoreCallCDLootDropEvent;
import com.arzio.arziolib.module.core.ModuleCoreCallEntityJoinWorldEvent;
import com.arzio.arziolib.module.core.ModuleCoreNetworkHandler;
import com.arzio.arziolib.module.core.ModuleCoreWorldGuardRegionEvents;
import com.arzio.arziolib.module.fix.ModuleFixArmorBreaking;
import com.arzio.arziolib.module.fix.ModuleFixCorpseDuplication;
import com.arzio.arziolib.module.fix.ModuleFixDamageSourceDetection;
import com.arzio.arziolib.module.fix.ModuleFixDeathDropsCompatibility;
import com.arzio.arziolib.module.fix.ModuleFixGunDamageOnServerFreeze;
import com.arzio.arziolib.module.fix.ModuleFixInvisibleEntities;
import com.arzio.arziolib.module.fix.ModuleFixItemUsageOnRegions;
import com.arzio.arziolib.module.fix.ModuleFixPlotMeEntityInteraction;
import com.arzio.arziolib.module.fix.ModuleFixPvPOnWorldsWithoutPvP;
import com.arzio.arziolib.module.fix.ModuleFixShutdownKickMessageColor;
import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import cpw.mods.fml.common.ModContainer;

public class ArzioLib extends JavaPlugin {

    public static final ModContainer MOD_CONTAINER = ReflectionHelper.getCraftingDeadModContainer();
    public static final String MOD_ID = "craftingdead";
    public static final String MOD_RESOURCE_NAME = MOD_ID+":";
    public static final String MOD_NETWORK_ID = "cdaNetworking";

    private UpdateChecker updateChecker;

    private static ArzioLib instance;
    private TestHelper testHelper;
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
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        instance = this;

        this.updateChecker = new GitHubUpdateChecker(this, "https://api.github.com/repos/Arzio/ArzioLib/releases",
                "https://github.com/Arzio/ArzioLib/releases");

        this.testHelper = new TestHelperImpl();
        this.userDataProvider = new UserDataProvider();
        this.playerDataHandler = new PlayerDataHandlerImpl(this);
        this.forgeBukkitEventManager = new ForgeBukkitEventManagerImpl(this);
        this.itemProvider = new ItemProviderImpl();
        this.craftingDead = new CraftingDeadMainImpl();
        this.itemStackHelper = new ItemStackHelperImpl();
        this.baseProvider = new BaseProviderImpl();
        this.lootProvider = new LootProviderImpl();

        // Initiate the module manager
        this.moduleManager = new ArzioModuleManager(this);

        // Addons
        this.moduleManager.registerModule(ModuleAddonBiomeChanger.class);
        this.moduleManager.registerModule(ModuleAddonCorpseCustomLifespan.class);
        this.moduleManager.registerModule(ModuleAddonCustomBaseMaterials.class);
        this.moduleManager.registerModule(ModuleAddonCustomFlags.class);
        this.moduleManager.registerModule(ModuleAddonCustomGunsAndAmmos.class);
        this.moduleManager.registerModule(ModuleAddonCustomLoots.class);
        this.moduleManager.registerModule(ModuleAddonDisableLegBreak.class);
        this.moduleManager.registerModule(ModuleAddonEquipGunsFromInventory.class);
        this.moduleManager.registerModule(ModuleAddonInfinityEnchantCompatiblity.class);
        this.moduleManager.registerModule(ModuleAddonKeepEXPAfterDeath.class);
        this.moduleManager.registerModule(ModuleAddonKickPlayersBeforeServerStop.class);
        this.moduleManager.registerModule(ModuleAddonProjectileProtectionCompatibility.class);
        this.moduleManager.registerModule(ModuleAddonRealSound.class);
        this.moduleManager.registerModule(ModuleAddonRespawnAtBase.class);
        this.moduleManager.registerModule(ModuleAddonRespawnLoots.class);
        this.moduleManager.registerModule(ModuleAddonRestrictGMInventoryWithPermission.class);
        this.moduleManager.registerModule(ModuleAddonStackableGrenades.class);
        this.moduleManager.registerModule(ModuleAddonStepEmeraldHeal.class);
        this.moduleManager.registerModule(ModuleAddonTCPNoDelay.class);
        this.moduleManager.registerModule(ModuleAddonZombieFollowGrenades.class);
        this.moduleManager.registerModule(ModuleAddonZombieHearGuns.class);
        this.moduleManager.registerModule(ModuleAddonZombieSpawnBlockBlacklist.class);
        if (CauldronUtils.isPluginLoaded("SimpleClans")) {
            this.moduleManager.registerModule(ModuleAddonSimpleClansNametags.class);
            this.moduleManager.registerModule(ModuleAddonSimpleClansFlags.class);
        }

        // Core - DO NOT DISABLE THEM UNLESS YOU KNOW YOU ARE DOING
        this.moduleManager.registerModule(ModuleCoreBukkitEventsForBases.class);
        this.moduleManager.registerModule(ModuleCoreCallCDLootDropEvent.class);
        this.moduleManager.registerModule(ModuleCoreCallEntityJoinWorldEvent.class);
        this.moduleManager.registerModule(ModuleCoreCDPacketEventCaller.class);
        this.moduleManager.registerModule(ModuleCoreNetworkHandler.class);
        this.moduleManager.registerModule(ModuleCoreWorldGuardRegionEvents.class);

        // Fixes
        this.moduleManager.registerModule(ModuleFixArmorBreaking.class);
        this.moduleManager.registerModule(ModuleFixCorpseDuplication.class);
        this.moduleManager.registerModule(ModuleFixDamageSourceDetection.class);
        this.moduleManager.registerModule(ModuleFixDeathDropsCompatibility.class);
        this.moduleManager.registerModule(ModuleFixGunDamageOnServerFreeze.class);
        this.moduleManager.registerModule(ModuleFixInvisibleEntities.class);
        this.moduleManager.registerModule(ModuleFixItemUsageOnRegions.class);
        if (CauldronUtils.isPluginLoaded("PlotMe") && CauldronUtils.isPluginLoaded("AuthMe")) {
            this.moduleManager.registerModule(ModuleFixPlotMeEntityInteraction.class);
        }
        this.moduleManager.registerModule(ModuleFixPvPOnWorldsWithoutPvP.class);
        this.moduleManager.registerModule(ModuleFixShutdownKickMessageColor.class);

        // Toggle them all!
        this.moduleManager.toggleAll(ToggleAction.TOGGLE_FROM_CONFIG);

        this.getServer().getPluginManager().registerEvents(new MiscListener(), this);

        this.getCommand("arziolib").setExecutor(new ArzioLibCommand(this));
        this.getCommand("particle").setExecutor(new ParticlesCommand());
        this.getCommand("clothes").setExecutor(new ClothesCommand());
        this.getCommand("ping").setExecutor(new PingCommand());
        this.getCommand("thirst").setExecutor(new ThirstCommand());
        this.getCommand("thirstall").setExecutor(new ThirstAllCommand());
        this.getCommand("test").setExecutor(new TestCommand());
        this.getCommand("nodelay").setExecutor(new NoDelayCommand());

        this.getLogger().info("Loading done! :3");
        this.getLogger().info("This plugin was made by Arzio <3");
        this.getLogger().info("Please, use '/arziolib' to check all available commands");

        // Checks for new updates using another thread
        this.getUpdateChecker().checkUpdates(CheckMethod.ASYNC);

        // Automatically checks for updates in a async way every 10 minutes
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {

            @Override
            public void run() {
                getUpdateChecker().checkUpdates(CheckMethod.ASYNC);
            }

        }, 600L * 20L, 600L * 20L); // Checks for update every 10 minutes
        
        // Warn about errored modules to some admins
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {

            @Override
            public void run() {
                List<ModuleContainer> erroredModules = getModuleManager().getErroredModules();
                
                if (!erroredModules.isEmpty()) {
                    if (getModuleManager().shouldWarnForErroredModules()) {
                        for (Player player : CauldronUtils.getPlayersWithPermission("arziolib.warnings")) {
                            player.sendMessage(" ");
                            player.sendMessage("§e["+getName()+"] §cThe following modules could not get loaded or unloaded:");
                            for (ModuleContainer module : erroredModules) {
                                player.sendMessage("§7 - "+module.getName());
                            }
                            player.sendMessage("§cCheck console for possible errors during server load phase.");
                            player.sendMessage(" ");
                        }
                    }
                }
            }

        }, 20L * 20L, 20L * 20L); // Warns admins every 20 seconds in case of load errors in modules
        
        ArzioLibTests.registerTests();
        
        // Runs the tests after 20 ticks.
        // The tests should run right after the plugin load phase.
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            
            @Override
            public void run() {
                try {
                    ArzioLibTests.runTests(Bukkit.getConsoleSender());
                } catch (TestException e) {
                    e.printStackTrace(Bukkit.getConsoleSender());
                }
            }
        }, 20L);
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
    
    public TestHelper getTestHelper() {
        return this.testHelper;
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

    public UpdateChecker getUpdateChecker() {
        return this.updateChecker;
    }

    public static ArzioLib getInstance() {
        if (instance == null)
            throw new IllegalStateException(
                    "The plugin is not enabled yet! Maybe you need to make your plugin wait until the Lib loads. Add this plugin to your plugin's dependencies in your plugin.yml");
        return instance;
    }
}
