package net.pekkit.feathereconomy.vault;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import net.pekkit.feathereconomy.FeatherEconomy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultImport {

    private final List<Economy> economies;
    private FeatherEconomy plugin;

    public VaultImport(FeatherEconomy pl) {
        this.economies = new ArrayList();

        FeatherEconomy plugin = pl;
        Collection<RegisteredServiceProvider<Economy>> providers = plugin.getServer().getServicesManager().getRegistrations(Economy.class);
        if (providers == null) {
            return;
        }
        for (RegisteredServiceProvider<Economy> econProvider : providers) {
            Economy provider = econProvider.getProvider();
            if (!(provider instanceof Economy_FeatherEcon)) {
                this.economies.add(provider);
            }
        }
    }

    public void doImport(String pluginName) {
        if (this.economies.size() <= 0) {
            plugin.getLogger().warning("No plugins to import!");
            return;
        }
        for (Economy economy : this.economies) {
            if (economy.getName().equalsIgnoreCase(pluginName)) {
                doImport(economy);
                return;
            }
        }
        if (this.economies.size() <= 0) {
            plugin.getLogger().warning("Cannot find plugin '" + pluginName + "' to import.");
        }
    }

    public void doImport() {
        if (this.economies.size() <= 0) {
            plugin.getLogger().warning("No plugins to import!");
            return;
        }
        for (Economy economy : this.economies) {
            doImport(economy);
        }
    }

    public boolean canImport(String pluginName) {
        for (Economy economy : this.economies) {
            if (economy.getName().equalsIgnoreCase(pluginName)) {
                return true;
            }
        }
        return false;
    }

    public void doImport(final Economy economy) {
        if (economy != null) {
            plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
                public void run() {
                    plugin.getLogger().info("[Plugin: " + economy.getName() + "] Iterating over all offline players...");
                    OfflinePlayer[] players = plugin.getServer().getOfflinePlayers();
                    int lastPercent = -1;
                    for (int i = 0; i < players.length; i++) {
                        int balance = (int) economy.getBalance(players[i].getName());
                        plugin.getAPI().setBalance(players[i].getUniqueId(), balance);
                        int percent = (int) (i / players.length * 100.0D);
                        if ((percent % 10 == 0) && (percent != lastPercent)) {
                            plugin.getLogger().info("[Plugin: " + economy.getName() + "]" + i + "/" + players.length + " imported. (" + percent + "%)");
                            lastPercent = percent;
                        }
                    }
                    plugin.getLogger().info("[Plugin: " + economy.getName() + "] Import complete!");
                }
            });
        }
    }
}
