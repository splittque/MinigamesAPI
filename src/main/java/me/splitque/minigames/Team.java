package me.splitque.minigames;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private ChatColor color;
    private int maxPlayers;
    private int minPlayers;
    private List<Player> players;
    private Location spawn;

    public Team(String name, ChatColor color, int maxPlayers, int minPlayers) {
        this.name = name;
        this.color = color;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.players = new ArrayList<>(maxPlayers);

        if (Game.showLogs) Game.logger.info("Created team: " + name);
    }

    // SETTERS
    public void setSpawn(Location spawn) {
        this.spawn = spawn;
        if (Game.showLogs) Game.logger.info("Setted spawn for team: " + name);
    }
    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            if (Game.showLogs) Game.logger.info(player.getName() + " joined to team: " + name);
        }
    }
    public void deletePlayer(Player player) {
        if (players.contains(player)) {
            players.remove(player);
            if (Game.showLogs) Game.logger.info(player.getName() + " leave from team: " + name);
        }
    }
    public void clearPlayers() {
        players.clear();
        if (Game.showLogs) Game.logger.info("Players cleared in team: " + name);
    }

    // GETTERS
    public String getName() {
        return name;
    }
    public ChatColor getColor() {
        return color;
    }
    public String getNameWithColor() {
        return color + name;
    }
    public int getMaxPlayers() {
        return maxPlayers;
    }
    public int getMinPlayers() {
        return minPlayers;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public Location getSpawn() {
        return spawn;
    }

    // CHECKS
    public Boolean isPlayerOnTeam(Player player) {
        return players.contains(player);
    }
    public Boolean isTeamFull() {
        return players.size() == maxPlayers;
    }

    // METHODS
    public void teleportPlayersToSpawn() {
        for (Player player : players) {
            player.teleport(spawn);
            if (Game.showLogs) Game.logger.info(player.getName() + " teleported to spawn-team: " + name);
        }
    }
}
