package com.arzio.arziolib.api.util.reflection;

import static com.arzio.arziolib.api.util.reflection.ReflectionHelper.getSimpleSourceFileNamesToOriginalClassNames;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;

import com.arzio.arziolib.api.util.CDLootType;
import com.arzio.arziolib.api.util.CDMaterial;
import com.arzio.arziolib.api.util.reflection.ReflectionHelper.FieldChecker;
import com.arzio.arziolib.api.util.reflection.finder.ContentFinder;
import com.arzio.arziolib.api.util.reflection.finder.NameClassFinder;
import com.craftingdead.server.API;

import net.minecraft.server.v1_6_R3.Item;
import net.minecraft.server.v1_6_R3.ItemStack;

public class CDClasses {

	public static final int BASE_X_VALUE_FOR_LOOKUP = 11111111;
	public static final int BASE_Y_VALUE_FOR_LOOKUP = 22222222;
	public static final int BASE_Z_VALUE_FOR_LOOKUP = 33333333;
	
	public static final String LOOT_TYPE_ID_FOR_LOOKUP = "FakeIDForLootType";
	public static final String LOOT_TYPE_NAME_FOR_LOOKUP = "FakeNameForLootType";
	public static final String PLAYER_DATA_DUMMY_NAME = "ThisIsADummyPlayerDataObject!";
	
