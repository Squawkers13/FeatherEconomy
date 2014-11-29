/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2014 Squawkers13 <Squawkers13@pekkit.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
                        int balance = (int) economy.getBalance(players[i]);
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
