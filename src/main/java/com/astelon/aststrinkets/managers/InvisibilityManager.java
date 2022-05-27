package com.astelon.aststrinkets.managers;

import com.astelon.aststrinkets.AstsTrinkets;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class InvisibilityManager {

    private final AstsTrinkets plugin;

    private final HashSet<String> invisiblePlayers;
    private final HashSet<String> trulyInvisiblePlayers;

    public InvisibilityManager(AstsTrinkets plugin) {
        this.plugin = plugin;
        invisiblePlayers = new HashSet<>();
        trulyInvisiblePlayers = new HashSet<>();
    }

    public HashSet<String> getInvisiblePlayers() {
        return invisiblePlayers;
    }

    public HashSet<String> getTrulyInvisiblePlayers() {
        return trulyInvisiblePlayers;
    }

    public void addInvisiblePlayer(Player player) {
        invisiblePlayers.add(player.getName());
        for (Player otherPlayer: Bukkit.getOnlinePlayers()) {
            if (otherPlayer.equals(player))
                continue;
            if (!otherPlayer.hasPermission("aststrinkets.trinket.seeinvisible"))
                otherPlayer.hidePlayer(plugin, player);
        }
        player.sendMessage(Component.text("You are now invisible.", NamedTextColor.GOLD));
    }

    public void removeInvisiblePlayer(Player player) {
        boolean contained = invisiblePlayers.remove(player.getName());
        if (contained) {
            for (Player otherPlayer: Bukkit.getOnlinePlayers()) {
                if (otherPlayer.equals(player))
                    continue;
                otherPlayer.showPlayer(plugin, player);
            }
            player.sendMessage(Component.text("You are no longer invisible.", NamedTextColor.GOLD));
        }
    }

    public void addTrulyInvisiblePlayer(Player player) {
        trulyInvisiblePlayers.add(player.getName());
        for (Player otherPlayer: Bukkit.getOnlinePlayers()) {
            if (otherPlayer.equals(player))
                continue;
            otherPlayer.hidePlayer(plugin, player);
        }
        player.sendMessage(Component.text("You are now truly invisible.", NamedTextColor.GOLD));
    }

    public void removeTrulyInvisiblePlayer(Player player) {
        boolean contained = trulyInvisiblePlayers.remove(player.getName());
        if (contained) {
            for (Player otherPlayer: Bukkit.getOnlinePlayers()) {
                if (otherPlayer.equals(player))
                    continue;
                otherPlayer.showPlayer(plugin, player);
            }
            player.sendMessage(Component.text("You are no longer truly invisible.", NamedTextColor.GOLD));
        }
    }
}
