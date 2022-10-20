package com.astelon.aststrinkets.utils;

import com.astelon.aststrinkets.listeners.InventoryUseListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

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
}
