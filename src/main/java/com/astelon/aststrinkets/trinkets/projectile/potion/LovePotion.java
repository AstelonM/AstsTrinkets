package com.astelon.aststrinkets.trinkets.projectile.potion;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.projectile.ProjectileTrinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class LovePotion extends ProjectileTrinket {

    private int loveTime;

    public LovePotion(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "lovePotion", TextColor.fromHexString("#FFFF00"), Power.LOVE_MODE, false, Usages.THROW);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.AWKWARD));
        meta.setColor(Color.fromRGB(0xFF0000));
        meta.displayName(Component.text("Love Potion", TextColor.fromHexString("#FF0000")));
        meta.lore(List.of(Component.text("They say love is in the air, but it"),
                Component.text("is in fact in this very bottle.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public int getLoveTime() {
        return loveTime;
    }

    public void setLoveTime(int loveTime) {
        this.loveTime = loveTime;
    }
}
