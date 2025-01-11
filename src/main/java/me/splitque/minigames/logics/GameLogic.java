package me.splitque.minigames.logics;

import me.splitque.minigames.Arena;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GameLogic<T extends Arena> implements Listener {
    public T arena;

    public GameLogic(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void init(T arena) {
        this.arena = arena;
    }
}
