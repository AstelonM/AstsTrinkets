package com.astelon.aststrinkets.managers;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.trinkets.*;
import com.astelon.aststrinkets.trinkets.block.*;
import com.astelon.aststrinkets.trinkets.creature.*;
import com.astelon.aststrinkets.trinkets.creature.traps.AmethystTrap;
import com.astelon.aststrinkets.trinkets.creature.traps.DiamondTrap;
import com.astelon.aststrinkets.trinkets.creature.traps.EmeraldTrap;
import com.astelon.aststrinkets.trinkets.creature.traps.NetherStarTrap;
import com.astelon.aststrinkets.trinkets.equipable.*;
import com.astelon.aststrinkets.trinkets.inventory.*;
import com.astelon.aststrinkets.trinkets.projectile.*;
import com.astelon.aststrinkets.trinkets.rocket.PerfectedReignitableRocket;
import com.astelon.aststrinkets.trinkets.rocket.ReignitableRocket;
import com.astelon.aststrinkets.trinkets.rocket.ReignitableRocketPrototype;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFactory;
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
    private final NamespacedKeys keys;

    private final ArrayList<Trinket> trinkets;
    private final HashMap<String, Trinket> trinketMap;

    public TrinketManager(AstsTrinkets plugin, MobInfoManager mobInfoManager, InvisibilityManager invisibilityManager) {
        this.plugin = plugin;
        this.mobInfoManager = mobInfoManager;
        this.invisibilityManager = invisibilityManager;
        this.keys = new NamespacedKeys(plugin);
        trinkets = new ArrayList<>();
        trinketMap = new HashMap<>();
        initTrinkets();
    }

    private void initTrinkets() {
        addTrinket(new InvisibilityTunic(plugin, keys));
        addTrinket(new TrueInvisibilityTunic(plugin, keys));
        addTrinket(new FragileInvisibilityTunic(plugin, keys));
        addTrinket(new ShapeShifter(plugin, keys));
        addTrinket(new MysteryCake(plugin, keys));
        addTrinket(new Spinneret(plugin, keys));
        addTrinket(new MendingPowder(plugin, keys));
        addTrinket(new BindingPowder(plugin, keys));
        addTrinket(new TrueSightCap(plugin, keys));
        addTrinket(new Homendirt(plugin, keys));
        addTrinket(new Homendingdirt(plugin, keys));
        addTrinket(new YouthMilk(plugin, keys));
        addTrinket(new DiamondTrap(plugin, mobInfoManager, keys));
        addTrinket(new EmeraldTrap(plugin, mobInfoManager, keys));
        addTrinket(new AmethystTrap(plugin, mobInfoManager, keys));
        addTrinket(new NetherStarTrap(plugin, mobInfoManager, keys));
        addTrinket(new InfinityItem(plugin, keys));
        addTrinket(new UnbindingPowder(plugin, keys));
        addTrinket(new ReignitableRocketPrototype(plugin, keys));
        addTrinket(new ReignitableRocket(plugin, keys));
        addTrinket(new PerfectedReignitableRocket(plugin, keys));
        addTrinket(new DeathArrow(plugin, keys));
        addTrinket(new TrueDeathArrow(plugin, keys));
        addTrinket(new ShulkerBoxContainmentUnit(plugin, keys));
        addTrinket(new DivingHelmet(plugin, keys));
        addTrinket(new HydraulicBoots(plugin, keys));
        addTrinket(new LifeWater(plugin, keys));
        addTrinket(new Souleater(plugin, keys));
        addTrinket(new MysteryEgg(plugin, keys));
        addTrinket(new Bait(plugin, keys));
        addTrinket(new ExperienceBottle(plugin, keys));
        addTrinket(new SentientAxe(plugin, keys));
        addTrinket(new Spellbook(plugin, keys));
        addTrinket(new GatewayAnchor(plugin, keys));
        addTrinket(new BuddingSolution(plugin, keys));
        addTrinket(new ItemMagnet(plugin, keys));
        addTrinket(new Terrarium(plugin, keys, mobInfoManager));
        addTrinket(new SmitingArrow(plugin, keys));
        addTrinket(new ExplosiveArrow(plugin, keys));
        addTrinket(new FireproofVest(plugin, keys));
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
        if (!container.has(keys.nameKey, PersistentDataType.STRING))
            return null;
        String name = container.get(keys.nameKey, PersistentDataType.STRING);
        return trinketMap.get(name);
    }

    public boolean isTrinket(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(keys.nameKey, PersistentDataType.STRING);
    }

    public boolean isTrinket(Projectile projectile) {
        if (projectile == null)
            return false;
        PersistentDataContainer container = projectile.getPersistentDataContainer();
        return container.has(keys.nameKey, PersistentDataType.STRING);
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
        if (!container.has(keys.ownerKey, PersistentDataType.STRING))
            return null;
        return container.get(keys.ownerKey, PersistentDataType.STRING);
    }

    @Nullable
    public String getOwner(Projectile projectile) {
        if (projectile == null)
            return null;
        PersistentDataContainer container = projectile.getPersistentDataContainer();
        if (!container.has(keys.ownerKey, PersistentDataType.STRING))
            return null;
        return container.get(keys.ownerKey, PersistentDataType.STRING);
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

    public boolean isStrictlyOwnedBy(ItemStack itemStack, String playerName) {
        String owner = getOwner(itemStack);
        if (owner == null)
            return false;
        return playerName.equals(owner);
    }

    public boolean isStrictlyOwnedBy(Projectile projectile, String playerName) {
        String owner = getOwner(projectile);
        if (owner == null)
            return false;
        return playerName.equals(owner);
    }

    public boolean isOwnedWithRestrictions(ItemStack itemStack, Entity entity) {
        String owner = getOwner(itemStack);
        if (owner != null) {
            if (entity instanceof Player player && !isOwnedBy(itemStack, player.getName()))
                return false;
            else
                return entity instanceof Player;
        }
        return true;
    }

    public boolean isOwnedWithRestrictions(Arrow arrow, Entity entity) {
        String owner = getOwner(arrow);
        if (owner != null) {
            if (entity instanceof Player player && !isOwnedBy(arrow, player.getName()))
                return false;
            else
                return entity instanceof Player;
        }
        return true;
    }

    public boolean isTrinketImmune(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(keys.trinketImmuneKey, PersistentDataType.BYTE);
    }

    public void makeTrinketImmune(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            ItemFactory factory = plugin.getServer().getItemFactory();
            meta = factory.getItemMeta(itemStack.getType());
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.trinketImmuneKey, PersistentDataType.BYTE, (byte) 1);
        itemStack.setItemMeta(meta);
    }

    public boolean isTrinketImmune(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        return container.has(keys.trinketImmuneKey, PersistentDataType.BYTE);
    }

    public void makeTrinketImmune(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.set(keys.trinketImmuneKey, PersistentDataType.BYTE, (byte) 1);
    }

    public MobInfoManager getMobInfoManager() {
        return mobInfoManager;
    }

    public HashMap<String, Object> getPresentKeys(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR)
            throw new IllegalArgumentException("ItemStack cannot be null or air.");
        ItemMeta meta = itemStack.getItemMeta();
        HashMap<String, Object> result = new HashMap<>();
        if (meta == null)
            return result;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        for (Map.Entry<NamespacedKey, PersistentDataType<?, ?>> entry: keys.getKeyMap().entrySet()) {
            if (container.has(entry.getKey(), entry.getValue())) {
                result.put(entry.getKey().getKey(), container.get(entry.getKey(), entry.getValue()));
            }
        }
        return result;
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

    public ShulkerBoxContainmentUnit getShulkerBoxContainmentUnit() {
        return (ShulkerBoxContainmentUnit) getTrinketExact("shulkerBoxContainmentUnit");
    }

    public DivingHelmet getDivingHelmet() {
        return (DivingHelmet) getTrinketExact("divingHelmet");
    }

    public HydraulicBoots getHydraulicBoots() {
        return (HydraulicBoots) getTrinketExact("hydraulicBoots");
    }

    public LifeWater getLifeWater() {
        return (LifeWater) getTrinketExact("lifeWater");
    }

    public Souleater getSouleater() {
        return (Souleater) getTrinketExact("souleater");
    }

    public MysteryEgg getMysteryEgg() {
        return (MysteryEgg) getTrinketExact("mysteryEgg");
    }

    public Bait getBait() {
        return (Bait) getTrinketExact("bait");
    }

    public ExperienceBottle getExperienceBottle() {
        return (ExperienceBottle) getTrinketExact("experienceBottle");
    }

    public SentientAxe getSentientAxe() {
        return (SentientAxe) getTrinketExact("sentientAxe");
    }

    public Spellbook getSpellbook() {
        return (Spellbook) getTrinketExact("spellbook");
    }

    public GatewayAnchor getGatewayAnchor() {
        return (GatewayAnchor) getTrinketExact("gatewayAnchor");
    }

    public BuddingSolution getBuddingSolution() {
        return (BuddingSolution) getTrinketExact("buddingSolution");
    }

    public ItemMagnet getItemMagnet() {
        return (ItemMagnet) getTrinketExact("itemMagnet");
    }

    public Terrarium getTerrarium() {
        return (Terrarium) getTrinketExact("terrarium");
    }

    public SmitingArrow getSmitingArrow() {
        return (SmitingArrow) getTrinketExact("smitingArrow");
    }

    public ExplosiveArrow getExplosiveArrow() {
        return (ExplosiveArrow) getTrinketExact("explosiveArrow");
    }

    public FireproofVest getFireproofVest() {
        return (FireproofVest) getTrinketExact("fireproofVest");
    }
}
