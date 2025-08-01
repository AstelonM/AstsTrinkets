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
import com.astelon.aststrinkets.trinkets.projectile.arrow.DeathArrow;
import com.astelon.aststrinkets.trinkets.projectile.arrow.ExplosiveArrow;
import com.astelon.aststrinkets.trinkets.projectile.arrow.SmitingArrow;
import com.astelon.aststrinkets.trinkets.projectile.arrow.TrueDeathArrow;
import com.astelon.aststrinkets.trinkets.projectile.rocket.*;
import com.astelon.aststrinkets.utils.NamespacedKeys;
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
import java.util.stream.Collectors;

import static com.astelon.aststrinkets.utils.Utils.isNothing;

public class TrinketManager {

    private final AstsTrinkets plugin;
    private final MobInfoManager mobInfoManager;
    private final InvisibilityManager invisibilityManager;
    private final NamespacedKeys keys;

    private final ArrayList<Trinket> trinkets;
    private final HashMap<String, Trinket> trinketMap;

    public TrinketManager(AstsTrinkets plugin, MobInfoManager mobInfoManager, InvisibilityManager invisibilityManager, NamespacedKeys keys) {
        this.plugin = plugin;
        this.mobInfoManager = mobInfoManager;
        this.invisibilityManager = invisibilityManager;
        this.keys = keys;
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
        addTrinket(new TimeMachinePrototype(plugin, keys));
        addTrinket(new HoldingBundle(plugin, keys));
        addTrinket(new TrinketImmunitySponge(plugin, keys));
        addTrinket(new TrinketVulnerabilitySponge(plugin, keys));
        addTrinket(new ArcaneTome(plugin, keys));
        addTrinket(new PlayerMagnet(plugin, keys));
        addTrinket(new AdamantineStrand(plugin, keys));
        addTrinket(new HealingHerb(plugin, keys));
        addTrinket(new InvincibilityBelt(plugin, keys));
        addTrinket(new VampiricSword(plugin, keys));
        addTrinket(new MysteryFirework(plugin, keys));
        addTrinket(new UnbreakableTurtleShell(plugin, keys));
        addTrinket(new NightVisionGoggles(plugin, keys));
        addTrinket(new CloudSeeder(plugin, keys));
        addTrinket(new Die(plugin, keys));
        addTrinket(new MysteryShell(plugin, keys));
        addTrinket(new AbyssShell(plugin, keys));
        addTrinket(new SurfaceCure(plugin, keys));
        addTrinket(new InvisibilityPowder(plugin, keys));
        addTrinket(new SpoiledEgg(plugin, keys));
        addTrinket(new CopperOxidationSolution(plugin, keys));
        addTrinket(new Flippers(plugin, keys));
        addTrinket(new CuringApple(plugin, keys));
        addTrinket(new SnowGolemBlueprint(plugin, keys));
        addTrinket(new SnowGolemKit(plugin, keys));
        addTrinket(new TaintedLifeWater(plugin, keys));
        addTrinket(new SpoiledYouthMilk(plugin, keys));
        addTrinket(new EternalYouthCookie(plugin, keys));
        addTrinket(new InfestedWheat(plugin, keys));
        addTrinket(new CloudElectrifier(plugin, keys));
        addTrinket(new HuntingBow(plugin, keys));
        addTrinket(new AdvancedFlippers(plugin, keys));
        addTrinket(new RudimentaryRockCrusher(plugin, keys));
        addTrinket(new Treats(plugin, keys));
        addTrinket(new SunTotem(plugin, keys));
        addTrinket(new MagicBerries(plugin, keys));
        addTrinket(new ForbiddenTome(plugin, keys));
        addTrinket(new IronGolemBlueprint(plugin, keys));
        addTrinket(new IronGolemKit(plugin, keys));
        addTrinket(new Lasso(plugin, keys));
        addTrinket(new VoidGateway(plugin, keys));
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

    @Nullable
    public Trinket getTrinket(Projectile projectile) {
        if (projectile == null)
            return null;
        PersistentDataContainer container = projectile.getPersistentDataContainer();
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

    public boolean isOwnedBy(ItemStack itemStack, Player player) {
        return isOwnedBy(itemStack, player.getName());
    }

    public boolean isOwnedBy(Projectile projectile, String playerName) {
        String owner = getOwner(projectile);
        if (owner == null)
            return true;
        return playerName.equals(owner);
    }

    public boolean isOwnedBy(Projectile projectile, Player player) {
        return isOwnedBy(projectile, player.getName());
    }

    public boolean isStrictlyOwnedBy(ItemStack itemStack, String playerName) {
        String owner = getOwner(itemStack);
        if (owner == null)
            return false;
        return playerName.equals(owner);
    }

    public boolean isStrictlyOwnedBy(ItemStack itemStack, Player player) {
        return isStrictlyOwnedBy(itemStack, player.getName());
    }

    public boolean isStrictlyOwnedBy(Projectile projectile, String playerName) {
        String owner = getOwner(projectile);
        if (owner == null)
            return false;
        return playerName.equals(owner);
    }

    public boolean isStrictlyOwnedBy(Projectile projectile, Player player) {
        return isStrictlyOwnedBy(projectile, player.getName());
    }

    public boolean isOwnedWithRestrictions(ItemStack itemStack, Entity entity) {
        String owner = getOwner(itemStack);
        if (owner != null) {
            if (entity instanceof Player player && !isOwnedBy(itemStack, player))
                return false;
            else
                return entity instanceof Player;
        }
        return true;
    }

    public boolean isOwnedWithRestrictions(Arrow arrow, Entity entity) {
        String owner = getOwner(arrow);
        if (owner != null) {
            if (entity instanceof Player player && !isOwnedBy(arrow, player))
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

    public void removeTrinketImmunity(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.remove(keys.trinketImmuneKey);
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

    public void removeTrinketImmunity(Entity entity) {
        PersistentDataContainer container = entity.getPersistentDataContainer();
        container.remove(keys.trinketImmuneKey);
    }

    public boolean shouldShowHelp(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return false;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return !container.has(keys.hideHelpKey, PersistentDataType.BYTE);
    }

    public HashMap<String, Object> getPresentKeys(ItemStack itemStack) {
        if (isNothing(itemStack))
            throw new IllegalArgumentException("ItemStack cannot be null or air.");
        ItemMeta meta = itemStack.getItemMeta();
        HashMap<String, Object> result = new HashMap<>();
        if (meta == null)
            return result;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        for (NamespacedKeys.KeyTypePair keyTypePair: keys.getKeys()) {
            if (container.has(keyTypePair.key(), keyTypePair.type())) {
                result.put(keyTypePair.key().getKey(), container.get(keyTypePair.key(), keyTypePair.type()));
            }
        }
        return result;
    }

    public NamespacedKeys getNamespacedKeys() {
        return keys;
    }

    public List<NamespacedKey> getKeys() {
        return keys.getKeys().stream().map(NamespacedKeys.KeyTypePair::key).collect(Collectors.toList());
    }

    public NamespacedKeys.KeyTypePair getKeyTypePair(String keyName) {
        return keys.getKeyTypePair(keyName);
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

    public TimeMachinePrototype getTimeMachinePrototype() {
        return (TimeMachinePrototype) getTrinketExact("timeMachinePrototype");
    }

    public HoldingBundle getHoldingBundle() {
        return (HoldingBundle) getTrinketExact("holdingBundle");
    }

    public TrinketImmunitySponge getTrinketImmunitySponge() {
        return (TrinketImmunitySponge) getTrinketExact("trinketImmunitySponge");
    }

    public TrinketVulnerabilitySponge getTrinketVulnerabilitySponge() {
        return (TrinketVulnerabilitySponge) getTrinketExact("trinketVulnerabilitySponge");
    }

    public ArcaneTome getArcaneTome() {
        return (ArcaneTome) getTrinketExact("arcaneTome");
    }

    public PlayerMagnet getPlayerMagnet() {
        return (PlayerMagnet) getTrinketExact("playerMagnet");
    }

    public AdamantineStrand getAdamantineStrand() {
        return (AdamantineStrand) getTrinketExact("adamantineStrand");
    }

    public HealingHerb getHealingHerb() {
        return (HealingHerb) getTrinketExact("healingHerb");
    }

    public InvincibilityBelt getInvincibilityBelt() {
        return (InvincibilityBelt) getTrinketExact("invincibilityBelt");
    }

    public VampiricSword getVampiricSword() {
        return (VampiricSword) getTrinketExact("vampiricSword");
    }

    public MysteryFirework getMysteryFirework() {
        return (MysteryFirework) getTrinketExact("mysteryFirework");
    }

    public UnbreakableTurtleShell getUnbreakableTurtleShell() {
        return (UnbreakableTurtleShell) getTrinketExact("unbreakableTurtleShell");
    }

    public NightVisionGoggles getNightVisionGoggles() {
        return (NightVisionGoggles) getTrinketExact("nightVisionGoggles");
    }

    public CloudSeeder getCloudSeeder() {
        return (CloudSeeder) getTrinketExact("cloudSeeder");
    }

    public Die getDie() {
        return (Die) getTrinketExact("die");
    }

    public MysteryShell getMysteryShell() {
        return (MysteryShell) getTrinketExact("mysteryShell");
    }

    public AbyssShell getAbyssShell() {
        return (AbyssShell) getTrinketExact("abyssShell");
    }

    public SurfaceCure getSurfaceCure() {
        return (SurfaceCure) getTrinketExact("surfaceCure");
    }

    public InvisibilityPowder getInvisibilityPowder() {
        return (InvisibilityPowder) getTrinketExact("invisibilityPowder");
    }

    public SpoiledEgg getSpoiledEgg() {
        return (SpoiledEgg) getTrinketExact("spoiledEgg");
    }

    public CopperOxidationSolution getCopperOxidationSolution() {
        return (CopperOxidationSolution) getTrinketExact("copperOxidationSolution");
    }

    public Flippers getFlippers() {
        return (Flippers) getTrinketExact("flippers");
    }

    public CuringApple getCuringApple() {
        return (CuringApple) getTrinketExact("curingApple");
    }

    public SnowGolemBlueprint getSnowGolemBlueprint() {
        return (SnowGolemBlueprint) getTrinketExact("snowGolemBlueprint");
    }

    public SnowGolemKit getSnowGolemKit() {
        return (SnowGolemKit) getTrinketExact("snowGolemKit");
    }

    public TaintedLifeWater getTaintedLifeWater() {
        return (TaintedLifeWater) getTrinketExact("taintedLifeWater");
    }

    public SpoiledYouthMilk getSpoiledYouthMilk() {
        return (SpoiledYouthMilk) getTrinketExact("spoiledYouthMilk");
    }

    public EternalYouthCookie getEternalYouthCookie() {
        return (EternalYouthCookie) getTrinketExact("eternalYouthCookie");
    }

    public InfestedWheat getInfestedWheat() {
        return (InfestedWheat) getTrinketExact("infestedWheat");
    }

    public CloudElectrifier getCloudElectrifier() {
        return (CloudElectrifier) getTrinketExact("cloudElectrifier");
    }

    public HuntingBow getHuntingBow() {
        return (HuntingBow) getTrinketExact("huntingBow");
    }

    public AdvancedFlippers getAdvancedFlippers() {
        return (AdvancedFlippers) getTrinketExact("advancedFlippers");
    }

    public RudimentaryRockCrusher getRockCrusher() {
        return (RudimentaryRockCrusher) getTrinketExact("rudimentaryRockCrusher");
    }

    public Treats getTreats() {
        return (Treats) getTrinketExact("treats");
    }

    public SunTotem getSunTotem() {
        return (SunTotem) getTrinketExact("sunTotem");
    }

    public MagicBerries getMagicBerries() {
        return (MagicBerries) getTrinketExact("magicBerries");
    }

    public ForbiddenTome getForbiddenTome() {
        return (ForbiddenTome) getTrinketExact("forbiddenTome");
    }

    public IronGolemBlueprint getIronGolemBlueprint() {
        return (IronGolemBlueprint) getTrinketExact("ironGolemBlueprint");
    }

    public IronGolemKit getIronGolemKit() {
        return (IronGolemKit) getTrinketExact("ironGolemKit");
    }

    public Lasso getLasso() {
        return (Lasso)  getTrinketExact("lasso");
    }

    public VoidGateway getVoidGateway() {
        return (VoidGateway) getTrinketExact("voidGateway");
    }
}
