package com.arzio.arziolib.module.addon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.event.CDDefineNametagsEvent;
import com.arzio.arziolib.api.wrapper.PlayerData;
import com.arzio.arziolib.api.wrapper.PlayerDataHandler;
import com.arzio.arziolib.module.Module;
import com.arzio.arziolib.module.RegisterModule;

import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.api.events.SimpleClansJoinEvent;
import net.sacredlabyrinth.phaed.simpleclans.api.events.SimpleClansLeaveEvent;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;

@RegisterModule(name = "addon-simpleclans-nametags")
public class ModuleAddonSimpleClansNametags extends Module {
    
    private PlayerDataHandler dataHandler = ArzioLib.getInstance().getPlayerDataHandler();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onDefineNametags(CDDefineNametagsEvent event) {
        ClanManager clanManager = SimpleClans.getInstance().getClanManager();
        ClanPlayer clanPlayer = clanManager.getClanPlayer(event.getPlayer());

        if (clanPlayer != null) {
            for (ClanPlayer clan : clanPlayer.getClan().getAllMembers()) {
                event.add(clan.getName());
            }
            for (ClanPlayer clan : clanPlayer.getClan().getAllAllyMembers()) {
                event.add(clan.getName());
            }
        }
    }

    @EventHandler
    public void onJoinClan(SimpleClansJoinEvent event) {
        Player thePlayer = event.getClanPlayer().toPlayer();
        
        // Sometimes the event is triggered when the player is offline.
        if (thePlayer != null) {
            PlayerData data = dataHandler.getPlayerData(thePlayer);
            data.resendViewableNametags();
        }
        
        for (ClanPlayer clanPlayer : event.getClan().getOnlineMembers()) {
            PlayerData memberData = dataHandler.getPlayerData(clanPlayer.toPlayer());
            memberData.resendViewableNametags();
        }
    }

    @EventHandler
    public void onLeaveClan(SimpleClansLeaveEvent event) {
        Player thePlayer = event.getClanPlayer().toPlayer();
        
        // Sometimes the event is triggered when the player is offline.
        if (thePlayer != null) {
            PlayerData data = dataHandler.getPlayerData(thePlayer);
            data.resendViewableNametags();
        }

        for (ClanPlayer clanPlayer : event.getClan().getOnlineMembers()) {
            PlayerData memberData = dataHandler.getPlayerData(clanPlayer.toPlayer());
            memberData.resendViewableNametags();
        }
    }

}
