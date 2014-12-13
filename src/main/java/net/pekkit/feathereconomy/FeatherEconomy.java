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
package net.pekkit.feathereconomy;

import java.io.File;
import java.io.IOException;
import net.milkbowl.vault.economy.Economy;
import net.pekkit.feathereconomy.api.FeatherAPI;
import net.pekkit.feathereconomy.commands.BaseCommandExecutor;
import net.pekkit.feathereconomy.data.DatabaseManager;
import net.pekkit.feathereconomy.data.EDataHandler;
import net.pekkit.feathereconomy.listeners.PlayerListener;
import net.pekkit.feathereconomy.locale.MessageSender;
import net.pekkit.feathereconomy.util.Constants;
import net.pekkit.feathereconomy.util.FeatherUtils;
import net.pekkit.feathereconomy.util.Version;
import net.pekkit.feathereconomy.vault.Economy_FeatherEcon;
import net.pekkit.feathereconomy.vault.VaultImport;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

/**
 * FeatherEconomy- light as a feather Simple and lightweight economy plugin.
 *
 * @author Squawkers13
 */
public class FeatherEconomy extends JavaPlugin {

    private DatabaseManager dm;
    private EDataHandler edh;
    
    private BaseCommandExecutor ece;

    private PlayerListener pl;

    private FeatherAPI fa;
    
    private VaultImport va;

    /**
     *
     */
    @Override
    public void onEnable() {

        // --- Version check ---
        Version installed = FeatherUtils.getServerVersion(getServer().getVersion());
        Version required = new Version(Constants.MIN_MINECRAFT_VERSION);
        if (installed.compareTo(required) < 0) {
            MessageSender.log("&4This plugin requires &cCraftBukkit 1.7.9 &4or higher!");
            MessageSender.log("&4Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        // --- Config check ---
        if (getConfig().getDouble("settings.config-version", -1.0D) != Constants.CONFIG_VERSION) {
            
            String old = getConfig().getString("settings.config-version", "OLD");
            
            MessageSender.log("&cIncompatible config detected! Renaming it to config-" + old + ".yml");
            MessageSender.log("&cA new config has been created, please transfer your settings.");
            MessageSender.log("&cWhen you have finished, type &6/econ reload&c to load your settings.");
            try {
                getConfig().save(new File(getDataFolder(), "config-" + old + ".yml"));
            } catch (IOException ex) {
                MessageSender.logStackTrace("Error while renaming config!", ex);
            }
            saveResource("config.yml", true);
        }

        // --- MCStats submission ---
        if (getConfig().getBoolean("settings.general.stats")) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-(
            }
        }

        dm = new DatabaseManager(this);

        edh = new EDataHandler(this, dm);

        fa = new FeatherAPI(this, edh);

        pl = new PlayerListener(this, fa);
        getServer().getPluginManager().registerEvents(pl, this);
        
        ece = new BaseCommandExecutor(this, fa);

        getCommand("econ").setExecutor(ece);

        getCommand("balance").setExecutor(ece);

        getCommand("pay").setExecutor(ece);

        Plugin vault = getServer().getPluginManager().getPlugin("Vault");
        if (vault != null) {
            va = new VaultImport(this);
            if (getConfig().getBoolean("settings.advanced.register-vault", true)) {
                ServicesManager manager = getServer().getServicesManager();
                Class<? extends Economy> clazz = Economy_FeatherEcon.class;
                String name = "FeatherEconomy";
                try {
                    Economy econ = (Economy) clazz.getConstructor(new Class[]{FeatherEconomy.class}).newInstance(new Object[]{this});
                    manager.register(Economy.class, econ, vault, ServicePriority.Highest);
                    vault.getLogger().info(String.format("[Economy] %s found: %s", new Object[]{name, econ.isEnabled() ? "Loaded" : "Waiting"}));
                } catch (Exception e) {
                    vault.getLogger().severe(String.format("[Economy] There was an error hooking %s - check to make sure you're using a compatible version!", new Object[]{name}));
                }
            }
        }
    }

    /**
     *
     */
    @Override
    public void onDisable() {
        //Nothing here yet!
    }

    /**
     * Gets the plugin's API.
     *
     * @return API instance
     * @see FeatherAPI
     */
    public FeatherAPI getAPI() {
        return fa;
    }

}
