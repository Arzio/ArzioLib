package com.arzio.arziolib.module;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;

import com.arzio.arziolib.ArzioLib;

public class ModuleContainer {

    private List<BukkitTask> tasks = new ArrayList<>();
    private boolean isEnabled = false;
    private ArzioLib plugin;
    private boolean errored = false;
    private Module module;
    private final String name;

    public ModuleContainer(String name) {
        this.name = name;
    }

    public void onEnable() throws Throwable{
        // Wraps with the error in a try-catch and rethrow it.
        try {
            if (this.getModule() == null) {
                throw new IllegalStateException("This module container does not contains a module yet!");
            }
            
            this.getPlugin().getForgeBukkitEventManager().registerEvents(this.getPlugin(), this.getModule());
            Bukkit.getPluginManager().registerEvents(this.getModule(), this.getPlugin());

            for (Method m : module.getClass().getDeclaredMethods()) {
                if (m.isAnnotationPresent(RepeatingTask.class)) {
                    RepeatingTask task = m.getAnnotation(RepeatingTask.class);
                    this.addTask(new MethodCallback(m) {

                        @Override
                        public void run() {
                            try {
                                this.getMethod().invoke(getModule());
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }, task.delay(), task.period());
                }
            }
            
            this.getModule().onEnable();
            this.errored = false;
        } catch (Throwable t) {
            this.errored = true;
            throw t;
        }
    }

    public void onDisable() throws Throwable {
        try {
            this.getPlugin().getForgeBukkitEventManager().unregisterEvents(this.getModule());
            HandlerList.unregisterAll(this.getModule());

            for (BukkitTask task : this.getTasks()) {
                this.removeTask(task);
            }
            module.onDisable();
            this.errored = false;
        } catch (Throwable t) {
            this.errored = true;
            throw t;
        }
    }

    protected ArzioLib getPlugin() {
        if (this.plugin == null) {
            this.plugin = ArzioLib.getInstance();
        }
        return this.plugin;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void reload() throws Throwable {
        this.setEnabled(false);
        this.setEnabled(true);
    }

    public void setModule(Module module) {
        this.module = module;
    }
    
    public void setModule(Class<? extends Module> moduleClass) throws Throwable {
        try {
            this.setModule(Module.fromClass(moduleClass));
        } catch (Throwable t) {
            this.errored = true;
            throw t;
        }
    }

    public Module getModule() {
        return module;
    }

    public boolean isErrored() {
        return errored;
    }

    public void setErrored(boolean errored) {
        this.errored = errored;
    }

    public void setEnabled(boolean state) throws Throwable {
        if (isEnabled != state) {
            isEnabled = state;

            if (isEnabled) {
                this.onEnable();
                this.plugin.getLogger().info("Module " + this.getName() + " enabled!");
            } else {
                this.onDisable();
                this.plugin.getLogger().info("Module " + this.getName() + " disabled!");
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public void addTask(Runnable runnable, long delay, long period) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(ArzioLib.getInstance(), runnable, delay, period);
        this.tasks.add(task);
    }

    public void removeTask(BukkitTask task) {
        task.cancel();
        this.tasks.remove(task);
    }

    public List<BukkitTask> getTasks() {
        return Collections.unmodifiableList(new ArrayList<>(this.tasks));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModuleContainer other = (ModuleContainer) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public abstract class MethodCallback implements Runnable {

        private final Method method;

        public MethodCallback(Method method) {
            this.method = method;
        }

        public Method getMethod() {
            return method;
        }
    }
}
