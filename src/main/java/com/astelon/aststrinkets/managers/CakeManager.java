package com.astelon.aststrinkets.managers;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;

public class CakeManager {

    private final AstsTrinkets plugin;
    private final HashSet<String> cakes;

    public CakeManager(AstsTrinkets plugin) {
        this.plugin = plugin;
        cakes = new HashSet<>();
    }

    public void init() {
        plugin.getLogger().info("Loading cakes.");
        File cakesFile = new File(plugin.getDataFolder(), "cakes.txt");
        if (!cakesFile.exists()) {
            plugin.getLogger().info("Found no cakes.txt file in the data folder. No cakes loaded.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(cakesFile))) {
            reader.lines().forEach(line -> {
                if (line.isEmpty() || line.isBlank())
                    return;
                Location location = Utils.deserializeCoords(line);
                if (location == null) {
                    plugin.getLogger().warning("Invalid cake coordinates: " + line);
                    return;
                }
                cakes.add(line);
            });
        } catch (IOException e) {
            plugin.getLogger().severe("Could not read cakes.txt. No cakes loaded.");
            e.printStackTrace();
        }
        plugin.getLogger().info("Loaded " + cakes.size() + " cakes.");
    }

    public boolean isCakeLocation(Location location) {
        return cakes.contains(Utils.serializeCoords(location));
    }

    public void addCake(Location location) {
        boolean added = cakes.add(Utils.serializeCoords(location));
        if (added) {
            plugin.getLogger().info("A mystery cake was placed at " + Utils.serializeCoordsLogging(location));
            String cakesOutput = formatCakes();
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> saveCakes(cakesOutput));
        }
    }

    public void removeCake(Location location) {
        boolean removed = cakes.remove(Utils.serializeCoords(location));
        if (removed) {
            plugin.getLogger().info("A mystery cake was removed from " + Utils.serializeCoordsLogging(location));
            String cakesOutput = formatCakes();
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> saveCakes(cakesOutput));
        }
    }

    private String formatCakes() {
        return String.join("\n", cakes);
    }

    private void saveCakes(String cakesOutput) {
        File cakesFile = new File(plugin.getDataFolder(), "cakes.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(cakesFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(cakesOutput);
            writer.flush();
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save the cake list.");
            e.printStackTrace();
        }
    }
}