	public static ReflectedClass entityPlayerClass;
	static {
		try {
			entityPlayerClass = new ReflectedClass(Class.forName("net.minecraft.entity.player.EntityPlayer"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final ReflectedClass blockManagerClass = new ReflectedClass(NameClassFinder.find("BlockManager"));
		public static final ReflectedField<int[]> blockManagerBaseMaterialIds = new ReflectedField<>(blockManagerClass, new ContentFinder.FieldBuilder<int[]>().withValue(null, int[].class, new FieldChecker<int[]>() {

			@Override
			public boolean isCorrect(int[] found, Field field) {
				return ArrayUtils.contains(found, 98) && ArrayUtils.contains(found, 5);
			}
			
		}).build());
	
	public static final ReflectedClass tileEntityBaseCenterClass = new ReflectedClass(NameClassFinder.find("TileEntityBaseCenter"));
	
	public static final ReflectedClass commonPlayerTrackerClass = new ReflectedClass(NameClassFinder.find("CommonPlayerTracker"));
	
	public static final ReflectedClass craftingDeadMainClass = new ReflectedClass(NameClassFinder.find("CraftingDead", "CDOrigins"));
		public static final ReflectedField<Object> craftingDeadMainInstanceField = new ReflectedField<>(craftingDeadMainClass, new ContentFinder.FieldBuilder<>().withType(craftingDeadMainClass.getReferencedClass()).build());
		public static final ReflectedField<Object> craftingDeadMainCommonPlayerTrackerField = new ReflectedField<>(craftingDeadMainClass, new ContentFinder.FieldBuilder<>().withType(commonPlayerTrackerClass.getReferencedClass()).build());
		
	public static final ReflectedClass blockBaseCenterClass = new ReflectedClass(NameClassFinder.find("BlockBaseCenter"));
		public static final ReflectedMethod blockBaseCenterDestroy = new ReflectedMethod(blockBaseCenterClass, new ContentFinder.MethodBuilder().withParameterTypes(new Class<?>[] { net.minecraft.server.v1_6_R3.World.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, CDClasses.entityPlayerClass.getReferencedClass() }).build());
	
	public static final ReflectedClass damageSourceCDClass = new ReflectedClass(NameClassFinder.find("DamageSourceCD"));
	
	public static final ReflectedClass itemCDClass = new ReflectedClass(NameClassFinder.find("ItemCD"));
	
	// ItemGun class and fields
	public static final ReflectedClass itemGunClass = new ReflectedClass(NameClassFinder.find("ItemGun"));
		public static final ReflectedField<Integer> itemGunBodyDamageField = new ReflectedField<>(itemGunClass, new ContentFinder.FieldBuilder<Integer>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], int.class, 6).build());
		public static final ReflectedField<Integer> itemGunHeadshotDamageField = new ReflectedField<>(itemGunClass, new ContentFinder.FieldBuilder<Integer>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], int.class, 13).build());
		public static final ReflectedField<int[]>   itemGunCompatibleMagazinesField = new ReflectedField<>(itemGunClass, new ContentFinder.FieldBuilder<int[]>().withType(int[].class).build());
		public static final ReflectedField<Float>   itemGunRoundsPerMinute = new ReflectedField<>(itemGunClass, new ContentFinder.FieldBuilder<Float>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], float.class, 700F).build());
		public static final ReflectedField<Integer> itemGunBulletsPerRound = new ReflectedField<>(itemGunClass, new ContentFinder.FieldBuilder<Integer>().withExactValue(Item.byId[CDMaterial.MOSSBERG.getId()], int.class, 6).build());
		public static final ReflectedField<Float>   itemGunSoundLevel = new ReflectedField<>(itemGunClass, new ContentFinder.FieldBuilder<Float>().withExactValue(Item.byId[CDMaterial.M1_GARAND.getId()], float.class, 1.5F).build());
		public static final ReflectedField<String>  itemGunFireSoundName = new ReflectedField<>(itemGunClass, new ContentFinder.FieldBuilder<String>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], String.class, "m4a1shoot").build());
		public static final ReflectedField<String>  itemGunSuppresedSoundName = new ReflectedField<>(itemGunClass, new ContentFinder.FieldBuilder<String>().withExactValue(Item.byId[CDMaterial.M4A1.getId()], String.class, "m4a1shootsd").build());
		
	// ItemMagazine class and fields
	public static final ReflectedClass itemMagazineClass = new ReflectedClass(NameClassFinder.find("ItemAmmo"));
		public static final ReflectedField<Double> itemMagazinePenetrationField = new ReflectedField<>(itemMagazineClass, new ContentFinder.FieldBuilder<Double>().withExactValue(Item.byId[CDMaterial.M1_GARAND_MAGAZINE.getId()], double.class, 95D).build());
		public static final ReflectedField<Integer> itemMagazineBulletAmountField = new ReflectedField<>(itemMagazineClass, new ContentFinder.FieldBuilder<Integer>().withExactValue(Item.byId[CDMaterial.M1_GARAND_MAGAZINE.getId()], int.class, 8).build());
	
	public static final ReflectedClass lootTypeClass = new ReflectedClass(NameClassFinder.find("LootType"));
		public static final ReflectedField<ArrayList<Object>> lootTypeContent = new ReflectedField<>(lootTypeClass, new ContentFinder.FieldBuilder<>().withType(ArrayList.class).build());
		public static final ReflectedField<Double> lootTypeSpawnChance = new ReflectedField<>(lootTypeClass, new ContentFinder.FieldBuilder<>().withType(double.class).build());
		
		public static Object LOOT_TYPE_DUMMY;
		static {
			try {
				LOOT_TYPE_DUMMY = lootTypeClass.getReferencedClass().getConstructor(String.class, String.class, double.class).newInstance(LOOT_TYPE_ID_FOR_LOOKUP, LOOT_TYPE_NAME_FOR_LOOKUP, -1);
			} catch (Exception e) {
				Bukkit.getLogger().log(Level.SEVERE, "Bad news: the base system will not be working properly. Some mod update have broken it.", e);
			}

		}
		public static final ReflectedField<String> lootTypeId = new ReflectedField<>(lootTypeClass, new ContentFinder.FieldBuilder<>().withExactValue(LOOT_TYPE_DUMMY, String.class, LOOT_TYPE_ID_FOR_LOOKUP).build());
		public static final ReflectedField<String> lootTypeName = new ReflectedField<>(lootTypeClass, new ContentFinder.FieldBuilder<>().withExactValue(LOOT_TYPE_DUMMY, String.class, LOOT_TYPE_NAME_FOR_LOOKUP).build());
		
	public static final ReflectedClass lootManagerClass = new ReflectedClass(NameClassFinder.find("LootManager"));
		public static final ReflectedField<Object> lootManagerLootResidentialInstance = new ReflectedField<Object>(lootManagerClass, new ContentFinder.FieldBuilder<>().withValue(null, lootTypeClass.getReferencedClass(), new FieldChecker<Object>() {

			@Override
			public boolean isCorrect(Object found, Field field) throws Exception {
				return lootTypeId.getValue(found).equalsIgnoreCase(CDLootType.RESIDENTIAL.getId());
			}
			
		}).build());
		public static final ReflectedField<Object> lootManagerLootResidentialRareInstance = new ReflectedField<Object>(lootManagerClass, new ContentFinder.FieldBuilder<>().withValue(null, lootTypeClass.getReferencedClass(), new FieldChecker<Object>() {

			@Override
			public boolean isCorrect(Object found, Field field) throws Exception {
				return lootTypeId.getValue(found).equalsIgnoreCase(CDLootType.RESIDENTIAL_RARE.getId());
			}
			
		}).build());
		public static final ReflectedField<Object> lootManagerLootMedicalInstance = new ReflectedField<Object>(lootManagerClass, new ContentFinder.FieldBuilder<>().withValue(null, lootTypeClass.getReferencedClass(), new FieldChecker<Object>() {

			@Override
			public boolean isCorrect(Object found, Field field) throws Exception {
				return lootTypeId.getValue(found).equalsIgnoreCase(CDLootType.MEDICAL.getId());
			}
			
		}).build());
		public static final ReflectedField<Object> lootManagerLootMilitaryInstance = new ReflectedField<Object>(lootManagerClass, new ContentFinder.FieldBuilder<>().withValue(null, lootTypeClass.getReferencedClass(), new FieldChecker<Object>() {

			@Override
			public boolean isCorrect(Object found, Field field) throws Exception {
				return lootTypeId.getValue(found).equalsIgnoreCase(CDLootType.MILITARY.getId());
			}
			
		}).build());
		public static final ReflectedField<Object> lootManagerLootPoliceInstance = new ReflectedField<Object>(lootManagerClass, new ContentFinder.FieldBuilder<>().withValue(null, lootTypeClass.getReferencedClass(), new FieldChecker<Object>() {

			@Override
			public boolean isCorrect(Object found, Field field) throws Exception {
				return lootTypeId.getValue(found).equalsIgnoreCase(CDLootType.POLICE.getId());
			}
			
		}).build());
		
	
	public static final ReflectedClass lootContentClass = new ReflectedClass(NameClassFinder.find("LootContent"));
		public static final ReflectedField<Integer> lootContentItemId = new ReflectedField<>(lootContentClass, new ContentFinder.FieldBuilder<>().withType(int.class).build());
		public static final ReflectedField<Double> lootContentChance = new ReflectedField<>(lootContentClass, new ContentFinder.FieldBuilder<>().withType(double.class).build());
		
	public static final ReflectedClass tileEntityLootClass = new ReflectedClass(NameClassFinder.find("TileEntityLoot"));
		public static final ReflectedField<Object> tileEntityLootType = new ReflectedField<>(tileEntityLootClass, new ContentFinder.FieldBuilder<>().withType(lootTypeClass.getReferencedClass()).build());
	
	public static final ReflectedClass inventoryCDAClass = new ReflectedClass(NameClassFinder.find("InventoryCDA", "InventoryCD", "InventoryCDO"));
		public static final ReflectedField<ItemStack[]> inventoryCDAInventory = new ReflectedField<>(inventoryCDAClass, new ContentFinder.FieldBuilder<ItemStack[]>().withType(ItemStack[].class).build());
	
	public static final ReflectedClass waterLevelsClass = new ReflectedClass(NameClassFinder.find("WaterLevels", "WaterLevel"));
	
	// Player Data class and fields
	public static final ReflectedClass playerDataClass = new ReflectedClass(NameClassFinder.find("PlayerData"));
		
	public static final ReflectedClass playerDataHandlerClass = new ReflectedClass(NameClassFinder.find("PlayerDataHandler"));
		public static final ReflectedMethod playerDataHandlerGetPlayerData = new ReflectedMethod(playerDataHandlerClass, new ContentFinder.MethodBuilder().withReturnType(playerDataClass.getReferencedClass()).withParameterTypes(String.class).build());

	// ... Continuation of PlayerDataClass
		public static final ReflectedField<Object> playerDataWaterLevels = new ReflectedField<>(playerDataClass, new ContentFinder.FieldBuilder<>().withType(waterLevelsClass.getReferencedClass()).build());
		public static final ReflectedMethod playerDataBaseSetLocation = new ReflectedMethod(playerDataClass, new ContentFinder.MethodBuilder().withParameterTypes(int.class, int.class, int.class).build());
		public static Object PLAYER_DATA_DUMMY;
		static {
			try {
				PLAYER_DATA_DUMMY = playerDataHandlerGetPlayerData.invoke(null, PLAYER_DATA_DUMMY_NAME);
				playerDataBaseSetLocation.getReferencedMethod().invoke(PLAYER_DATA_DUMMY, BASE_X_VALUE_FOR_LOOKUP, BASE_Y_VALUE_FOR_LOOKUP, BASE_Z_VALUE_FOR_LOOKUP);
			} catch (Exception e) {
				Bukkit.getLogger().log(Level.SEVERE, "Bad news: the base system will not be working properly. Some mod update have broken it.", e);
			}

		}
		public static final ReflectedField<Integer> playerDataBaseXField = new ReflectedField<>(playerDataClass, new ContentFinder.FieldBuilder<Integer>().withExactValue(PLAYER_DATA_DUMMY, int.class, BASE_X_VALUE_FOR_LOOKUP).build());
		public static final ReflectedField<Integer> playerDataBaseYField = new ReflectedField<>(playerDataClass, new ContentFinder.FieldBuilder<Integer>().withExactValue(PLAYER_DATA_DUMMY, int.class, BASE_Y_VALUE_FOR_LOOKUP).build());
		public static final ReflectedField<Integer> playerDataBaseZField = new ReflectedField<>(playerDataClass, new ContentFinder.FieldBuilder<Integer>().withExactValue(PLAYER_DATA_DUMMY, int.class, BASE_Z_VALUE_FOR_LOOKUP).build());
		public static final ReflectedField<Object> playerDataInventoryCDA = new ReflectedField<>(playerDataClass, new ContentFinder.FieldBuilder<>().withType(inventoryCDAClass.getReferencedClass()).build());
	
		static {
			// Forces the fake-player-data to have a negative value for the water level stat
			API.increaseWaterLevelsToPlayer(PLAYER_DATA_DUMMY_NAME, -500);
		}
		
		public static final ReflectedField<Integer> waterLevelsValue = new ReflectedField<>(waterLevelsClass, new ContentFinder.FieldBuilder<Integer>().withValue(playerDataWaterLevels.getValue(PLAYER_DATA_DUMMY), int.class, new FieldChecker<Integer>() {

			@Override
			public boolean isCorrect(Integer found, Field field) throws Exception {
				// Get the field with negative value.
				// Theoretically, the only negative value for this fake-player-data
				// will be the current water level value, as we changed it to negative before.
				return found < 0;
			}
		
		}).build());
	
		

	public static final ReflectedClass packetBaseBaseRemovalClass = new ReflectedClass(NameClassFinder.find("CDAPacketBaseRemoval"));
	public static final ReflectedClass packetBulletCollisionClientClass = new ReflectedClass(NameClassFinder.find("CDAPacketBulletCollisionClient"));
	public static final ReflectedClass packetGrenadeThrowingClass = new ReflectedClass(NameClassFinder.find("CDAPacketGrenadeThrowing"));
	public static final ReflectedClass packetSwitchItemClass = new ReflectedClass(NameClassFinder.find("CDAPacketSwitchItem"));
	public static final ReflectedClass packetGunTriggerClass = new ReflectedClass(NameClassFinder.find("CDAPacketGunTrigger"));
	public static final ReflectedClass packetGunReloadClass = new ReflectedClass(NameClassFinder.find("CDAPacketGunReload"));
	public static final ReflectedClass packetBulletCollisionClass = new ReflectedClass(NameClassFinder.find("CDAPacketBulletCollision"));
	public static final ReflectedClass packetPlayerDataToPlayerClass = new ReflectedClass(NameClassFinder.find("CDAPacketPlayerDataToPlayer"));
	public static final ReflectedClass packetFTTriggerClass = new ReflectedClass(NameClassFinder.find("CDAPacketFTTrigger"));
	public static final ReflectedClass packetACLCombatClass = new ReflectedClass(NameClassFinder.find("CDAPacketACLInCombat"));

	public static final ReflectedClass inventoryBackpackClass = new ReflectedClass(NameClassFinder.find("InventoryBackpack"));
	public static final ReflectedClass inventoryFuelTanksClass = new ReflectedClass(NameClassFinder.find("InventoryFuelTanks"));
	public static final ReflectedClass inventoryTacticalVestClass = new ReflectedClass(NameClassFinder.find("InventoryTacticalVest"));
	public static final ReflectedClass inventoryShelfLootClass = new ReflectedClass(NameClassFinder.find("InventoryShelfLoot"));
	public static final ReflectedClass inventoryVendingMachineClass = new ReflectedClass(NameClassFinder.find("InventoryVendingMachine"));
	
	public static final ReflectedClass containerBackpackClass = new ReflectedClass(NameClassFinder.find("ContainerBackpack"));
	public static final ReflectedClass containerFuelTanksClass = new ReflectedClass(NameClassFinder.find("ContainerFuelTanks"));
	public static final ReflectedClass containerTacticalVestClass = new ReflectedClass(NameClassFinder.find("ContainerTacticalVest"));
	public static final ReflectedClass containerShelfLootClass = new ReflectedClass(NameClassFinder.find("ContainerShelf", "ContainerShelfLoot"));
	public static final ReflectedClass containerVendingMachineClass = new ReflectedClass(NameClassFinder.find("ContainerVendingMachine"));
	public static final ReflectedClass containerInventoryCDAClass = new ReflectedClass(NameClassFinder.find("ContainerInventoryCDA"));
	
	public static final ReflectedClass itemClothingClass = new ReflectedClass(NameClassFinder.find("ItemClothing"));
		public static final ReflectedField<Integer> itemClothingProtectionLevel = new ReflectedField<>(itemClothingClass, new ContentFinder.FieldBuilder<Integer>().withExactValue(Item.byId[CDMaterial.SPETSNAZ_CLOTHING.getId()], int.class, 3).build());
	
	public static final ReflectedClass networkManagerClass = new ReflectedClass(NameClassFinder.find("NetworkManager"));
		public static final ReflectedMethod networkManagerGetPacketIdFromClass = new ReflectedMethod(networkManagerClass, new ContentFinder.MethodBuilder().withReturnType(int.class).withParameterTypes(Class.class).build());
	
	
	public static Class<?> getClassBySourceFileName(String name) throws ClassNotFoundException{
		String found = getSimpleSourceFileNamesToOriginalClassNames().get(name);
		
		if (found == null) {
			throw new ClassNotFoundException("Tried to find class "+name+", but I don't have found it on the CD mappings.");
		}
		
		return Class.forName(found);
	}
	
	public static String getSourceFileNameFromClass(Class<?> clazz) throws ClassNotFoundException{
		String result = getSimpleSourceFileNamesToOriginalClassNames().inverse().get(clazz.getName());
		if (result == null) {
			throw new ClassNotFoundException("Class not found in CD class mapping: "+clazz);
		}
		return result;
	}

}
