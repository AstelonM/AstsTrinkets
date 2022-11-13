package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ShapeShifter extends Trinket {

    private final List<Material> materials;
    private final Random random;

    public ShapeShifter(AstsTrinkets plugin, NamespacedKey nameKey, NamespacedKey powerKey) {
        super(plugin, nameKey, powerKey, "shapeShifter", NamedTextColor.AQUA, Power.SHAPE_SHIFTING, false);
        materials = Arrays.stream(Material.values())
                .filter(Material::isItem)
                .filter(material -> !material.isAir() && !material.isEmpty())
                .filter(this::filterMaterials)
                .filter(material -> material.getMaxDurability() == 0)
                .filter(this::filterMaterialNames)
                .collect(Collectors.toList());
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.DRAGON_EGG);
        setMeta(itemStack);
        return itemStack;
    }

    private boolean filterMaterials(Material material) {
        return material != Material.BEDROCK && material != Material.BARRIER &&
                material != Material.DEBUG_STICK && material != Material.END_GATEWAY && material != Material.JIGSAW &&
                material != Material.KNOWLEDGE_BOOK && material != Material.TOTEM_OF_UNDYING;
    }

    private boolean filterMaterialNames(Material material) {
        String name = material.name();
        return !name.startsWith("LEGACY") && !name.contains("COMMAND") && !name.contains("SPAWN") &&
                !name.contains("PORTAL") && !name.contains("INFESTED")  && !name.contains("STRUCTURE") &&
                !name.contains("BUCKET") && !name.contains("MINECART") && !name.contains("BOAT") &&
                !name.contains("FRAME");
    }

    public void removeItems(List<String> itemBlacklist) {
        for (String item: itemBlacklist) {
            materials.remove(Material.getMaterial(item));
        }
    }

    private void setMeta(ItemStack itemStack) {
        Material material = itemStack.getType();
        String materialName = material.name().toLowerCase().replace('_', ' ');
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            ItemFactory factory = plugin.getServer().getItemFactory();
            meta = factory.getItemMeta(material);
        }
        meta.displayName(Component.text("Not " + materialName, NamedTextColor.GREEN)
                .decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(Component.text("Or is it?")));
        itemStack.setItemMeta(meta);
    }

    public void shapeShift(ItemStack itemStack) {
        itemStack.setType(materials.get(random.nextInt(materials.size())));
        setMeta(itemStack);
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(nameKey, PersistentDataType.STRING, name);
        container.set(powerKey, PersistentDataType.STRING, power.powerName());
        itemStack.setItemMeta(meta);
    }
}
