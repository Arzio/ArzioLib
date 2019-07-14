package com.arzio.arziolib.api.wrapper.impl;

import org.bukkit.Material;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.util.CDAttachment;
import com.arzio.arziolib.api.util.CDMaterial;
import com.arzio.arziolib.api.util.CauldronUtils;
import com.arzio.arziolib.api.util.reflection.CDClasses;
import com.arzio.arziolib.api.wrapper.Gun;

public class GunImpl extends CDSharedItemImpl implements Gun{

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
	public double getRPM() {
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
	public double getSoundLevel() {
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

	@Override
	public String getReloadSound() {
		return ArzioLib.MOD_RESOURCE_NAME+CDClasses.itemGunReloadSoundName.getValue(this.getItemInstance());
	}

	@Override
	public void setReloadSound(String soundName) {
		CDClasses.itemGunReloadSoundName.setValue(this.getItemInstance(), soundName);
	}

	@Override
	public CDAttachment[] getCompatibleAttachments() {
		Object[] compatibleArray = CDClasses.itemGunCompatibleAttachmentsField.getValue(this.getItemInstance());
		Object[] existingAttachments = CDClasses.gunAttachmentArrayField.getValue(null);
		
		CDAttachment[] foundAttachments = new CDAttachment[compatibleArray.length];
		int index = 0;
		
		for (Object compatible : compatibleArray) {
			for (CDAttachment attach : CDAttachment.getAll()) {
				if (compatible == existingAttachments[attach.getId()]) {
					foundAttachments[index++] = attach;
				}
			}
		}
		
		return foundAttachments;
	}

    @Override
    public void setRPM(double rpm) {
        CDClasses.itemGunRoundsPerMinute.setValue(this.getItemInstance(), (float) rpm);
    }

    @Override
    public void setBulletsPerRound(int amount) {
        CDClasses.itemGunBulletsPerRound.setValue(this.getItemInstance(), amount);
    }

    @Override
    public int getMaximumBulletDistance() {
        return CDClasses.itemGunBulletDistanceField.getValue(this.getItemInstance());
    }

    @Override
    public void setMaximumBulletDistance(int distance) {
        CDClasses.itemGunBulletDistanceField.setValue(this.getItemInstance(), distance);
    }

    @Override
    public double getRecoil() {
        return CDClasses.itemGunRecoilField.getValue(this.getItemInstance());
    }

    @Override
    public void setRecoil(double recoil) {
        CDClasses.itemGunRecoilField.setValue(this.getItemInstance(), (float) recoil);
    }

    @Override
    public double getAccuracy() {
        return CDClasses.itemGunAccuracyField.getValue(this.getItemInstance());
    }

    @Override
    public void setAccuracy(double accuracy) {
        CDClasses.itemGunAccuracyField.setValue(this.getItemInstance(), (float) accuracy);
    }

    @Override
    public double getMovementPenalty() {
        return CDClasses.itemGunMovementPenaltyField.getValue(this.getItemInstance());
    }

    @Override
    public void setMovementPenalty(double amount) {
        CDClasses.itemGunMovementPenaltyField.setValue(this.getItemInstance(), amount);
    }

    @Override
    public double getZoomLevel() {
        return CDClasses.itemGunZoomLevelField.getValue(this.getItemInstance());
    }

    @Override
    public void setZoomLevel(double amount) {
        CDClasses.itemGunZoomLevelField.setValue(this.getItemInstance(), (float) amount);
    }

    @Override
    public void setSoundLevel(double soundLevel) {
        CDClasses.itemGunSoundLevel.setValue(this.getItemInstance(), (float) soundLevel);
    }

    @Override
    public boolean canSpreadWhileAiming() {
        return CDClasses.itemGunSpreadWhileAimingField.getValue(this.getItemInstance());
    }

    @Override
    public void setSpreadWhileAiming(boolean canSpread) {
        CDClasses.itemGunSpreadWhileAimingField.setValue(this.getItemInstance(), canSpread);
    }

    @Override
    public boolean hasCrosshair() {
        return CDClasses.itemGunRenderCrosshairsField.getValue(this.getItemInstance());
    }

    @Override
    public void setCrosshair(boolean hasCrosshair) {
        CDClasses.itemGunRenderCrosshairsField.setValue(this.getItemInstance(), hasCrosshair);
    }

}
