package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.Material;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDMaterial;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.Gun;

public class GunImpl extends CDItemImpl implements Gun{

	public GunImpl(Material material) {
		super(material);
	}

	@Override
	public int getBodyDamage() {
		return CDClasses.itemGunBodyDamageField.getValue(this.getItemInstance());
	}

	@Override
	public void setBodyDamage(int value) {
		CDClasses.itemGunBodyDamageField.setValue(this.getItemInstance(), value);
	}

	@Override
	public int getHeadshotDamage() {
		return CDClasses.itemGunHeadshotDamageField.getValue(this.getItemInstance());
	}

	@Override
	public void setHeadshotDamage(int value) {
		CDClasses.itemGunHeadshotDamageField.setValue(this.getItemInstance(), value);
	}

	@Override
	public Material[] getCompatibleAmmos() {
		return CauldronUtils.intArrayToMaterialArray(CDClasses.itemGunCompatibleMagazinesField.getValue(this.getItemInstance()));
	}

	@Override
	public void setCompatibleMagazines(Material... materials) {
		CDClasses.itemGunCompatibleMagazinesField.setValue(this.getItemInstance(), CauldronUtils.materialArrayToIntArray(materials));
	}

	@Override
	public float getRPM() {
		return CDClasses.itemGunRoundsPerMinute.getValue(this.getItemInstance());
	}

	@Override
	public long getMillisPerRound() {
		double maxPerSecond = this.getRPM() / 60.0D;
	    double delayPerSecond = 1.0D / maxPerSecond;
	    return (long) (delayPerSecond * 1000.0D);
	}

	@Override
	public int getBulletsPerRound() {
		return CDClasses.itemGunBulletsPerRound.getValue(this.getItemInstance());
	}

	@Override
	public float getSoundLevel() {
		return CDClasses.itemGunSoundLevel.getValue(this.getItemInstance());
	}

	@Override
	public boolean isFireBased() {
		return (this.getItem() != CDMaterial.CROSSBOW.asMaterial()) && (this.getItem() != CDMaterial.TASER.asMaterial());
	}

	@Override
	public String getShootSound() {
		return ArzioLib.MOD_RESOURCE_NAME+CDClasses.itemGunFireSoundName.getValue(this.getItemInstance());
	}

	@Override
	public String getSilencedSound() {
		return ArzioLib.MOD_RESOURCE_NAME+CDClasses.itemGunSuppresedSoundName.getValue(this.getItemInstance());
	}

	@Override
	public void setShootSound(String soundName) {
		CDClasses.itemGunFireSoundName.setValue(this.getItemInstance(), soundName);
	}

	@Override
	public void setSilencedSound(String soundName) {
		CDClasses.itemGunSuppresedSoundName.setValue(this.getItemInstance(), soundName);
	}

}
