package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.TeleportationUtils;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Random;

public abstract class RandomTeleportationTrinket extends Trinket {

    protected final Random random;
    protected boolean useWorldDefault;
    protected boolean surfaceOnly;
    protected int minX;
    protected int maxX;
    protected int minY;
    protected int maxY;
    protected int minZ;
    protected int maxZ;

    public RandomTeleportationTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, TextColor infoColour,
                                      Power power, boolean op, String usage) {
        super(plugin, keys, name, infoColour, power, op, usage);
        random = new Random();
    }

    public RandomTeleportationTrinket(AstsTrinkets plugin, NamespacedKeys keys, String name, Power power, boolean op, String usage) {
        super(plugin, keys, name, power, op, usage);
        random = new Random();
    }

    protected Location findLocation(PersistentDataContainer container, World world) {
        boolean localUseWorldDefault = isLocalUseWorldDefault(container);
        boolean localSurfaceOnly = isLocalSurfaceOnly(container);
        Integer localMinX = getLocalMinX(container);
        Integer localMaxX = getLocalMaxX(container);
        Integer localMinY = getLocalMinY(container);
        Integer localMaxY = getLocalMaxY(container);
        Integer localMinZ = getLocalMinZ(container);
        Integer localMaxZ = getLocalMaxZ(container);
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

    public boolean isLocalUseWorldDefault(PersistentDataContainer container) {
        return container.has(keys.useWorldDefaultKey, PersistentDataType.BYTE);
    }

    public boolean isUseWorldDefault() {
        return useWorldDefault;
    }

    public void setUseWorldDefault(boolean useWorldDefault) {
        this.useWorldDefault = useWorldDefault;
    }

    public boolean isLocalSurfaceOnly(PersistentDataContainer container) {
        return container.has(keys.surfaceOnlyKey, PersistentDataType.BYTE);
    }

    public boolean isSurfaceOnly() {
        return surfaceOnly;
    }

    public void setSurfaceOnly(boolean surfaceOnly) {
        this.surfaceOnly = surfaceOnly;
    }

    public Integer getLocalMinX(PersistentDataContainer container) {
        return container.get(keys.minXKey, PersistentDataType.INTEGER);
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public Integer getLocalMaxX(PersistentDataContainer container) {
        return container.get(keys.maxXKey, PersistentDataType.INTEGER);
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public Integer getLocalMinY(PersistentDataContainer container) {
        return container.get(keys.minYKey, PersistentDataType.INTEGER);
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public Integer getLocalMaxY(PersistentDataContainer container) {
        return container.get(keys.maxYKey, PersistentDataType.INTEGER);
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public Integer getLocalMinZ(PersistentDataContainer container) {
        return container.get(keys.minZKey, PersistentDataType.INTEGER);
    }

    public int getMinZ() {
        return minZ;
    }

    public void setMinZ(int minZ) {
        this.minZ = minZ;
    }

    public Integer getLocalMaxZ(PersistentDataContainer container) {
        return container.get(keys.maxZKey, PersistentDataType.INTEGER);
    }

    public int getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(int maxZ) {
        this.maxZ = maxZ;
    }
}
