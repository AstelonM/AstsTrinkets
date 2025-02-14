package com.astelon.aststrinkets.trinkets.creature;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PiglinAbstract;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.List;

public class SurfaceCure extends Trinket {

    public SurfaceCure(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "surfaceCure", Power.NETHER_ZOMBIFICATION_CURE, false, Usages.INTERACT_NETHER_ENTITY_CAN_ZOMBIFY);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.THICK));
        meta.setColor(Color.fromRGB(0xffb6c1));
        meta.displayName(Component.text("Surface Cure", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("A cure for the surface sickness that"),
                Component.text("many of the Nether denizens suffer"),
                Component.text("from. Administered orally."),
                Component.text(""),
                Component.text("Side effects include, but are not"),
                Component.text("limited to: death by axe, death"),
                Component.text("by crossbow bolt, death by goring.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public void makeImmuneToZombification(LivingEntity entity) {
        if (entity instanceof PiglinAbstract piglin)
            piglin.setImmuneToZombification(true);
        else if (entity instanceof Hoglin hoglin)
            hoglin.setImmuneToZombification(true);
    }
}
