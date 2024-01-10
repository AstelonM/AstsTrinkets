package com.astelon.aststrinkets.trinkets.equipable;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class NightVisionGoggles extends Trinket {

    private final PotionEffect nightVision;

    public NightVisionGoggles(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "nightVisionGoggles", Power.NIGHT_VISION, false, Usages.WEAR);
        nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 4,
                false, false);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        meta.setColor(Color.BLACK);
        meta.displayName(Component.text("Night Vision Goggles", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("A pair of goggles attached to a"),
                Component.text("leather cap that allow you to see in"),
                Component.text("the dark.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void addNightVision(Player player) {
        player.addPotionEffect(nightVision);
    }

    public boolean hasNightVision(Player player) {
        return player.getActivePotionEffects().contains(nightVision);
    }

    public boolean isNightVision(PotionEffect effect) {
        return nightVision.equals(effect);
    }

    public void removeNightVision(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
