package com.elmakers.mine.bukkit.arenas.dueling;

import com.elmakers.mine.bukkit.api.magic.Mage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ArenaPlayer implements Comparable<ArenaPlayer> {
    private Mage mage;
    private final Arena arena;
    private final UUID uuid;
    private String name;
    private String displayName;

    private int wins;
    private int losses;
    private int quits;
    private int joins;
    private int draws;

    public ArenaPlayer(Arena arena, ConfigurationSection config) {
        mage = null;
        this.arena = arena;

        uuid = UUID.fromString(config.getString("uuid"));
        name = config.getString("name");
        displayName = config.getString("display_name");
        wins = config.getInt("wins");
        losses = config.getInt("losses");
        quits = config.getInt("quits");
        joins = config.getInt("joins");
        draws = config.getInt("draws");
    }

    public ArenaPlayer(Arena arena, Player player) {
        uuid = player.getUniqueId();
        this.arena = arena;
        update(player);
    }

    public void update(Player player) {
        if (player == null) {
            return;
        }

        name = player.getName();
        mage = arena.getController().getMagic().getMage(player);
        displayName = player.getDisplayName();

        wins = get("won");
        losses = get("lost");
        quits = get("quit");
        joins = get("joined");
        draws = get("draw");
    }

    public void save(ConfigurationSection config) {
        config.set("uuid", uuid.toString());
        config.set("name", name);
        config.set("display_name", displayName);
        config.set("wins", wins);
        config.set("losses", losses);
        config.set("quits", quits);
        config.set("joins", joins);
        config.set("draws", draws);
    }

    public boolean isValid() {
        return mage == null ? false : mage.isValid();
    }

    public boolean isDead() {
        return mage == null ? false : mage.isDead();
    }

    public Player getPlayer() {
        return mage == null ? null : mage.getPlayer();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void won() {
        Player player = getPlayer();
        if (player != null) {
            increment("won");
            wins = get("won");
        }
    }

    public void lost() {
        Player player = getPlayer();
        if (player != null) {
            increment("lost");
            losses = get("lost");
        }
    }

    public void quit() {
        Player player = getPlayer();
        if (player != null) {
            increment("quit");
            quits = get("quit");
        }
    }

    public void joined() {
        Player player = getPlayer();
        if (player != null) {
            increment("joined");
            joins = get("joined");
        }
    }

    public void draw() {
        Player player = getPlayer();
        if (player != null) {
            increment("draw");
            draws = get("draw");
        }
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getQuits() {
        return quits;
    }

    public int getJoins() {
        return joins;
    }

    public int getDraws() {
        return draws;
    }

    public boolean equals(Player player) {
        return (player != null && player.getUniqueId().equals(uuid));
    }

    public int getValidMatches() {
        return wins + losses;
    }

    public Arena getArena() {
        return arena;
    }

    protected void increment(String statName) {
        if (mage == null) {
            return;
        }
        String arenaKey = "arena." + arena.getKey() + "." + statName;
        ConfigurationSection data = mage.getData();
        int currentValue = data.getInt(arenaKey, 0);
        data.set(arenaKey, currentValue + 1);
    }

    protected int get(String statName) {
        if (mage == null) {
            return 0;
        }
        String arenaKey = "arena." + arena.getKey() + "." + statName;
        ConfigurationSection data = mage.getData();
        return data.getInt(arenaKey, 0);
    }

    public float getWinRatio() {
        if (losses == 0 && wins == 0) return 0;
        return (float)wins / (losses + wins);
    }

    @Override
    public int compareTo(ArenaPlayer other) {
        return uuid.compareTo(other.getUUID());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ArenaPlayer)) return false;
        return uuid.equals(((ArenaPlayer)other).uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }
}