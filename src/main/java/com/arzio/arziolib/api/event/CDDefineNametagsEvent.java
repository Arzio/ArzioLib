package com.arzio.arziolib.api.event;

import java.util.Collections;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class CDDefineNametagsEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Set<String> whitelist;
    private boolean isHidden;
    private boolean cancelled;
    
    public CDDefineNametagsEvent(Player player, boolean isHidden, Set<String> whitelist) {
        super(player);
        this.isHidden = isHidden;
        this.whitelist = whitelist;
    }
    
    public boolean isWhitelisted(Player player) {
        return this.isWhitelisted(player.getName());
    }
    
    public boolean isWhitelisted(String playerName) {
        return whitelist.contains(playerName.toLowerCase());
    }
    
    public void add(Player player) {
        this.add(player.getName());
    }
    
    public void add(String playerName) {
        this.whitelist.add(playerName.toLowerCase());
    }
    
    public void remove(Player player) {
        this.remove(player.getName());
    }
    
    public void remove(String playerName) {
        this.whitelist.remove(playerName.toLowerCase());
    }
    
    public void clearWhitelist() {
        this.whitelist.clear();
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }
    
    public Set<String> getUnmodifiableWhitelist(){
        return Collections.unmodifiableSet(this.whitelist);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    
}
