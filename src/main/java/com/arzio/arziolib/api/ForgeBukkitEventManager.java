package com.arzio.arziolib.api;

import java.util.Collection;

import org.bukkit.plugin.Plugin;

public interface ForgeBukkitEventManager {

    public void registerEvents(Plugin plugin, ForgeListener listener);

    public void unregisterEvents(ForgeListener listener);

    public void unregisterEvents(Plugin plugin);

    public void unregisterAll();

    public Collection<Plugin> getRegisteredPlugins();

    public Collection<ForgeListener> getListeners(Plugin plugin);
}
