package me.splitque.minigames;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Game<T extends Arena> {
    public static Logger logger;
    public static boolean showLogs;
    private String name;
    private Map<String, T> arenas;

    public Game(String name, boolean showLogs) {
        this.name = name;
        this.arenas = new HashMap<>();
        logger = Logger.getLogger(name);
        Game.showLogs = showLogs;

        logger.info("Game created!");
    }

    // SETTERS
    public void addArena(T arena) {
        arenas.putIfAbsent(arena.getName(), arena);
        if (Game.showLogs) logger.info("Added arena: " + arena.getName());
    }

    // GETTERS
    public T getArena(String name) {
        return arenas.get(name);
    }
    public List<T> getArenas() {
        return new ArrayList<>(arenas.values());
    }
    public List<String> getNamesArenas() {
        return new ArrayList<>(arenas.keySet());
    }
    public Map<String, T> getMapArenas() {
        return arenas;
    }

    // METHODS
    public static void joinToArena(Arena arena, Player player) {
        arena.joinToArena(player);
    }
    public static void leaveFromArena(Arena arena, Player player) {
        arena.leaveFromArena(player);
    }
    public static void startArena(Arena arena) {
        arena.startArena();
    }
    public static void stopArena(Arena arena) {
        arena.stopArena();
    }
}
