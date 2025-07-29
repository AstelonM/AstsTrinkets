package com.astelon.aststrinkets.trinkets.block;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.RandomTeleportationBlockTrinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class VoidGateway extends RandomTeleportationBlockTrinket {

    private int defaultX;
    private int defaultY;
    private int defaultZ;
    private boolean allowEntities;

    public VoidGateway(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "voidGateway", TextColor.fromHexString("#8E359C"), Power.CREATE_RANDOM_GATEWAYS,
                true, Usages.PLACE);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.RESPAWN_ANCHOR);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Void Gateway", TextColor.fromHexString("#232C65")));
        meta.lore(List.of(Component.text("You feel the blackness inside the"),
                Component.text("construct reaching into your mind,"),
                Component.text("asking you to release it. Will you"),
                Component.text("do so?")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public Location getDefaultLocation(World world) {
        return new Location(world, defaultX, defaultY, defaultZ);
    }

    public int getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(int defaultX) {
        this.defaultX = defaultX;
    }

    public int getDefaultY() {
        return defaultY;
    }

    public void setDefaultY(int defaultY) {
        this.defaultY = defaultY;
    }

    public int getDefaultZ() {
        return defaultZ;
    }

    public void setDefaultZ(int defaultZ) {
        this.defaultZ = defaultZ;
    }

    public boolean isAllowEntities() {
        return allowEntities;
    }

    public void setAllowEntities(boolean allowEntities) {
        this.allowEntities = allowEntities;
    }
}
