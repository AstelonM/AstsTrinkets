package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public abstract class RandomTeleportationTrinket extends Trinket {

    private static final int TRIES = 16;

    private final Random random;
    private boolean useWorldDefault;
    private boolean surfaceOnly;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;
    private int minZ;
    private int maxZ;

    public RandomTeleportationTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour,
                                      Power power, boolean op, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
        random = new Random();
    }

    public RandomTeleportationTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op, String usage) {
        super(plugin, keys, name, power, op, usage);
        random = new Random();
    }

    public Location getRandomLocation(ItemStack mysteryShell, World world) {
        boolean useWorldDefault = isUseWorldDefault(mysteryShell);
        boolean surfaceOnly = isSurfaceOnly(mysteryShell);
        WorldBorder border = world.getWorldBorder();
        for (int i = 0; i < TRIES; i++) {
            int x = getRandomX(mysteryShell, useWorldDefault, border);
            int z = getRandomZ(mysteryShell, useWorldDefault, border);
            int y = getRandomY(mysteryShell, useWorldDefault, surfaceOnly, x, z, world);
            Location location = new Location(world, x, y, z);
            Location result = findNearestFreeLocation(location);
            if (result != null)
                return result.toCenterLocation();
        }
        return null;
    }

    private int getRandomX(ItemStack mysteryShell, boolean currentUseWorldDefault, WorldBorder border) {
        int currentMinX = currentUseWorldDefault ? (int) (border.getCenter().getBlockX() - border.getSize() / 2) :
                getMinX(mysteryShell);
        int currentMaxX = currentUseWorldDefault ? (int) (border.getCenter().getBlockX() + border.getSize() / 2) :
                getMaxX(mysteryShell);
        return random.nextInt(currentMinX, currentMaxX);
    }

    private int getRandomZ(ItemStack mysteryShell, boolean currentUseWorldDefault, WorldBorder border) {
        int currentMinZ = currentUseWorldDefault ? (int) (border.getCenter().getBlockZ() - border.getSize() / 2) :
                getMinZ(mysteryShell);
        int currentMaxZ = currentUseWorldDefault ? (int) (border.getCenter().getBlockZ() + border.getSize() / 2) :
                getMaxZ(mysteryShell);
        return random.nextInt(currentMinZ, currentMaxZ);
    }

    private int getRandomY(ItemStack mysteryShell, boolean currentUseWorldDefault, boolean currentSurfaceOnly, int x, int z,
                           World world) {
        if (currentSurfaceOnly) {
            return world.getHighestBlockYAt(x, z) + 1;
        }
        int currentMinY = currentUseWorldDefault ? world.getMinHeight() + 1 : getMinY(mysteryShell);
        int currentMaxY = currentUseWorldDefault ? world.getLogicalHeight() : getMaxY(mysteryShell);
        return random.nextInt(currentMinY, currentMaxY);
    }

    private Location findNearestFreeLocation(Location origin) {
        if (isValid(origin))
            return origin;
        Block block = origin.getBlock();
        boolean searchDown = true;
        if (!block.isCollidable() && !block.isLiquid()) {
            Location firstBelow = findFirstValidBelow(origin);
            if (firstBelow != null)
                return firstBelow;
            else
                searchDown = false;
        }
        World world = origin.getWorld();
        int x = origin.getBlockX();
        int y = origin.getBlockY();
        int z = origin.getBlockZ();
        for (int distance = 1; distance <= TRIES; distance++) {
            if (searchDown) {
                Location lower = new Location(world, x, y - distance, z);
                if (isValid(lower))
                    return lower;
            }
            Location upper = new Location(world, x, y + distance, z);
            if (isValid(upper))
                return upper;
        }
        return null;
    }

    private Location findFirstValidBelow(Location origin) {
        World world = origin.getWorld();
        int x = origin.getBlockX();
        int y = origin.getBlockY();
        int z = origin.getBlockZ();
        Location result = origin;
        for (int changedY = y - 1; changedY > world.getMinHeight(); changedY--) {
            Block below = new Location(world, x, changedY, z).getBlock();
            if (below.isCollidable())
                return result;
            if (below.isLiquid()) //TODO maybe allow 1 deep water
                return null;
            result = new Location(world, x, changedY, z);
        }
        return null;
    }

    private boolean isValid(Location location) {
        Block below = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ())
                .getBlock();
        if (!below.isCollidable())
            return false;
        Block lower = location.getBlock();
        if (lower.isCollidable() || lower.isLiquid())
            return false;
        Block upper = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ())
                .getBlock();
        return !upper.isCollidable() && !upper.isLiquid();
    }

    public boolean isUseWorldDefault(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        byte contained = container.getOrDefault(keys.useWorldDefaultKey, PersistentDataType.BYTE, (byte) -1);
        return contained == -1 ? useWorldDefault : contained == 1;
    }

    public void setUseWorldDefault(boolean useWorldDefault) {
        this.useWorldDefault = useWorldDefault;
    }

    public boolean isSurfaceOnly(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        byte contained = container.getOrDefault(keys.surfaceOnlyKey, PersistentDataType.BYTE, (byte) -1);
        return contained == -1 ? surfaceOnly : contained == 1;
    }

    public void setSurfaceOnly(boolean surfaceOnly) {
        this.surfaceOnly = surfaceOnly;
    }

    public int getMinX(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.minXKey, PersistentDataType.INTEGER, minX);
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.maxXKey, PersistentDataType.INTEGER, maxX);
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.minYKey, PersistentDataType.INTEGER, minY);
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.maxYKey, PersistentDataType.INTEGER, maxY);
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMinZ(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.minZKey, PersistentDataType.INTEGER, minZ);
    }

    public void setMinZ(int minZ) {
        this.minZ = minZ;
    }

    public int getMaxZ(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.maxZKey, PersistentDataType.INTEGER, maxZ);
    }

    public void setMaxZ(int maxZ) {
        this.maxZ = maxZ;
    }
}
