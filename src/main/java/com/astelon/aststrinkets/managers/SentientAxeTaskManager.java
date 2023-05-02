package com.astelon.aststrinkets.managers;

import com.astelon.aststrinkets.AstsTrinkets;
import com.astelon.aststrinkets.trinkets.SentientAxe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class SentientAxeTaskManager {

    private final AstsTrinkets plugin;
    private final SentientAxeMessageManager messageManager;
    private final SentientAxe sentientAxe;

    private final HashMap<PlayerAxePair, BukkitTask> taskMap;

    public SentientAxeTaskManager(AstsTrinkets plugin, TrinketManager trinketManager, SentientAxeMessageManager messageManager) {
        this.plugin = plugin;
        this.messageManager = messageManager;
        sentientAxe = trinketManager.getSentientAxe();
        taskMap = new HashMap<>();
    }

    public void startTaskBeforeComplaining(String playerName, String axeId) {
        PlayerAxePair pair = new PlayerAxePair(playerName, axeId);
        if (taskMap.containsKey(pair))
            return;
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, new RunnableBeforeComplaining(playerName, axeId),
                sentientAxe.getDelayBeforeComplaining() / 1000 * 20);
        taskMap.put(pair, task);
    }

    public void startComplainingTask(String playerName, String axeId) {
        PlayerAxePair pair = new PlayerAxePair(playerName, axeId);
        if (taskMap.containsKey(pair))
            return;
        BukkitTask task = Bukkit.getScheduler().runTask(plugin, new ComplainingRunnable(playerName, axeId));
        taskMap.put(pair, task);
    }

    public void cancelTask(String playerName, String axeId) {
        PlayerAxePair pair = new PlayerAxePair(playerName, axeId);
        BukkitTask task = taskMap.remove(pair);
        if (task != null) {
            task.cancel();
        }
    }

    private class RunnableBeforeComplaining implements Runnable {

        private final String playerName;
        private final String axeId;

        private RunnableBeforeComplaining(String playerName, String axeId) {
            this.playerName = playerName;
            this.axeId = axeId;
        }

        @Override
        public void run() {
            taskMap.remove(new PlayerAxePair(playerName, axeId));
            if (!sentientAxe.isEnabled())
                return;
            Player player = Bukkit.getPlayer(playerName);
            if (player == null)
                return;
            PlayerInventory inventory = player.getInventory();
            for (ItemStack itemStack: inventory) {
                if (sentientAxe.isTrinket(itemStack) && sentientAxe.isTheSameAxe(itemStack, axeId)) {
                    if (sentientAxe.canStartComplaining(itemStack)) {
                        startComplainingTask(playerName, axeId);
                        return;
                    } else {
                        startTaskBeforeComplaining(playerName, axeId);
                    }
                }
            }
        }
    }

    private class ComplainingRunnable implements Runnable {

        private final String playerName;
        private final String axeId;

        private ComplainingRunnable(String playerName, String axeId) {
            this.playerName = playerName;
            this.axeId = axeId;
        }

        @Override
        public void run() {
            if (!sentientAxe.isEnabled())
                return;
            Player player = Bukkit.getPlayer(playerName);
            if (player == null)
                return;
            PlayerInventory inventory = player.getInventory();
            for (ItemStack itemStack: inventory) {
                if (sentientAxe.isTrinket(itemStack) && sentientAxe.isTheSameAxe(itemStack, axeId)) {
                    if (sentientAxe.canStartComplaining(itemStack)) {
                        String axeName = sentientAxe.getName(itemStack);
                        player.sendMessage(messageManager.getIdleMessage(axeName));
                        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, new ComplainingRunnable(playerName, axeId),
                                sentientAxe.getComplainingDelay());
                        taskMap.put(new PlayerAxePair(playerName, axeId), task);
                        return;
                    }
                }
            }
        }
    }

    private record PlayerAxePair(String playerName, String axeId) {}
}
