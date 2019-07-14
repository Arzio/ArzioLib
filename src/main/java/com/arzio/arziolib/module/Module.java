package com.arzio.arziolib.module;

import java.lang.reflect.Constructor;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.arzio.arziolib.ArzioLib;
import com.arzio.arziolib.api.ForgeListener;

public abstract class Module implements ForgeListener, Listener {
    
    public void onEnable() {
        
    }
    
    public void onDisable() {
        
    }
    
    public Plugin getPlugin() {
        return ArzioLib.getInstance();
    }
    
    public static Module fromClass(Class<? extends Module> moduleClass) throws Throwable {
        Constructor<? extends Module> constructor = moduleClass.getConstructor();
        
        if (constructor == null) {
            throw new IllegalArgumentException("The class "+moduleClass.getSimpleName()+" does not have a default/empty constructor!");
        }
        
        return constructor.newInstance();
    }
}
