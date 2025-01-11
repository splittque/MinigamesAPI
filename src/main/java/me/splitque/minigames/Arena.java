package me.splitque.minigames;

import me.splitque.minigames.logics.GameLogic;
import me.splitque.minigames.logics.MainLogic;
import me.splitque.minigames.timer.Task;
import me.splitque.minigames.timer.Timer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Arena<T extends Team> {
    private String name;
    private Team defaultTeam;
    private Map<String, T> teams;
    private List<GameLogic> gameLogics;
    private MainLogic mainLogic;
    private ArenaState state;
    private Timer timer;
    private Location lobby;
    private Location spectators;

    public Arena(String name, int teamNumber) {
        this.name = name;
        this.teams = new HashMap<>(teamNumber);
        this.gameLogics = new ArrayList<>();
        this.timer = new Timer();

        defaultTeam = new Team("default", ChatColor.WHITE, 100, 1);
        state = ArenaState.WAITING;

        starter();
        if (Game.showLogs) Game.logger.info("Created arena: " + name);
    }

    // STARTER
    private void starter() {
        while (state == ArenaState.WAITING) {
            if (isMinPlayers()) state = ArenaState.STARTING;
        }
        while (state == ArenaState.STARTING) {
            if (!isMinPlayers()) {
                state = ArenaState.WAITING;
                timer.stopTimer();
            }
        }
        if (state == ArenaState.STARTING) {
            timer.startTimer(() -> {
                startArena();
                timer.stopTimer();
            }, 30);
        }
    }

    // SETTERS
    public void setMainLogic(MainLogic mainLogic) {
        if (this.mainLogic == null) {
            this.mainLogic = mainLogic;
            this.mainLogic.init(this);
            if (Game.showLogs) Game.logger.info("Setted main logic for arena: " + name);
        }
    }
    public void addGameLogic(GameLogic logic) {
        if (!gameLogics.contains(logic)) {
            logic.init(this);
            gameLogics.add(logic);
            if (Game.showLogs) Game.logger.info("Added logic for arena: " + name);
        }
    }
    public void setLobby(Location lobby) {
        this.lobby = lobby;
        if (Game.showLogs) Game.logger.info("Setted lobby for arena: " + name);
    }
    public void setSpectators(Location spectators) {
        this.spectators = spectators;
        if (Game.showLogs) Game.logger.info("Setted spec-location for arena: " + name);
    }
    public void setState(ArenaState state) {
        this.state = state;
        if (Game.showLogs) Game.logger.info("Setted state " + state + " for arena: " + name);
    }
    public void addTeam(T team) {
        if (!teams.containsKey(team.getName())) {
            teams.put(team.getName(), team);
            if (Game.showLogs) Game.logger.info("Added team " + team.getName() + " for arena: " + name);
        }
    }
    public void addPlayer(Player player) {
        boolean isInGame = false;
        for (T team : teams.values()) { if (team.isPlayerOnTeam(player)) isInGame = true; }
        if (!isInGame) defaultTeam.addPlayer(player);
    }
    public void addPlayerToTeam(Player player, T team) {
        if (defaultTeam.isPlayerOnTeam(player)) defaultTeam.deletePlayer(player);

        for (T teams : teams.values()) {
            if (teams.isPlayerOnTeam(player)) teams.deletePlayer(player);
        }

        if (teams.containsKey(team.getName())) {
            teams.get(team.getName()).addPlayer(player);
        }
    }
    public void deletePlayer(Player player) {
        for (T team : teams.values()) {
            if (team.isPlayerOnTeam(player)) team.deletePlayer(player);
        }
    }

    // GETTERS
    public String getName() {
        return name;
    }
    public Location getLobby() {
        return lobby;
    }
    public Location getSpectators() {
        return spectators;
    }
    public ArenaState getState() {
        return state;
    }
    public Map<String, T> getMapTeams() {
        return teams;
    }
    public List<T> getTeams() {
        return new ArrayList<>(teams.values());
    }
    public List<String> getNameTeams() {
        return new ArrayList<>(teams.keySet());
    }
    public T getTeam(String name) {
        return teams.get(name);
    }
    public Team getDefaultTeam() {
        return defaultTeam;
    }
    public T getPlayerTeam(Player player) {
        for (T team : teams.values()) {
            if (team.isPlayerOnTeam(player)) return team;
        }
        return null;
    }
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        for (T team : teams.values()) {
            players.addAll(team.getPlayers());
        }
        return players;
    }

    // CHECKS
    public Boolean isPlayerOnArena(Player player) {
        for (T team : teams.values()) {
            if (team.isPlayerOnTeam(player)) return true;
        }
        return false;
    }
    public Boolean isMinPlayers() {
        int minPlayers = 0;
        for (T team : teams.values()) {
            minPlayers += team.getMinPlayers();
        }
        return minPlayers == getAllPlayers().size();
    }
    public Boolean isWaiting() {
        return state == ArenaState.WAITING;
    }
    public Boolean isStarting() {
        return state == ArenaState.STARTING;
    }
    public Boolean isStarted() {
        return state == ArenaState.STARTED;
    }

    // METHODS
    public void teleportPlayerToLobby(Player player) {
        for (T team : teams.values()) {
            if (team.isPlayerOnTeam(player)) {
                player.teleport(lobby);
                if (Game.showLogs) Game.logger.info(player.getName() + "teleported to lobby in arena: " + name);
            }
        }
    }
    public void teleportPlayersToLobby() {
        for (T team : teams.values()) {
            for (Player player : team.getPlayers()) {
                player.teleport(lobby);
                if (Game.showLogs) Game.logger.info(player.getName() + "teleported to lobby in arena: " + name);
            }
        }
    }
    public void teleportPlayerToSpectators(Player player) {
        for (T team : teams.values()) {
            if (team.isPlayerOnTeam(player)) {
                player.teleport(spectators);
                if (Game.showLogs) Game.logger.info(player.getName() + "teleported to spec-location in arena: " + name);
            }
        }
    }
    public void teleportPlayersToSpectators() {
        for (T team : teams.values()) {
            for (Player player : team.getPlayers()) {
                player.teleport(spectators);
                if (Game.showLogs) Game.logger.info(player.getName() + "teleported to spec-location in arena: " + name);
            }
        }
    }
    public void joinToArena(Player player) {
        addPlayer(player);
        mainLogic.onJoin(player);
    }
    public void leaveFromArena(Player player) {
        deletePlayer(player);
        mainLogic.onLeave(player);
    }
    public void startArena() {
        mainLogic.onStart();
    }
    public void stopArena() {
        mainLogic.onStop();
        starter();
    }
}

