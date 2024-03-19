package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.trinkets.inventory.BindingPowder;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Die extends Trinket {

    private final Random random;
    private int sides;

    public Die(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "die", Power.SELECT_RANDOM_NUMBER_WHEN_THROWN, false, Usages.THROW_DROP_KEY_NO_SNEAKING);
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.POLISHED_BLACKSTONE_BUTTON);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Die", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("A small, pretty die with multiple"),
                Component.text("faces which you can throw to see"),
                Component.text("what number you get.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public int rollDie(ItemStack die) {
        ItemMeta meta = die.getItemMeta();
        int dieSides = getSides(die);
        int number = random.nextInt(dieSides) + 1;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        List<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        newLore.add(Component.text("Faces: " + dieSides, infoColour).decoration(TextDecoration.ITALIC, false));
        newLore.add(Component.text("Last roll: " + number, infoColour).decoration(TextDecoration.ITALIC, false));
        newLore.add(Component.text("A small, pretty die with multiple"));
        newLore.add(Component.text("faces which you can throw to see"));
        newLore.add(Component.text("what number you get."));
        meta.lore(newLore);
        die.setItemMeta(meta);
        return number;
    }

    public List<Integer> rollDie(Item die) {
        ItemStack originalStack = die.getItemStack();
        Location location = die.getLocation();
        World world = die.getWorld();
        List<Integer> numbers = new ArrayList<>(originalStack.getAmount());
        for (int i = 0; i < originalStack.getAmount() - 1; i++) {
            ItemStack newDie = originalStack.asOne();
            numbers.add(rollDie(newDie));
            Item newItem = world.dropItem(location, newDie);
            cloneAttributes(die, newItem);
        }
        ItemStack finalDie = originalStack.asOne();
        numbers.add(rollDie(finalDie));
        die.setItemStack(finalDie);
        return numbers;
    }

    private void cloneAttributes(Item original, Item clone) {
        clone.setOwner(original.getOwner());
        clone.setThrower(original.getThrower());
        clone.setPickupDelay(original.getPickupDelay());
        clone.setVelocity(original.getVelocity());
    }

    public void removeRoll(ItemStack die) {
        ItemMeta meta = die.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        int dieSides = container.getOrDefault(keys.maxAmountKey, PersistentDataType.INTEGER, sides);
        List<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            String ownerName = container.get(keys.ownerKey, PersistentDataType.STRING);
            newLore.add(BindingPowder.getOwnerLoreLine(ownerName, infoColour));
        }
        newLore.add(Component.text("Faces: " + dieSides, infoColour).decoration(TextDecoration.ITALIC, false));
        newLore.add(Component.text("A small, pretty die with multiple"));
        newLore.add(Component.text("faces which you can throw to see"));
        newLore.add(Component.text("what number you get."));
        meta.lore(newLore);
        die.setItemMeta(meta);
    }

    public boolean isSimilar(ItemStack firstDie, ItemStack secondDie) {
        if (firstDie.getType() != secondDie.getType())
            return false;
        int firstSides = getSides(firstDie);
        int secondSides = getSides(secondDie);
        if (firstSides != secondSides)
            return false;
        PersistentDataContainer firstContainer = firstDie.getItemMeta().getPersistentDataContainer();
        PersistentDataContainer secondContainer = secondDie.getItemMeta().getPersistentDataContainer();
        String firstOwner = firstContainer.get(keys.ownerKey, PersistentDataType.STRING);
        String secondOwner = secondContainer.get(keys.ownerKey, PersistentDataType.STRING);
        return Objects.equals(firstOwner, secondOwner);
    }

    public int getSides() {
        return sides;
    }

    public int getSides(ItemStack die) {
        ItemMeta meta = die.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.getOrDefault(keys.maxAmountKey, PersistentDataType.INTEGER, sides);
    }

    public void setSides(int sides) {
        this.sides = sides;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.maxAmountKey, PersistentDataType.INTEGER, sides);
        meta.lore(List.of(Component.text("Faces: " + sides, infoColour).decoration(TextDecoration.ITALIC, false),
                Component.text("A small, pretty die with multiple"),
                Component.text("faces which you can throw to see"),
                Component.text("what number you get.")));
        itemStack.setItemMeta(meta);
    }
}
