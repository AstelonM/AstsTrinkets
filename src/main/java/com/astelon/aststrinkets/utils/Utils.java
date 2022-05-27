package com.astelon.aststrinkets.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Utils {

    public static String serializeCoords(Location location) {
        return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" +
                location.getBlockZ();
    }

    public static String serializeCoordsLogging(Location location) {
        return location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " +
                location.getBlockZ();
    }

    public static Location deserializeCoords(String coords) {
        String[] components = coords.split(";");
        if (components.length != 4)
            return null;
        World world = Bukkit.getWorld(components[0]);
        if (world == null)
            return null;
        int x, y, z;
        try {
            x = Integer.parseInt(components[1]);
            y = Integer.parseInt(components[2]);
            z = Integer.parseInt(components[3]);
        } catch (NumberFormatException e) {
            return null;
        }
        return new Location(world, x, y, z);
    }
}
