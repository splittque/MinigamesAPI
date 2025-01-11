package me.splitque.minigames.logics;

import me.splitque.minigames.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class MainLogic<T extends Arena> implements Listener {
    public T arena;

    public void init(T arena) {
        this.arena = arena;
    }

    public abstract void onJoin(Player player);
    public abstract void onLeave(Player player);
    public abstract void onStart();
    public abstract void onStop();
}
