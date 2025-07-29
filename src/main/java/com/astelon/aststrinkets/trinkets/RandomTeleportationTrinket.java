package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.TeleportationUtils;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public abstract class RandomTeleportationTrinket extends Trinket {

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

    public Location findLocation(ItemStack mysteryShell, World world) {
        boolean localUseWorldDefault = isLocalUseWorldDefault(mysteryShell);
        boolean localSurfaceOnly = isLocalSurfaceOnly(mysteryShell);
        Integer localMinX = getLocalMinX(mysteryShell);
        Integer localMaxX = getLocalMaxX(mysteryShell);
        Integer localMinY = getLocalMinY(mysteryShell);
        Integer localMaxY = getLocalMaxY(mysteryShell);
        Integer localMinZ = getLocalMinZ(mysteryShell);
        Integer localMaxZ = getLocalMaxZ(mysteryShell);
        if (!localUseWorldDefault && !localSurfaceOnly &&
                localMinX == null && localMaxX == null &&
                localMinY == null && localMaxY == null &&
                localMinZ == null && localMaxZ == null) {
            localUseWorldDefault = useWorldDefault;
            localSurfaceOnly = surfaceOnly;
            localMinX = minX;
            localMaxX = maxX;
            localMinY = minY;
            localMaxY = maxY;
            localMinZ = minZ;
            localMaxZ = maxZ;
        }
        return TeleportationUtils.getRandomLocation(random, world, localUseWorldDefault, localSurfaceOnly, localMinX,
                localMaxX, localMinY, localMaxY, localMinZ, localMaxZ);
    }

    public boolean isLocalUseWorldDefault(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.has(keys.useWorldDefaultKey, PersistentDataType.BYTE);
    }

    public boolean isUseWorldDefault() {
        return useWorldDefault;
    }

    public void setUseWorldDefault(boolean useWorldDefault) {
        this.useWorldDefault = useWorldDefault;
    }

    public boolean isLocalSurfaceOnly(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.has(keys.surfaceOnlyKey, PersistentDataType.BYTE);
    }

    public boolean isSurfaceOnly() {
        return surfaceOnly;
    }

    public void setSurfaceOnly(boolean surfaceOnly) {
        this.surfaceOnly = surfaceOnly;
    }

    public Integer getLocalMinX(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.get(keys.minXKey, PersistentDataType.INTEGER);
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public Integer getLocalMaxX(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.get(keys.maxXKey, PersistentDataType.INTEGER);
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public Integer getLocalMinY(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.get(keys.minYKey, PersistentDataType.INTEGER);
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public Integer getLocalMaxY(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.get(keys.maxYKey, PersistentDataType.INTEGER);
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public Integer getLocalMinZ(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.get(keys.minZKey, PersistentDataType.INTEGER);
    }

    public int getMinZ() {
        return minZ;
    }

    public void setMinZ(int minZ) {
        this.minZ = minZ;
    }

    public Integer getLocalMaxZ(ItemStack mysteryShell) {
        PersistentDataContainer container = mysteryShell.getItemMeta().getPersistentDataContainer();
        return container.get(keys.maxZKey, PersistentDataType.INTEGER);
    }

    public int getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(int maxZ) {
        this.maxZ = maxZ;
    }
}
