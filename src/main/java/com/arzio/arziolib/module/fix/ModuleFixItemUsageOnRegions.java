package com.arzio.arziolib.module.fix;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.arzio.arziolib.api.event.IndirectCDEvent;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent;
import com.arzio.arziolib.api.event.packet.CDBulletHitEvent.HitType;
import com.arzio.arziolib.api.event.packet.CDGrenadeThrowEvent;
import com.arzio.arziolib.api.event.packet.CDGunTriggerEvent;
import com.arzio.arziolib.api.region.Flags;
import com.arzio.arziolib.api.util.CDMaterial;
import com.arzio.arziolib.api.util.reflection.ReflectionHelper;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

import net.minecraft.server.v1_6_R3.EntityLiving;
import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;

@RegisterModule(name = "fix-item-usage-on-regions-without-pvp")
public class ModuleFixItemUsageOnRegions extends Module {
	
    @ForgeSubscribe
    @IndirectCDEvent(cdClassName = "EventFlamethrowerSetFire")
    public void fixFlamethrowerDamageOnSafezones(Event event) {
        EntityPlayer attacker = ReflectionHelper.getValueFromEvent(event, EntityPlayer.class);
        EntityLiving entityHit = ReflectionHelper.getValueFromEvent(event, EntityLiving.class);
        
        Player playerAttacker = attacker.getBukkitEntity();
        LivingEntity living = (LivingEntity) entityHit.getBukkitEntity();
        
        // Both 'hit' and 'attacker' are players
        if (living instanceof Player) {
            if (!Flags.canRegionHavePvP(playerAttacker.getLocation()) || !Flags.canRegionHavePvP(living.getLocation())) {
                event.setCanceled(true);
            }
        }
    }
	
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void fixTaserTriggerInSafezones(CDGunTriggerEvent event) {
        if (CDMaterial.TASER.isTypeOf(event.getHeldGun())) {
            if (!Flags.canRegionHavePvP(event.getPlayer().getLocation())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cYou cannot use Taser in safe zones!");
            }
        }
    }
	
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void fixTaserHitInSafezones(CDBulletHitEvent event) {
        if (CDMaterial.TASER.isTypeOf(event.getHeldGun())) {
            
            boolean shouldCancel = false;
            
            if (event.getHitType() == HitType.ENTITY) {
                shouldCancel = !Flags.canRegionHavePvP(event.getPlayer().getLocation()) || !Flags.canRegionHavePvP(event.getEntityHit().getLocation());
            } else {
                shouldCancel = !Flags.canRegionHavePvP(event.getPlayer().getLocation());
            }

            if (shouldCancel) {
                event.setCancelled(true);
            }
        }
    }
	
	@EventHandler
	public void onGrenadeThrowing(CDGrenadeThrowEvent event) {
		if (!Flags.canRegionHavePvP(event.getPlayer().getLocation())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§cPVP is disabled in this area!");
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		// Continue only if the clicked entity is a player
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}
		
		ItemStack stack = event.getPlayer().getItemInHand();
		
		// Check if the clicker is holding any item
		if (stack == null) {
			return;
		}
		
		// First, checks if its one of the disabled items.
		// Only disable the pvp if it is disabled in any of both locations.
		if (stack.getType() == CDMaterial.BLOODBAG.asMaterial() || stack.getType() == CDMaterial.HANDCUFFS.asMaterial()) {
			if (!Flags.canRegionHavePvP(event.getPlayer().getLocation()) || !Flags.canRegionHavePvP(event.getRightClicked().getLocation())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cPVP is disabled here!");
			}
		}
	}

}
