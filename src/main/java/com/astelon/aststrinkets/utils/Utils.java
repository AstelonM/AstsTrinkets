package com.astelon.aststrinkets.utils;

import com.astelon.aststrinkets.AstsTrinkets;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class Utils {

    private static final Color[] BASE_COLORS = new Color[] {
            Color.fromRGB(16383998), // white
            Color.fromRGB(10329495), // light gray
            Color.fromRGB(4673362), // gray
            Color.fromRGB(1908001), // black
            Color.fromRGB(8606770), // brown
            Color.fromRGB(11546150), // red
            Color.fromRGB(16351261), // orange
            Color.fromRGB(16701501), // yellow
            Color.fromRGB(8439583), // lime
            Color.fromRGB(6192150), // green
            Color.fromRGB(1481884), // cyan
            Color.fromRGB(3847130), // light blue
            Color.fromRGB(3949738), // blue
            Color.fromRGB(8991416), // purple
            Color.fromRGB(13061821), // magenta
            Color.fromRGB(15961002) // pink
    };

    public static final Color PERSONAL_COLOUR = Color.fromRGB(0x263265);

    // For some reasons no way to get this through the API
    public static final int OFF_HAND_SLOT = 40;

    public static final int TICKS_PER_SECOND = 20;

    public static final long DAY_TIME = 0;

    private static long lastLog;

    public static String serializeCoords(Location location) {
        return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" +
                location.getBlockZ();
    }

    public static String serializeCoordsLogging(Location location) {
        return location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " +
                location.getBlockZ();
    }

    public static String serializeCoordsCommand(Location location) {
        return location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
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

    /**
     * Transforms an item into another. If the source stack has only 1 item, it is removed and replaced with the target.
     * Otherwise, the stack size is decremented, and the target item is added to the inventory of the source if there
     * is space, or dropped at the player's location if there isn't.
     * @param source the source ItemStack that is transformed
     * @param target the target ItemStack that the source is transformed to
     * @param slot the inventory slot of the source item
     * @param inventory the inventory where the transformation happens
     * @param player the player who triggered the transformation
     */
    public static void transformItem(ItemStack source, ItemStack target, int slot, Inventory inventory, Player player) {
        int amount = source.getAmount();
        if (amount == 1) {
            inventory.setItem(slot, target);
        } else {
            source.subtract();
            HashMap<Integer, ItemStack> failed = inventory.addItem(target);
            if (!failed.isEmpty()) {
                player.getWorld().dropItemNaturally(player.getLocation(), target);
                player.sendMessage(Component.text("Your inventory was full. The resulting item was dropped near you.",
                        NamedTextColor.YELLOW));
            }
        }
    }

    /**
     * Transforms the item held by the player on the cursor.
     * See {@link Utils#transformItem(ItemStack, ItemStack, int, Inventory, Player)}.
     * @param source the source ItemStack that is held by the player
     * @param target the target ItemStack that the source is transformed to
     * @param inventory the inventory where the transformation happens
     * @param player the player who triggered the transformation
     */
    public static void transformCursorItem(ItemStack source, ItemStack target, Inventory inventory, Player player) {
        int amount = source.getAmount();
        if (amount == 1) {
            player.setItemOnCursor(target);
        } else {
            source.subtract();
            HashMap<Integer, ItemStack> failed = inventory.addItem(target);
            if (!failed.isEmpty()) {
                player.getWorld().dropItemNaturally(player.getLocation(), target);
                player.sendMessage(Component.text("Your inventory was full. The resulting item was dropped near you.",
                        NamedTextColor.YELLOW));
            }
        }
    }

    public static String locationToString(Location location) {
        return location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
    }

    public static String getBlockName(Material material) {
        return Arrays.stream(material.name().split("_"))
                .map(text -> text.charAt(0) + text.substring(1).toLowerCase()).collect(Collectors.joining(" "));
    }

    public static boolean isPetOwner(Entity entity, Player player) {
        if (!(entity instanceof Tameable) && !(entity instanceof Fox))
            return true;
        if (entity instanceof Tameable tameable) {
            if (!tameable.isTamed())
                return true;
            return player.getUniqueId().equals(tameable.getOwnerUniqueId());
        }
        Fox fox = (Fox) entity;
        AnimalTamer firstTamer = fox.getFirstTrustedPlayer();
        AnimalTamer secondTamer = fox.getSecondTrustedPlayer();
        return firstTamer == null && secondTamer == null ||
                firstTamer != null && firstTamer.getUniqueId().equals(player.getUniqueId()) ||
                secondTamer != null && secondTamer.getUniqueId().equals(player.getUniqueId());
    }

    public static double normalizeRate(double value) {
        value /= 100;
        if (value > 1)
            value = 1;
        if (value < 0)
            value = 0;
        return value;
    }

    public static boolean isShulkerBox(ItemStack itemStack) {
        return itemStack != null && itemStack.getType().name().contains("SHULKER_BOX");
    }

    public static double ensurePercentage(double source, double defaultPercentage) {
        if (source < 0 || source > 100)
            return defaultPercentage;
        return source;
    }

    public static int ensurePositive(int source, int defaultValue) {
        if (source < 0)
            return defaultValue;
        return source;
    }

    public static int ensureBoundsInclusive(int source, int min, int max) {
        if (source <= min)
            return min;
        return Math.min(source, max);
    }

    public static int getTotalExperience(Player player) {
        int currentLevel = player.getLevel();
        int experience = Math.round(getExpAtLevel(currentLevel) * player.getExp());
        while (currentLevel > 0) {
            currentLevel--;
            experience += getExpAtLevel(currentLevel);
        }
        if (experience < 0)
            experience = Integer.MAX_VALUE;
        return experience;
    }

    public static int getExpAtLevel(int level) {
        if (level <= 15)
            return (level * 2) + 7;
        else if (level <= 30)
            return (level * 5) - 38;
        else
            return (level * 9) - 158;
    }

    public static Color getRandomBaseColour(Random random) {
        return BASE_COLORS[random.nextInt(BASE_COLORS.length)];
    }

    public static Color generateRandomColour(Random random) {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return Color.fromRGB(red, green, blue);
    }

    public static int secondsToTicks(int seconds) {
        return seconds * TICKS_PER_SECOND;
    }

    public static double ticksToSeconds(int ticks) {
        return (double) ticks / TICKS_PER_SECOND;
    }

    public static boolean isNothing(ItemStack itemStack) {
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    public static Entity deserializeEntity(AstsTrinkets plugin, byte[] data, World world) {
        try {
            // No better way to do it yet
            //noinspection deprecation
            return Bukkit.getUnsafe().deserializeEntity(data, world);
        } catch (Exception e) {
            if (System.currentTimeMillis() - lastLog > 10000)
                plugin.getLogger().log(Level.WARNING, "Could not deserialize trapped entity.", e);
            else
                plugin.getLogger().warning("Could not deserialize trapped entity.");
            lastLog = System.currentTimeMillis();
            return null;
        }
    }

    public static boolean isBundle(ItemStack itemStack) {
        return itemStack != null && itemStack.getType() == Material.BUNDLE;
    }

    public static boolean isNotNetherZombie(Entity entity) {
        return !(entity instanceof PigZombie) && !(entity instanceof Zoglin);
    }

    public static void copyEquipment(Mob source, Mob destination) {
        EntityEquipment sourceEquipment = source.getEquipment();
        EntityEquipment destinationEquipment = destination.getEquipment();
        for (EquipmentSlot slot: EquipmentSlot.values()) {
            ItemStack item = sourceEquipment.getItem(slot);
            destinationEquipment.setItem(slot, item.clone());
        }
    }

    /**
     * Returns whether the column of blocks above the given location (inclusive) is passable.
     * @param origin the origin of the area to be checked, located at the bottom of it
     * @param height the height of the area checked, including origin
     * @param checkOrigin whether the origin block should be checked
     * @return <code>true</code> if any block within the checked area is not passable
     */
    public static boolean hasNoSpaceAbove(Location origin, int height, boolean checkOrigin) {
        int start = checkOrigin ? 0 : 1;
        for (int i = start; i < height; i++) {
            Location location = new Location(origin.getWorld(), origin.getBlockX(), origin.getBlockY() + i, origin.getBlockZ());
            if (!location.getBlock().isPassable())
                return true;
        }
        return false;
    }

    /**
     * Returns whether a cuboid area with a given origin and length is passable.
     * @param origin the origin of the cube to be checked, located at the bottom middle block
     * @param length the length of the sides of the cube. May only be an odd number
     * @param checkOrigin whether the origin block should be checked
     * @return <code>true</code> if any block within the checked area is not passable
     */
    public static boolean hasNoSpaceCuboid(Location origin, int length, boolean checkOrigin) {
        if (length % 2 == 0)
            throw new IllegalArgumentException("The length cannot be even.");
        for (int y = 0; y < length; y++) {
            int halfLength = length / 2;
            for (int x = -halfLength; x <= halfLength; x++) {
                for (int z = -halfLength; z <= halfLength; z++) {
                    if (!checkOrigin && x == 0 && y == 0 && z == 0)
                        continue;
                    Location location =  new Location(origin.getWorld(), origin.getBlockX() + x,
                            origin.getBlockY() + y, origin.getBlockZ() + z);
                    if (!location.getBlock().isPassable())
                        return true;
                }
            }
        }
        return false;
    }

    public static void copyCommonEntityAttributes(Mob source, Mob destination) {
        destination.customName(source.customName());
        destination.setCustomNameVisible(source.isCustomNameVisible());
        destination.setInvulnerable(source.isInvulnerable());
        destination.setRemoveWhenFarAway(source.getRemoveWhenFarAway());
        if (source instanceof Ageable sourceAgeable && destination instanceof Ageable destinationAgeable)
            destinationAgeable.setAge(sourceAgeable.getAge());
        if (source instanceof Tameable sourceTameable && destination instanceof Tameable destinationTameable)
            destinationTameable.setOwner(sourceTameable.getOwner());
        copyEquipment(source, destination);
    }

    public static int nextIntInclusive(Random random, int min, int max) {
        if (max == Integer.MAX_VALUE)
            throw new IllegalArgumentException("Max value cannot be the integer maximum.");
        return random.nextInt(min, max + 1);
    }
}
