package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SentientAxe extends Trinket {

    private static final UUID ATTACK_DAMAGE = UUID.nameUUIDFromBytes("sentientAxeAttackDamage".getBytes());
    private static final UUID ATTACK_SPEED = UUID.nameUUIDFromBytes("sentientAxeAttackSpeed".getBytes());

    private final long chopMessageCooldown = 5000;
    private final long delayBeforeComplaining = 60000;
    private final int minComplainingDelay = 10 * 20;
    private final int maxComplainingDelay = 20 * 20;

    private final Random random;
    private String[] names;

    public SentientAxe(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "sentientAxe", Power.SENTIENT_AXE, true, Usages.CUT_TREES);
        random = new Random();
        initNames();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.IRON_AXE);
        Repairable meta = (Repairable) itemStack.getItemMeta();
        meta.setUnbreakable(true);
        meta.setRepairCost(100);
        AttributeModifier damageModifier = new AttributeModifier(ATTACK_DAMAGE, "halvedDamage", 3.5,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);
        //TODO iron axe attack speed is 0.9000000953674316, this gives 0.8999999999999999, should I fix?
        AttributeModifier attackSpeedModifier = new AttributeModifier(ATTACK_SPEED, "fixedAttackSpeed", -3.1,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifier);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.displayName(Component.text("Iron Axe", NamedTextColor.GOLD));
        meta.lore(List.of(Component.text("What a lovely axe.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public String getCustomId(ItemStack axe) {
        PersistentDataContainer container = axe.getItemMeta().getPersistentDataContainer();
        return container.get(keys.customIdKey, PersistentDataType.STRING);
    }

    public boolean isTheSameAxe(ItemStack axe, String axeId) {
        PersistentDataContainer container = axe.getItemMeta().getPersistentDataContainer();
        return axeId.equals(container.get(keys.customIdKey, PersistentDataType.STRING));
    }

    public boolean hasName(ItemStack axe) {
        PersistentDataContainer container = axe.getItemMeta().getPersistentDataContainer();
        return container.has(keys.customNameKey, PersistentDataType.STRING);
    }

    public String getName(ItemStack axe) {
        PersistentDataContainer container = axe.getItemMeta().getPersistentDataContainer();
        return container.get(keys.customNameKey, PersistentDataType.STRING);
    }

    public String setName(ItemStack axe) {
        String randomName = getRandomName();
        ItemMeta meta = axe.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.customNameKey, PersistentDataType.STRING, randomName);
        container.set(keys.customIdKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        meta.displayName(Component.text(randomName + " the Axe", NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false));
        axe.setItemMeta(meta);
        return randomName;
    }

    public void setLastUse(ItemStack axe) {
        ItemMeta meta = axe.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.lastUseKey, PersistentDataType.LONG, System.currentTimeMillis());
        axe.setItemMeta(meta);
    }

    public boolean canSendChopMessage(ItemStack axe) {
        return System.currentTimeMillis() - getLastUse(axe) >= chopMessageCooldown;
    }

    public boolean canStartComplaining(ItemStack axe) {
        return System.currentTimeMillis() - getLastUse(axe) >= delayBeforeComplaining;
    }

    private long getLastUse(ItemStack axe) {
        return axe.getItemMeta().getPersistentDataContainer().getOrDefault(keys.lastUseKey, PersistentDataType.LONG, 0L);
    }

    public long getDelayBeforeComplaining() {
        return delayBeforeComplaining;
    }

    public int getComplainingDelay() {
        return random.nextInt(minComplainingDelay, maxComplainingDelay);
    }

    private String getRandomName() {
        return names[random.nextInt(names.length)];
    }

    private void initNames() {
        names = new String[] {
                "Daisy", "Lily", "Emily", "Molly", "Stacy", "Amy", "Abby", "Zoey", "Nancy"
        };
    }
}
