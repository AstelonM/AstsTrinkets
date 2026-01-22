package com.astelon.aststrinkets.trinkets;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.Power;
import com.astelon.aststrinkets.utils.NamespacedKeys;
import com.astelon.aststrinkets.utils.Usages;
import com.astelon.aststrinkets.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spellbook extends Trinket {

    private static final Pattern COOLDOWN_PATTERN = Pattern.compile("<cooldown:(\\d+)>");
    private static final Pattern USES_PATTERN = Pattern.compile("<uses:([1-9]\\d*)>");
    private static final Pattern TITLE_PATTERN = Pattern.compile("<title:(.+)>");
    private static final Pattern FUNCTIONAL_COPIES_PATTERN = Pattern.compile("<functionalCopies:(original|copy)>");
    private static final Pattern WORLD_PATTERN = Pattern.compile("<world:(.+)>");
    private static final Pattern USE_CHANCE_PATTERN = Pattern.compile("<useChance:([1-9]\\d*\\.\\d+|[1-9]\\d*)>");

    private final Random random;

    private final Component useUsage;

    public Spellbook(AstsTrinkets plugin, NamespacedKeys keys) {
        super(plugin, keys, "spellbook", Power.RUN_COMMANDS, true, Usages.SPELLBOOK_CREATE);
        useUsage = MiniMessage.miniMessage().deserialize("<gold>How to use: <trinketname></gold><br><trinketusage>",
                Placeholder.component("trinketname", this.itemStack.displayName().hoverEvent(this.itemStack.asHoverEvent())),
                Placeholder.parsed("trinketusage", "<green>" + Usages.SPELLBOOK_USE));
        random = new Random();
    }

    @Override
    protected ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("Empty Spellbook", TextColor.fromHexString("#246BCE")));
        meta.lore(List.of(Component.text("An empty spellbook.")));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public boolean isEmpty(ItemStack spellbook) {
        return spellbook.getType() == Material.WRITABLE_BOOK;
    }

    public BookMeta createSpellbook(BookMeta bookMeta) {
        List<String> pages = bookMeta.pages().stream().map(PlainTextComponentSerializer.plainText()::serialize).toList();
        int displayIndex = -1;
        ArrayList<String> commands = new ArrayList<>();
        StringBuilder nextCommand = new StringBuilder();
        int cooldown = -1;
        int uses = -1;
        double useChance = -1;
        String customTitle = null;
        boolean noCopy = false;
        byte functionalCopies = -1;
        List<String> worlds = new ArrayList<>();
        for (int i = 0; i < pages.size() && displayIndex == -1; i++) {
            String[] lines = pages.get(i).split("\n");
            for (int j = 0; j < lines.length; j++) {
                String line = lines[j];
                if (line.isBlank())
                    continue;
                if (line.equalsIgnoreCase("<display>")) {
                    displayIndex = i + 1;
                    parseAndAddCommand(commands, nextCommand.toString());
                    break;
                }
                if (line.equalsIgnoreCase("<noCopy>")) {
                    noCopy = true;
                }
                Matcher matcher = COOLDOWN_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String cooldownGroup = matcher.group(1);
                    if (cooldownGroup != null)
                        cooldown = Integer.parseInt(cooldownGroup);
                }
                matcher = USES_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String usesGroup = matcher.group(1);
                    if (usesGroup != null)
                        uses = Integer.parseInt(usesGroup);
                }
                matcher = TITLE_PATTERN.matcher(line);
                if (matcher.matches()) {
                    customTitle = matcher.group(1);
                }
                matcher = FUNCTIONAL_COPIES_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String copyOf = matcher.group(1);
                    if (copyOf.equalsIgnoreCase("original"))
                        functionalCopies = 0;
                    else if (copyOf.equalsIgnoreCase("copy"))
                        functionalCopies = 1;
                }
                matcher = WORLD_PATTERN.matcher(line);
                if (matcher.matches()) {
                    worlds.add(matcher.group(1));
                }
                matcher = USE_CHANCE_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String useChanceGroup = matcher.group(1);
                    if (useChanceGroup != null) {
                        useChance = Double.parseDouble(useChanceGroup);
                        useChance = Utils.normalizeRate(Utils.ensurePercentage(useChance, 0));
                    }
                }
                if (line.startsWith("/") || line.startsWith("<")) {
                    parseAndAddCommand(commands, nextCommand.toString());
                    nextCommand = new StringBuilder(line);
                } else if (!nextCommand.isEmpty() && j == 0) {
                    nextCommand.append(line);
                }
            }
        }
        if (displayIndex == -1 && !nextCommand.isEmpty())
            parseAndAddCommand(commands, nextCommand.toString());
        if (commands.isEmpty())
            return null;
        List<Component> displayPages = new ArrayList<>();
        if (displayIndex != -1) {
            for (int i = displayIndex; i < pages.size(); i++)
                displayPages.add(bookMeta.page(i + 1));
        } else {
            displayPages = bookMeta.pages();
        }
        BookMeta result = bookMeta.toBuilder().pages(displayPages).title(bookMeta.title())
                .author(bookMeta.author()).build();
        if (noCopy)
            result.setGeneration(BookMeta.Generation.TATTERED);
        PersistentDataContainer container = result.getPersistentDataContainer();
        container.set(keys.nameKey, PersistentDataType.STRING, name);
        container.set(keys.powerKey, PersistentDataType.STRING, power.powerName());
        PersistentDataContainer originalContainer = bookMeta.getPersistentDataContainer();
        String owner = originalContainer.get(keys.ownerKey, PersistentDataType.STRING);
        if (owner != null)
            container.set(keys.ownerKey, PersistentDataType.STRING, owner);
        container.set(keys.commandsKey, PersistentDataType.STRING, serializeCommands(commands));
        if (cooldown != -1)
            container.set(keys.cooldownKey, PersistentDataType.INTEGER, cooldown);
        if (uses != -1)
            container.set(keys.remainingUsesKey, PersistentDataType.INTEGER, uses);
        if (useChance != -1)
            container.set(keys.useChanceKey, PersistentDataType.DOUBLE, useChance);
        if (functionalCopies != -1)
            container.set(keys.functionalCopiesKey, PersistentDataType.BYTE, (byte) functionalCopies);
        if (!worlds.isEmpty())
            container.set(keys.worldWhitelistKey, PersistentDataType.STRING, String.join(",", worlds));
        ArrayList<Component> newLore = createLore(originalContainer, bookMeta, uses);
        result.lore(newLore);
        Component bookName = bookMeta.title();
        if (customTitle != null)
            bookName = Component.text(customTitle);
        if (bookName == null)
            bookName = Component.text("Spellbook");
        result.displayName(bookName.color(TextColor.fromHexString("#246BCE")).decoration(TextDecoration.ITALIC, false));
        return result;
    }

    private ArrayList<Component> createLore(PersistentDataContainer container, ItemMeta meta, int uses) {
        ArrayList<Component> newLore = new ArrayList<>();
        if (container.has(keys.ownerKey, PersistentDataType.STRING)) {
            List<Component> oldLore = meta.lore();
            if (oldLore != null && oldLore.size() >= 1)
                newLore.add(oldLore.get(0));
        }
        if (uses != -1) {
            newLore.add(Component.text("Uses: " + uses, infoColour).decoration(TextDecoration.ITALIC, false));
        }
        newLore.add(Component.text("What powers could this book hold?"));
        return newLore;
    }

    private void parseAndAddCommand(ArrayList<String> commands, String text) {
        if (text.isEmpty() || text.isBlank())
            return;
        if (text.matches("^(<[\\w-:]+>)*/.+"))
            commands.add(text);
    }

    public ItemStack use(ItemStack spellbook) {
        ItemStack result = spellbook.asOne();
        ItemMeta meta = result.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(keys.lastUseKey, PersistentDataType.LONG, System.currentTimeMillis());
        spellbook.setItemMeta(meta);
        int uses = container.getOrDefault(keys.remainingUsesKey, PersistentDataType.INTEGER, -1);
        if (uses == -1) {
            result.setItemMeta(meta);
        } else {
            double chance = container.getOrDefault(keys.useChanceKey, PersistentDataType.DOUBLE, -1.0);
            if (chance == -1 || random.nextDouble() < chance) {
                if (uses == 1)
                    return null;
                else {
                    uses--;
                    container.set(keys.remainingUsesKey, PersistentDataType.INTEGER, uses);
                    ArrayList<Component> newLore = createLore(container, meta, uses);
                    meta.lore(newLore);
                    result.setItemMeta(meta);
                    return result;
                }
            }
        }
        return result;
    }

    public boolean canUse(ItemStack spellbook) {
        PersistentDataContainer container = spellbook.getItemMeta().getPersistentDataContainer();
        int cooldown = container.getOrDefault(keys.cooldownKey, PersistentDataType.INTEGER, -1);
        if (cooldown == -1)
            cooldown = 1;
        long lastUse = container.getOrDefault(keys.lastUseKey, PersistentDataType.LONG, 0L);
        return System.currentTimeMillis() - lastUse >= cooldown * 1000L;
    }

    public boolean canUseHere(ItemStack spellbook, World world) {
        PersistentDataContainer container = spellbook.getItemMeta().getPersistentDataContainer();
        String worldList = container.get(keys.worldWhitelistKey, PersistentDataType.STRING);
        if (worldList == null)
            return true;
        //TODO are commas allowed in world names?
        return worldList.contains(world.getName());
    }

    public boolean isFunctional(ItemStack spellbook) {
        BookMeta meta = (BookMeta) spellbook.getItemMeta();
        BookMeta.Generation generation = meta.getGeneration();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (generation == BookMeta.Generation.COPY_OF_ORIGINAL)
            return container.has(keys.functionalCopiesKey, PersistentDataType.BYTE);
        else if (generation == BookMeta.Generation.COPY_OF_COPY)
            return container.getOrDefault(keys.functionalCopiesKey, PersistentDataType.BYTE, (byte) -1) == 1;
        else
            return true;
    }

    public String[] getCommands(ItemStack spellbook) {
        PersistentDataContainer container = spellbook.getItemMeta().getPersistentDataContainer();
        String serializedCommands = container.get(keys.commandsKey, PersistentDataType.STRING);
        if (serializedCommands == null)
            return null;
        return deserializeCommands(serializedCommands);
    }

    private String serializeCommands(List<String> commands) {
        return String.join("<end>", commands);
    }

    private String[] deserializeCommands(String commands) {
        return commands.split("<end>");
    }

    public Component getUsage(ItemStack spellbook) {
        if (isEmpty(spellbook))
            return super.getUsage();
        return useUsage;
    }
}
