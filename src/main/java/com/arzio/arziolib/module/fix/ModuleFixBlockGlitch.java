package com.arzio.arziolib.module.fix;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.arzio.arziolib.api.util.reflection.ReflectionHelper;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

import net.minecraft.server.v1_6_R3.EntityLiving;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;

/*
 * WIP.
 */
@RegisterModule(name = "fix-block-glitch")
public class ModuleFixBlockGlitch extends Module {

    @ForgeSubscribe
    public void armorBreakingFix(LivingJumpEvent event) {
        EntityLiving living = ReflectionHelper.getValueFromEvent(event, EntityLiving.class);
        Entity entity = living.getBukkitEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;

            Block blockBelow = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

            // isOnGround value can be changed through hacked clients.
            if (blockBelow.isEmpty() && player.isOnGround()) {
                player.teleport(player.getLocation());
            }
        }
    }
}
