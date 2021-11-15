package xyz.lotai.assassination.Util;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import xyz.lotai.assassination.Assassination;

public class Events implements Listener {
    private Boolean isRunning;

    public Events() {
        this.isRunning = false;
    }

    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            Assassination.getInstance().getServer().getPluginManager().registerEvents(this, Assassination.getInstance());
        }
    }

    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            HandlerList.unregisterAll(this);
        }
    }

    public Boolean isRunning() {
        return this.isRunning;
    }
}