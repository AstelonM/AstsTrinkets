package com.astelon.aststrinkets.trinkets.inventory;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.Trinket;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

public class ResurrectionScroll extends Trinket {

    private boolean allowWithers;
    private boolean allowEnderDragons;

    public ResurrectionScroll(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "resurrectionScroll", NamedTextColor.YELLOW, Power.RESURRECTION, true, Usages.RESURRECTION_SCROLL);
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.MAGENTA_BANNER);
        BannerMeta meta = (BannerMeta) itemStack.getItemMeta();
        meta.setPatterns(List.of(
                new Pattern(DyeColor.BLACK, PatternType.BRICKS),
                new Pattern(DyeColor.MAGENTA, PatternType.BORDER),
                new Pattern(DyeColor.MAGENTA, PatternType.TRIANGLES_TOP),
                new Pattern(DyeColor.MAGENTA, PatternType.TRIANGLES_BOTTOM),
                new Pattern(DyeColor.MAGENTA, PatternType.BRICKS)
        ));
        meta.displayName(Component.text("Scroll of Resurrection", NamedTextColor.LIGHT_PURPLE));
        meta.lore(List.of(Component.text("On the scroll you recognize a"),
                Component.text("resurrection spell. The material"),
                Component.text("used is curious, but you think"),
                Component.text("best not to question it.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean isAllowWithers() {
        return allowWithers;
    }

    public void setAllowWithers(boolean allowWithers) {
        this.allowWithers = allowWithers;
    }

    public boolean isAllowEnderDragons() {
        return allowEnderDragons;
    }

    public void setAllowEnderDragons(boolean allowEnderDragons) {
        this.allowEnderDragons = allowEnderDragons;
    }
}
