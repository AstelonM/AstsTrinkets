package com.astelon.aststrinkets.listeners;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.managers.TrinketManager;
import com.astelon.aststrinkets.trinkets.Spellbook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;

public class BookListener implements Listener {

    private final AstsTrinkets plugin;
    private final TrinketManager trinketManager;
    private final Spellbook spellbook;

    public BookListener(AstsTrinkets plugin, TrinketManager trinketManager) {
        this.plugin = plugin;
        this.trinketManager = trinketManager;
        spellbook = trinketManager.getSpellbook();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        if (!spellbook.isEnabled() || !event.isSigning())
            return;
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItemInMainHand();
        if (!spellbook.isTrinket(itemStack) || !spellbook.isEmpty(itemStack))
            itemStack = inventory.getItemInOffHand();
        if (!spellbook.isTrinket(itemStack) || !spellbook.isEmpty(itemStack))
            return;
        if (!trinketManager.isOwnedBy(itemStack, player.getName())) {
            event.setCancelled(true);
            return;
        }
        BookMeta bookMeta = event.getNewBookMeta();
        if (!bookMeta.hasPages()) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You can't create an empty spellbook.", NamedTextColor.RED));
            return;
        }

        BookMeta resultBookMeta = spellbook.createSpellbook(bookMeta);
        if (resultBookMeta == null) {
            player.sendMessage(Component.text("No command was detected.", NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }
        event.setNewBookMeta(resultBookMeta);
    }
}
