package com.astelon.aststrinkets.utils;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;

import java.util.Random;

public final class TeleportationUtils {

    public static final int ATTEMPTS = 16;

    public static Location getRandomLocation(Random random, World world, boolean useWorldDefault, boolean surfaceOnly,
                                             Integer minX, Integer maxX, Integer minY, Integer maxY, Integer minZ, Integer maxZ) {
        WorldBorder border = world.getWorldBorder();
        int properMinX = minX == null ? getWorldMinX(border) : minX;
        int properMaxX = maxX == null ? getWorldMaxX(border) : maxX;
        int properMinZ = minZ == null ? getWorldMinZ(border) : minZ;
        int properMaxZ = maxZ == null ? getWorldMaxZ(border) : maxZ;
        int properMinY = minY == null ? getWorldMinY(world) : minY;
        int properMaxY = maxY == null ? getWorldMaxY(world) : maxY;
        for (int i = 0; i < ATTEMPTS; i++) {
            int x = getRandomX(random, useWorldDefault, border, properMinX, properMaxX);
            int z = getRandomZ(random, useWorldDefault, border, properMinZ, properMaxZ);
            int y = getRandomY(random, useWorldDefault, surfaceOnly, x, z, world, properMinY, properMaxY);
            Location location = new Location(world, x, y, z);
            Location result = findNearestFreeLocation(location);
            if (result != null)
                return result.toCenterLocation();
        }
        return null;
    }

    private static int getRandomX(Random random, boolean useWorldDefault, WorldBorder border, int minX, int maxX) {
        int currentMinX = useWorldDefault ? getWorldMinX(border) : minX;
        int currentMaxX = useWorldDefault ? getWorldMaxX(border) :
                maxX;
        return Utils.nextIntInclusive(random, currentMinX, currentMaxX);
    }

    private static int getWorldMinX(WorldBorder border) {
        return (int) (border.getCenter().getBlockX() - border.getSize() / 2);
    }

    private static int getWorldMaxX(WorldBorder border) {
        return (int) (border.getCenter().getBlockX() + border.getSize() / 2);
    }

    private static int getRandomZ(Random random, boolean useWorldDefault, WorldBorder border, int minZ, int maxZ) {
        int currentMinZ = useWorldDefault ? getWorldMinZ(border) : minZ;
        int currentMaxZ = useWorldDefault ? getWorldMaxZ(border) : maxZ;
        return Utils.nextIntInclusive(random, currentMinZ, currentMaxZ);
    }

    private static int getWorldMinZ(WorldBorder border) {
        return (int) (border.getCenter().getBlockZ() - border.getSize() / 2);
    }

    private static int getWorldMaxZ(WorldBorder border) {
        return (int) (border.getCenter().getBlockZ() + border.getSize() / 2);
    }

    private static int getRandomY(Random random, boolean useWorldDefault, boolean surfaceOnly, int x, int z,
                           World world, int minY, int maxY) {
        if (surfaceOnly) {
            return world.getHighestBlockYAt(x, z) + 1;
        }
        int currentMinY = useWorldDefault ? getWorldMinY(world) : minY;
        int currentMaxY = useWorldDefault ? getWorldMaxY(world) : maxY;
        return Utils.nextIntInclusive(random, currentMinY, currentMaxY);
    }

    private static int getWorldMinY(World world) {
        return world.getMinHeight() + 1;
    }

    private static int getWorldMaxY(World world) {
        return world.getLogicalHeight();
    }

    private static Location findNearestFreeLocation(Location origin) {
        if (isValid(origin))
            return origin;
        Block block = origin.getBlock();
        boolean searchDown = true;
        // If this is air or effectively air, look for the first block below that's valid
        if (!isPassableUnsafe(block)) {
            Location firstBelow = findFirstValidBelow(origin);
            if (firstBelow != null)
                return firstBelow;
            else
                searchDown = false;
        }
        // If none found yet, search up and down, or only up if down was already searched
        World world = origin.getWorld();
        int x = origin.getBlockX();
        int y = origin.getBlockY();
        int z = origin.getBlockZ();
        for (int distance = 1; distance <= ATTEMPTS; distance++) {
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

    private static Location findFirstValidBelow(Location origin) {
        World world = origin.getWorld();
        Block highestLocation = world.getHighestBlockAt(origin, HeightMap.MOTION_BLOCKING);
        if (origin.getBlockY() > highestLocation.getY()) {
            Block above = getBlockAbove(highestLocation);
            if (isValid(above))
                return above.getLocation();
            return null;
        }
        int x = origin.getBlockX();
        int y = origin.getBlockY();
        int z = origin.getBlockZ();
        for (int changedY = y - 1; changedY > world.getMinHeight(); changedY--) {
            Block below = new Location(world, x, changedY, z).getBlock();
            if (isValid(below))
                return below.getLocation();
            if (isPassableUnsafe(below)) //TODO maybe allow 1 deep water
                return null;
        }
        return null;
    }

    private static boolean isValid(Block block) {
        return isValid(block.getLocation());
    }

    private static boolean isValid(Location location) {
        Block below = getBlockBelow(location);
        if (!below.isCollidable() || isCollidableUnsafe(below))
            return false;
        Block lowerHalf = location.getBlock();
        if (lowerHalf.isCollidable() || isPassableUnsafe(lowerHalf))
            return false;
        Block upperHalf = new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ())
                .getBlock();
        return !upperHalf.isCollidable() && !isPassableUnsafe(upperHalf);
    }

    public static boolean isPassableUnsafe(Block block) {
        return block.isPassable() && (isLiquid(block) || isFire(block) || isSpiky(block) || isFreezing(block));
    }

    public static boolean isLiquid(Block block) {
        return block.isLiquid() ||
                block.isPassable() && block.getBlockData() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged() ||
                block.getType() == Material.KELP_PLANT || block.getType() == Material.SEAGRASS || block.getType() == Material.TALL_SEAGRASS;
    }

    public static boolean isFire(Block block) {
        return block.getType() == Material.FIRE || block.getType() == Material.SOUL_FIRE;
    }

    public static boolean isSpiky(Block block) {
        return block.getType() == Material.SWEET_BERRY_BUSH;
    }

    public static boolean isFreezing(Block block) {
        return block.getType() == Material.POWDER_SNOW;
    }

    public static boolean isCollidableUnsafe(Block block) {
        return isSolidBurning(block) || isUnsafeSnow(block) || isSolidSpiky(block);
    }

    public static boolean isSolidBurning(Block block) {
        return block.getType() == Material.MAGMA_BLOCK || block.getType() == Material.CAMPFIRE || block.getType() == Material.SOUL_CAMPFIRE;
    }

    public static boolean isUnsafeSnow(Block block) {
        if (block.getType() == Material.SNOW) {
            Block below = getBlockBelow(block);
            return !below.isSolid();
        }
        return false;
    }

    public static boolean isSolidSpiky(Block block) {
        return block.getType() == Material.CACTUS;
    }

    public static Block getBlockBelow(Block block) {
        return new Location(block.getWorld(), block.getX(), block.getY() - 1, block.getZ()).getBlock();
    }

    public static Block getBlockBelow(Location location) {
        return new Location(location.getWorld(), location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()).getBlock();
    }

    public static Block getBlockAbove(Block block) {
        return new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ()).getBlock();
    }

    public static Block getBlockAbove(Location location) {
        return new Location(location.getWorld(), location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()).getBlock();
    }
}
