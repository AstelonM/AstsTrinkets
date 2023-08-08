package com.astelon.aststrinkets.trinkets.projectile.arrow;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.Objects;

public class ExplosiveArrow extends ArrowTrinket {

    private float explosionPower;
    private boolean setFire;
    private boolean breakBlocks;

    public ExplosiveArrow(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "explosiveArrow", TextColor.fromHexString("#00FF00"), Power.CREATE_EXPLOSION, false,
                Usages.ARROW);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(Color.fromRGB(255, 255, 0));
        PotionData potionData = new PotionData(PotionType.INSTANT_DAMAGE);
        meta.setBasePotionData(potionData);
        meta.displayName(Component.text("Explosive Arrow", TextColor.fromHexString("#FFFF00")));
        meta.lore(List.of(Component.text("The tip is made out of an"),
                Component.text("explosive material.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void setProjectileTrinket(Projectile projectile, ItemStack itemStack) {
        super.setProjectileTrinket(projectile, itemStack);
        PersistentDataContainer container = projectile.getPersistentDataContainer();
        PersistentDataContainer sourceContainer = itemStack.getItemMeta().getPersistentDataContainer();
        if (sourceContainer.has(keys.explosionPowerKey, PersistentDataType.FLOAT))
            container.set(keys.explosionPowerKey, PersistentDataType.FLOAT,
                    Objects.requireNonNull(sourceContainer.get(keys.explosionPowerKey, PersistentDataType.FLOAT)));
        if (sourceContainer.has(keys.setFireKey, PersistentDataType.BYTE))
            container.set(keys.setFireKey, PersistentDataType.BYTE,
                    Objects.requireNonNull(sourceContainer.get(keys.setFireKey, PersistentDataType.BYTE)));
        if (sourceContainer.has(keys.breakBlocksKey, PersistentDataType.BYTE))
            container.set(keys.breakBlocksKey, PersistentDataType.BYTE,
                    Objects.requireNonNull(sourceContainer.get(keys.breakBlocksKey, PersistentDataType.BYTE)));
    }

    public float getExplosionPower(ItemStack explosiveArrow) {
        PersistentDataContainer container = explosiveArrow.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.explosionPowerKey, PersistentDataType.FLOAT, explosionPower);
    }

    public boolean isSetFire(ItemStack explosiveArrow) {
        PersistentDataContainer container = explosiveArrow.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.setFireKey, PersistentDataType.BYTE, (byte) (setFire ? 1 : 0)) == (byte) 1;
    }

    public boolean isBreakBlocks(ItemStack explosiveArrow) {
        PersistentDataContainer container = explosiveArrow.getItemMeta().getPersistentDataContainer();
        return container.getOrDefault(keys.breakBlocksKey, PersistentDataType.BYTE, (byte) (breakBlocks ? 1 : 0)) == (byte) 1;
    }

    public float getExplosionPower(Arrow explosiveArrow) {
        PersistentDataContainer container = explosiveArrow.getPersistentDataContainer();
        return container.getOrDefault(keys.explosionPowerKey, PersistentDataType.FLOAT, explosionPower);
    }

    public boolean isSetFire(Arrow explosiveArrow) {
        PersistentDataContainer container = explosiveArrow.getPersistentDataContainer();
        return container.getOrDefault(keys.setFireKey, PersistentDataType.BYTE, (byte) (setFire ? 1 : 0)) == (byte) 1;
    }

    public boolean isBreakBlocks(Arrow explosiveArrow) {
        PersistentDataContainer container = explosiveArrow.getPersistentDataContainer();
        return container.getOrDefault(keys.breakBlocksKey, PersistentDataType.BYTE, (byte) (breakBlocks ? 1 : 0)) == (byte) 1;
    }

    public void setExplosionPower(float explosionPower) {
        this.explosionPower = explosionPower;
    }

    public void setSetFire(boolean setFire) {
        this.setFire = setFire;
    }

    public void setBreakBlocks(boolean breakBlocks) {
        this.breakBlocks = breakBlocks;
    }
}
