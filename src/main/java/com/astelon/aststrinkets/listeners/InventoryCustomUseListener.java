package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.trinkets.block.InfinityItem;
import com.astelon.aststrinkets.trinkets.inventory.*;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

import static com.astelon.aststrinkets.utils.Utils.*;

public class InventoryCustomUseListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final MendingPowder mendingPowder;
    private final BindingPowder bindingPowder;
    private final UnbindingPowder unbindingPowder;
    private final Homendirt homendirt;
    private final Homendingdirt homendingdirt;
    private final InfinityItem infinityItem;
    private final ShulkerBoxContainmentUnit shulkerBoxContainmentUnit;
    private final BuddingSolution buddingSolution;
    private final TrinketImmunitySponge trinketImmunitySponge;
    private final TrinketVulnerabilitySponge trinketVulnerabilitySponge;
    private final ArcaneTome arcaneTome;
    private final AdamantineStrand adamantineStrand;
    private final HoldingBundle holdingBundle;
    private final ItemMagnet itemMagnet;
    private final CopperOxidationSolution copperOxidationSolution;

    public InventoryCustomUseListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        mendingPowder = trinketManager.getMendingPowder();
        bindingPowder = trinketManager.getBindingPowder();
        unbindingPowder = trinketManager.getUnbindingPowder();
        homendirt = trinketManager.getHomendirt();
        homendingdirt = trinketManager.getHomendingdirt();
        infinityItem = trinketManager.getInfinityItem();
        shulkerBoxContainmentUnit = trinketManager.getShulkerBoxContainmentUnit();
        buddingSolution = trinketManager.getBuddingSolution();
        trinketImmunitySponge = trinketManager.getTrinketImmunitySponge();
        trinketVulnerabilitySponge = trinketManager.getTrinketVulnerabilitySponge();
        arcaneTome = trinketManager.getArcaneTome();
        adamantineStrand = trinketManager.getAdamantineStrand();
        holdingBundle = trinketManager.getHoldingBundle();
        itemMagnet = trinketManager.getItemMagnet();
        copperOxidationSolution = trinketManager.getCopperOxidationSolution();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack heldItem = event.getCursor();
        Player player = (Player) event.getView().getPlayer();
        if (isNothing(heldItem))
            return;
        ItemStack clickedItem = event.getCurrentItem();
        switch (event.getClick()) {
            case DOUBLE_CLICK -> {
                if (!isNothing(clickedItem))
                    return;
                if (holdingBundle.isEnabledTrinket(heldItem) && trinketManager.isOwnedBy(heldItem, player)) {
                    ItemStack contained = holdingBundle.getItem(heldItem);
                    if (contained != null) {
                        InventoryView view = event.getView();
                        int amount = 0;
                        for (ItemStack itemStack : view.getTopInventory()) {
                            if (contained.isSimilar(itemStack)) {
                                amount += itemStack.getAmount();
                                itemStack.setAmount(0);
                            }
                        }
                        for (ItemStack itemStack : view.getBottomInventory()) {
                            if (contained.isSimilar(itemStack)) {
                                amount += itemStack.getAmount();
                                itemStack.setAmount(0);
                            }
                        }
                        if (amount == 0) {
                            Inventory inventory = event.getClickedInventory();
                            if (inventory != null && holdingBundle.hasExtraItems(heldItem)) {
                                ItemStack[] contents = inventory.getStorageContents();
                                int emptySlots = 0;
                                int stackSize = contained.getMaxStackSize();
                                for (ItemStack itemStack : contents) {
                                    if (itemStack == null)
                                        emptySlots += stackSize;
                                    else if (itemStack.isSimilar(contained))
                                        emptySlots += stackSize - itemStack.getAmount();
                                }
                                int extraItems = holdingBundle.getExtraItemsAmount(heldItem);
                                int amountToAdd = Math.min(emptySlots, extraItems);
                                List<ItemStack> itemsToAdd = holdingBundle.getExtraItemsAsStacks(heldItem, amountToAdd);
                                HashMap<Integer, ItemStack> remaining = inventory.addItem(itemsToAdd.toArray(new ItemStack[0]));
                                // Just in case somehow the items don't fit
                                if (!remaining.isEmpty()) {
                                    World world = player.getWorld();
                                    Location location = player.getLocation();
                                    for (ItemStack itemStack : remaining.values()) {
                                        world.dropItem(location, itemStack);
                                    }
                                }
                                ItemStack bundleResult = holdingBundle.removeExtraItemsAmount(heldItem, amountToAdd);
                                Utils.transformCursorItem(heldItem, bundleResult, inventory, player);
                                player.updateInventory();
                            }

                        } else {
                            ItemStack result = holdingBundle.addExtraAmountInBundle(heldItem, amount);
                            Utils.transformCursorItem(heldItem, result, player.getInventory(), player);
                            player.updateInventory();
                        }
                    }
                }
            }
            case SHIFT_RIGHT -> {
                if (trinketManager.isOwnedBy(heldItem, player.getName())) {
                    if (mendingPowder.isEnabledTrinket(heldItem)) {
                        if (isNothing(clickedItem))
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                            return;
                        }
                        ItemMeta meta = clickedItem.getItemMeta();
                        if (!(meta instanceof Damageable damageable))
                            return;
                        int maxDurability = clickedItem.getType().getMaxDurability();
                        int damage = damageable.getDamage();
                        // Just in case there's some weird values
                        if (damage != 0 && maxDurability > 0) {
                            event.setCancelled(true);
                            damageable.setDamage(0);
                            clickedItem.setItemMeta(meta);
                            heldItem.subtract();
                            player.updateInventory();
                        }
                    } else if (bindingPowder.isEnabledTrinket(heldItem)) {
                        if (isNothing(clickedItem))
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                            return;
                        }
                        Trinket trinket = trinketManager.getTrinket(clickedItem);
                        if (trinket != null) {
                            event.setCancelled(true);
                            ItemStack result = bindingPowder.bindTrinket(clickedItem, trinket, player);
                            if (result != null) {
                                heldItem.subtract();
                                transformItem(clickedItem, result, event.getSlot(), event.getClickedInventory(), player);
                                player.sendMessage(Component.text("This trinket is bound to you now.", NamedTextColor.GOLD));
                                player.updateInventory();
                            } else {
                                player.sendMessage(Component.text("Could not bind this trinket to you.", NamedTextColor.RED));
                            }
                        }
                    } else if (unbindingPowder.isEnabledTrinket(heldItem)) {
                        if (isNothing(clickedItem))
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                            return;
                        }
                        Trinket trinket = trinketManager.getTrinket(clickedItem);
                        if (trinket != null) {
                            event.setCancelled(true);
                            ItemStack result = unbindingPowder.unbindTrinket(clickedItem, trinket);
                            if (result != null) {
                                heldItem.subtract();
                                transformItem(clickedItem, result, event.getSlot(), event.getClickedInventory(), player);
                                player.sendMessage(Component.text("This trinket is now unbound.", NamedTextColor.GOLD));
                                player.updateInventory();
                            } else {
                                player.sendMessage(Component.text("Could not unbind this trinket.", NamedTextColor.RED));
                            }
                        }
                    } else if (homendirt.isEnabledTrinket(heldItem)) {
                        if (isNothing(clickedItem))
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                            return;
                        }
                        int slot = event.getSlot();
                        Inventory inventory = event.getClickedInventory();
                        if (homendingdirt.isTrinket(clickedItem)) {
                            event.setCancelled(true);
                            int mending = clickedItem.getEnchantmentLevel(Enchantment.MENDING);
                            clickedItem.subtract();
                            ItemStack newHomendingdirt = homendingdirt.createHomendingdirt(heldItem, mending);
                            transformCursorItem(heldItem, newHomendingdirt, inventory, player);
                            player.sendMessage(Component.text("The Homendingdirt is consumed.", NamedTextColor.GOLD));
                            player.updateInventory();
                            return;
                        }
                        int mending;
                        if (clickedItem.getType() == Material.ENCHANTED_BOOK) {
                            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) clickedItem.getItemMeta();
                            mending = meta.getStoredEnchantLevel(Enchantment.MENDING);
                        } else {
                            mending = clickedItem.getEnchantmentLevel(Enchantment.MENDING);
                        }
                        if (mending == 0)
                            return;
                        event.setCancelled(true);
                        ItemStack result = homendirt.removeMending(clickedItem);
                        transformItem(clickedItem, result, slot, inventory, player);
                        ItemStack homendingdirtItem = homendingdirt.createHomendingdirt(heldItem, mending);
                        transformCursorItem(heldItem, homendingdirtItem, inventory, player);
                        player.updateInventory();
                    } else if (homendingdirt.isEnabledTrinket(heldItem)) {
                        if (isNothing(clickedItem))
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                            return;
                        }
                        int slot = event.getSlot();
                        Inventory inventory = event.getClickedInventory();
                        if (homendirt.isTrinket(clickedItem)) {
                            event.setCancelled(true);
                            int mending = heldItem.getEnchantmentLevel(Enchantment.MENDING);
                            heldItem.subtract();
                            ItemStack newHomendingdirt = homendingdirt.createHomendingdirt(clickedItem, mending);
                            transformItem(clickedItem, newHomendingdirt, slot, inventory, player);
                            player.updateInventory();
                            player.sendMessage(Component.text("The Homendirt becomes a Homendingdirt.", NamedTextColor.GOLD));
                            return;
                        }
                        int mending = clickedItem.getEnchantmentLevel(Enchantment.MENDING);
                        if (mending != 0)
                            return;
                        event.setCancelled(true);
                        ItemStack result = homendingdirt.addMending(clickedItem, heldItem);
                        transformItem(clickedItem, result, slot, inventory, player);
                        heldItem.subtract();
                        player.updateInventory();
                    } else if (infinityItem.isEnabledTrinket(heldItem)) {
                        if (infinityItem.hasBlock(heldItem))
                            return;
                        if (isNothing(clickedItem))
                            return;
                        Material block = clickedItem.getType();
                        if (!infinityItem.isAllowedBlock(block, player)) {
                            player.sendMessage(Component.text("This block cannot be replicated.", NamedTextColor.RED));
                            return;
                        }
                        event.setCancelled(true);
                        ItemStack result = infinityItem.replicateBlock(heldItem, block);
                        transformCursorItem(heldItem, result, player.getInventory(), player);
                        player.updateInventory();
                    } else if (shulkerBoxContainmentUnit.isEnabledTrinket(heldItem)) {
                        if (shulkerBoxContainmentUnit.hasShulkerBox(heldItem))
                            return;
                        if (!Utils.isShulkerBox(clickedItem))
                            return;
                        //TODO trinket immune check?
                        if (!shulkerBoxContainmentUnit.canContainShulkerBox(clickedItem)) {
                            player.sendMessage(Component.text("You can't contain this shulker. Make sure it doesn't have " +
                                    "another containment unit inside.", NamedTextColor.RED));
                            return;
                        }
                        ItemStack result = shulkerBoxContainmentUnit.setContainedShulkerBox(heldItem, clickedItem);
                        if (result != null) {
                            event.setCancelled(true);
                            transformCursorItem(heldItem, result, player.getInventory(), player);
                            clickedItem.subtract();
                            player.updateInventory();
                        }
                    } else if (buddingSolution.isEnabledTrinket(heldItem)) {
                        if (clickedItem == null || clickedItem.getType() != Material.AMETHYST_BLOCK)
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                            return;
                        }
                        heldItem.subtract();
                        transformItem(clickedItem, new ItemStack(Material.BUDDING_AMETHYST), event.getSlot(), player.getInventory(), player);
                        player.updateInventory();
                        event.setCancelled(true);
                    } else if (trinketImmunitySponge.isEnabledTrinket(heldItem)) {
                        if (isNothing(clickedItem))
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("This item stack is already trinket immune.", NamedTextColor.RED));
                            return;
                        }
                        event.setCancelled(true);
                        trinketManager.makeTrinketImmune(clickedItem);
                        player.sendMessage(Component.text("The item stack is now trinket immune."));
                    } else if (trinketVulnerabilitySponge.isEnabledTrinket(heldItem)) {
                        if (isNothing(clickedItem))
                            return;
                        if (!trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("This item stack is not trinket immune.", NamedTextColor.RED));
                            return;
                        }
                        event.setCancelled(true);
                        trinketManager.removeTrinketImmunity(clickedItem);
                        player.sendMessage(Component.text("The item stack is no longer trinket immune."));
                    } else if (arcaneTome.isEnabledTrinket(heldItem)) {
                        if (isNothing(clickedItem))
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                            return;
                        }
                        if (clickedItem.getType() == Material.ENCHANTED_BOOK) {
                            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) clickedItem.getItemMeta();
                            if (meta == null || !meta.hasStoredEnchants() && clickedItem.getEnchantments().isEmpty()) {
                                player.sendMessage(Component.text("This item has no enchantments.", NamedTextColor.RED));
                                return;
                            }
                        } else if (clickedItem.getEnchantments().isEmpty()) {
                            player.sendMessage(Component.text("This item has no enchantments.", NamedTextColor.RED));
                            return;
                        }
                        event.setCancelled(true);
                        ItemStack result = arcaneTome.improveEnchantment(clickedItem);
                        int slot = event.getSlot();
                        Inventory inventory = event.getClickedInventory();
                        transformItem(clickedItem, result, slot, inventory, player);
                        heldItem.subtract();
                        player.updateInventory();
                    } else if (adamantineStrand.isEnabledTrinket(heldItem)) {
                        if (isNothing(clickedItem))
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                            return;
                        }
                        ItemMeta meta = clickedItem.getItemMeta();
                        if (meta != null && meta.isUnbreakable())
                            return;
                        EquipmentSlot equipmentSlot = clickedItem.getType().getEquipmentSlot();
                        ItemStack result = switch (equipmentSlot) {
                            case FEET, LEGS, CHEST, HEAD -> adamantineStrand.makeUnbreakable(clickedItem);
                            default -> null;
                        };
                        if (result != null) {
                            Utils.transformItem(clickedItem, result, event.getSlot(), event.getClickedInventory(), player);
                            heldItem.subtract();
                            player.updateInventory();
                        }
                    } else if (itemMagnet.isEnabledTrinket(heldItem)) {
                        if (isNothing(clickedItem))
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                            return;
                        }
                        if (holdingBundle.isEnabledTrinket(clickedItem)) {
                            if (holdingBundle.hasMagnet(clickedItem))
                                return;
                            ItemStack result = holdingBundle.addMagnet(clickedItem, itemMagnet.getRange(heldItem));
                            if (result != null) {
                                heldItem.subtract();
                                transformItem(clickedItem, result, event.getSlot(), event.getClickedInventory(), player);
                                player.sendMessage(Component.text("You added a magnet to this Bundle of Holding.",
                                        NamedTextColor.GOLD));
                                player.updateInventory();
                            } else {
                                player.sendMessage(Component.text("Could not add a magnet to the bundle.",
                                        NamedTextColor.RED));
                            }
                        }
                    } else if (copperOxidationSolution.isEnabledTrinket(heldItem)) {
                        if (clickedItem == null)
                            return;
                        if (trinketManager.isTrinketImmune(clickedItem)) {
                            player.sendMessage(Component.text("Trinkets cannot be used on this item.", NamedTextColor.RED));
                            return;
                        }
                        ItemStack result = copperOxidationSolution.oxidize(clickedItem);
                        if (result == null) {
                            player.sendMessage(Component.text("This block cannot be oxidized with this solution.", NamedTextColor.RED));
                            return;
                        }
                        heldItem.subtract();
                        player.updateInventory();
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
