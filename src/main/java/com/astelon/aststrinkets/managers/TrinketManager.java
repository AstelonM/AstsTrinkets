package com.astelon.aststrinkets.managers;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.trinkets.*;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TrinketManager {

    private final AstsTrinkets plugin;
    private final MobInfoManager mobInfoManager;
    private final InvisibilityManager invisibilityManager;

    private final NamespacedKey nameKey;
    private final NamespacedKey powerKey;
    private final NamespacedKey ownerKey;
    private final NamespacedKey trapKey;
    private final NamespacedKey failureChanceKey;
    private final NamespacedKey criticalFailureChanceKey;

    private final ArrayList<Trinket> trinkets;
    private final HashMap<String, Trinket> trinketMap;

    public TrinketManager(AstsTrinkets plugin, MobInfoManager mobInfoManager, InvisibilityManager invisibilityManager) {
        this.plugin = plugin;
        this.mobInfoManager = mobInfoManager;
        this.invisibilityManager = invisibilityManager;
        nameKey = new NamespacedKey(plugin, "trinketName");
        powerKey = new NamespacedKey(plugin, "trinketPower");
        ownerKey = new NamespacedKey(plugin, "trinketOwner");
        trapKey = new NamespacedKey(plugin, "trinketTrappedEntity");
        failureChanceKey = new NamespacedKey(plugin, "failureChance");
        criticalFailureChanceKey = new NamespacedKey(plugin, "criticalFailureChance");
        trinkets = new ArrayList<>();
        trinketMap = new HashMap<>();
        initTrinkets();
    }

    private void initTrinkets() {
        addTrinket(new InvisibilityTunic(plugin, nameKey, powerKey));
        addTrinket(new TrueInvisibilityTunic(plugin, nameKey, powerKey));
        addTrinket(new FragileInvisibilityTunic(plugin, nameKey, powerKey));
        addTrinket(new ShapeShifter(plugin, nameKey, powerKey));
        addTrinket(new MysteryCake(plugin, nameKey, powerKey));
        addTrinket(new Spinneret(plugin, nameKey, powerKey));
        addTrinket(new MendingPowder(plugin, nameKey, powerKey));
        addTrinket(new BindingPowder(plugin, nameKey, powerKey, ownerKey));
        addTrinket(new TrueSightCap(plugin, nameKey, powerKey));
        addTrinket(new Homendirt(plugin, nameKey, powerKey));
        addTrinket(new Homendingdirt(plugin, nameKey, powerKey, ownerKey));
        addTrinket(new YouthMilk(plugin, nameKey, powerKey));
        addTrinket(new DiamondTrap(plugin, mobInfoManager, nameKey, powerKey, trapKey, ownerKey));
        addTrinket(new EmeraldTrap(plugin, mobInfoManager, nameKey, powerKey, trapKey, ownerKey));
        addTrinket(new AmethystTrap(plugin, mobInfoManager, nameKey, powerKey, trapKey, ownerKey));
        addTrinket(new NetherStarTrap(plugin, mobInfoManager, nameKey, powerKey, trapKey, ownerKey));
        addTrinket(new InfinityItem(plugin, nameKey, powerKey, ownerKey));
        addTrinket(new UnbindingPowder(plugin, nameKey, powerKey, ownerKey));
        addTrinket(new ReignitableRocketPrototype(plugin, nameKey, powerKey, failureChanceKey, criticalFailureChanceKey));
        addTrinket(new ReignitableRocket(plugin, nameKey, powerKey, failureChanceKey));
        addTrinket(new PerfectedReignitableRocket(plugin, nameKey, powerKey));
        addTrinket(new DeathArrow(plugin, nameKey, powerKey, ownerKey));
        addTrinket(new TrueDeathArrow(plugin, nameKey, powerKey, ownerKey));
    }

    private void addTrinket(Trinket trinket) {
        trinkets.add(trinket);
        trinketMap.put(trinket.getName(), trinket);
    }

    public ArrayList<Trinket> getTrinkets() {
        return trinkets;
    }

    @Nullable
    public Trinket getTrinket(String name) {
        for (Trinket trinket: trinkets) {
            if (trinket.getName().equalsIgnoreCase(name))
                return trinket;
        }
        return null;
    }

    @NotNull
    public Trinket getTrinketExact(String name) {
        Trinket trinket = trinketMap.get(name);
        if (trinket == null)
            throw new IllegalArgumentException("There is no trinket called " + name + ".");
        return trinket;
    }

    @Nullable
    public Trinket getTrinket(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(nameKey, PersistentDataType.STRING))
            return null;
        String name = container.get(nameKey, PersistentDataType.STRING);
        return trinketMap.get(name);
    }

    public boolean isTrinket(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(nameKey, PersistentDataType.STRING);
    }

    public boolean isTrinket(Projectile projectile) {
        if (projectile == null)
            return false;
        PersistentDataContainer container = projectile.getPersistentDataContainer();
        return container.has(nameKey, PersistentDataType.STRING);
    }

    public void removePlayerTrinkets(Player player) {
        Inventory inventory = player.getInventory();
        ArrayList<Integer> slotsToRemove = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            for (Trinket trinket: trinkets) {
                if (trinket.isTrinket(inventory.getItem(i)))
                    slotsToRemove.add(i);
            }
        }
        for (int slot: slotsToRemove)
            inventory.setItem(slot, null);
        invisibilityManager.removeInvisiblePlayer(player);
        invisibilityManager.removeTrulyInvisiblePlayer(player);
    }

    /**
     * Gets the owner of an ItemStack. A null return value means that either the itemStack is null, or it has no ItemMeta,
     * or there was no owner set in the first place. This method is meant to check the owner of a trinket, but it makes
     * no check if the given ItemStack is actually a trinket.
     * @param itemStack the ItemStack whose owner is being checked
     * @return the name of the owner, if set, or null otherwise
     */
    @Nullable
    public String getOwner(ItemStack itemStack) {
        if (itemStack == null)
            return null;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ownerKey, PersistentDataType.STRING))
            return null;
        return container.get(ownerKey, PersistentDataType.STRING);
    }

    @Nullable
    public String getOwner(Projectile projectile) {
        if (projectile == null)
            return null;
        PersistentDataContainer container = projectile.getPersistentDataContainer();
        if (!container.has(ownerKey, PersistentDataType.STRING))
            return null;
        return container.get(ownerKey, PersistentDataType.STRING);
    }

    /**
     * Checks if an ItemStack is owned by a player with the given name. An ItemStack is owned by a given player if
     * the trinketOwner field was set to the name of the player, or if the trinketOwner field was not set at all. This
     * means that an ItemStack with no set owner is effectively owned by everyone. While the method is aimed at checking
     * ownership of a trinket, no check is made to make sure that the given ItemStack is actually a trinket.
     * @param itemStack the ItemStack being checked
     * @param playerName the name of the player
     * @return true if the ItemStack is owned by a player with the given name or if the ItemStack has no owner;
     * false if an owner was set, and has a different name than the given one
     */
    public boolean isOwnedBy(ItemStack itemStack, String playerName) {
        String owner = getOwner(itemStack);
        if (owner == null)
            return true;
        return playerName.equals(owner);
    }

    public boolean isOwnedBy(Projectile projectile, String playerName) {
        String owner = getOwner(projectile);
        if (owner == null)
            return true;
        return playerName.equals(owner);
    }

    public MobInfoManager getMobInfoManager() {
        return mobInfoManager;
    }

    public FragileInvisibilityTunic getFragileInvisibilityTunic() {
        return (FragileInvisibilityTunic) getTrinketExact("fragileInvisibilityTunic");
    }

    public InvisibilityTunic getInvisibilityTunic() {
        return (InvisibilityTunic) getTrinketExact("invisibilityTunic");
    }

    public TrueInvisibilityTunic getTrueInvisibilityTunic() {
        return (TrueInvisibilityTunic) getTrinketExact("trueInvisibilityTunic");
    }

    public ShapeShifter getShapeShifter() {
        return (ShapeShifter) getTrinketExact("shapeShifter");
    }

    public MysteryCake getMysteryCake() {
        return (MysteryCake) getTrinketExact("mysteryCake");
    }

    public Spinneret getSpinneret() {
        return (Spinneret) getTrinketExact("spinneret");
    }

    public MendingPowder getMendingPowder() {
        return (MendingPowder) getTrinketExact("mendingPowder");
    }

    public BindingPowder getBindingPowder() {
        return (BindingPowder) getTrinketExact("bindingPowder");
    }

    public TrueSightCap getTrueSightCap() {
        return (TrueSightCap) getTrinketExact("trueSightCap");
    }

    public Homendirt getHomendirt() {
        return (Homendirt) getTrinketExact("homendirt");
    }

    public Homendingdirt getHomendingdirt() {
        return (Homendingdirt) getTrinketExact("homendingdirt");
    }

    public YouthMilk getYouthMilk() {
        return (YouthMilk) getTrinketExact("youthMilk");
    }

    public DiamondTrap getDiamondTrap() {
        return (DiamondTrap) getTrinketExact("diamondTrap");
    }

    public EmeraldTrap getEmeraldTrap() {
        return (EmeraldTrap) getTrinketExact("emeraldTrap");
    }

    public AmethystTrap getAmethystTrap() {
        return (AmethystTrap) getTrinketExact("amethystTrap");
    }

    public NetherStarTrap getNetherStarTrap() {
        return (NetherStarTrap) getTrinketExact("netherStarTrap");
    }

    public InfinityItem getInfinityItem() {
        return (InfinityItem) getTrinketExact("infinityItem");
    }

    public UnbindingPowder getUnbindingPowder() {
        return (UnbindingPowder) getTrinketExact("unbindingPowder");
    }

    public ReignitableRocketPrototype getReignitableRocketPrototype() {
        return (ReignitableRocketPrototype) getTrinketExact("reignitableRocketPrototype");
    }

    public ReignitableRocket getReignitableRocket() {
        return (ReignitableRocket) getTrinketExact("reignitableRocket");
    }

    public PerfectedReignitableRocket getPerfectedReignitableRocket() {
        return (PerfectedReignitableRocket) getTrinketExact("perfectedReignitableRocket");
    }

    public DeathArrow getDeathArrow() {
        return (DeathArrow) getTrinketExact("deathArrow");
    }

    public TrueDeathArrow getTrueDeathArrow() {
        return (TrueDeathArrow) getTrinketExact("trueDeathArrow");
    }
}
