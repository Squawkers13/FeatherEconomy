/*
 * Copyright (C) 2014 Squawkers13
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.pekkit.feathereconomy;

import java.io.File;
import java.io.IOException;
import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateResult;
import net.pekkit.feathereconomy.api.FeatherAPI;
import net.pekkit.feathereconomy.commands.BalanceCommandExecutor;
import net.pekkit.feathereconomy.commands.EconCommandExecutor;
import net.pekkit.feathereconomy.commands.PayCommandExecutor;
import net.pekkit.feathereconomy.data.DatabaseManager;
import net.pekkit.feathereconomy.data.EDataHandler;
import net.pekkit.feathereconomy.listeners.PlayerListener;
import net.pekkit.feathereconomy.locale.MessageSender;
import net.pekkit.feathereconomy.updater.FeatherUpdater;
import net.pekkit.feathereconomy.updater.TaskUpdateCheck;
import net.pekkit.feathereconomy.util.Constants;
import net.pekkit.feathereconomy.util.FeatherUtils;
import net.pekkit.feathereconomy.util.Version;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

/**
 * FeatherEconomy- light as a feather Simple and lightweight economy plugin.
 *
 * @author Squawkers13
 */
public class FeatherEconomy extends JavaPlugin {

    private Updater updater;
    private UpdateResult updateResult;

    private DatabaseManager dm;
    private EDataHandler edh;

    private PlayerListener pl;

    private FeatherAPI fa;

    /**
     *
     */
    @Override
    public void onEnable() {

        // --- Version check ---
        Version installed = FeatherUtils.getServerVersion(getServer().getVersion());
        Version required = new Version(Constants.MIN_MINECRAFT_VERSION);
        if (installed.compareTo(required) < 0) {
            MessageSender.log("&4This plugin requires &cCraftBukkit 1.7.10 &4or higher!");
            MessageSender.log("&4Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        // --- Config check ---
        if (getConfig().getDouble("settings.config-version", -1.0D) != Constants.CONFIG_VERSION) {
            MessageSender.log("&cIncompatible config detected! Renaming it to config-OLD.yml");
            MessageSender.log("&cA new config has been created, please transfer your settings.");
            MessageSender.log("&cWhen you have finished, type &6/econ reload&c to load your settings.");
            try {
                getConfig().save(new File(getDataFolder(), "config-OLD.yml"));
            } catch (IOException ex) {
                MessageSender.logStackTrace("Error while renaming config!", ex);
            }
            saveResource("config.yml", true);
        }

        // --- Updater registration ---
        if (getConfig().getBoolean("update-check", true)) {
            if (getConfig().getBoolean("settings.general.auto-update", true)) {
                MessageSender.log("&bPerforming update check...");
                updater = new FeatherUpdater(this, Constants.UPDATER_ID, getFile(), Updater.UpdateType.DEFAULT, true);
                updateResult = updater.getResult();
            } else {
                MessageSender.log("&bPerforming update check...");
                updater = new FeatherUpdater(this, Constants.UPDATER_ID, getFile(), Updater.UpdateType.NO_DOWNLOAD, true);
                updateResult = updater.getResult();
            }
        } else {
            MessageSender.log("&cUpdate checking has been disabled!");
            MessageSender.log("&cPlease regularly check for updates by running &a/econ update&c!");
        }
        if (updateResult == null) {
            //Do nothing
        } else if (updateResult.equals(Updater.UpdateResult.UPDATE_AVAILABLE)) { //Needs update
            MessageSender.log("&bUpdate found! Type /econ update to download.");
        } else if (updateResult.equals(Updater.UpdateResult.SUCCESS)) { //Update downloaded
            MessageSender.log("&bUpdate downloaded! Restart the server to activate.");
        } else if (updateResult.equals(Updater.UpdateResult.NO_UPDATE)) { //Up to date
            MessageSender.log("&bThe plugin is up to date.");
        } else { //Error
            MessageSender.log("&cAn error occured while updating: &r" + updater.getResult().toString());
        }

        long taskDelay = getConfig().getLong("settings.general.update-interval", 43200) * 20;

        if (taskDelay > 0) {
            new TaskUpdateCheck(this, getFile()).runTaskTimer(this, taskDelay, taskDelay);
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

        getCommand("econ").setExecutor(new EconCommandExecutor(this, fa, getFile()));

        getCommand("balance").setExecutor(new BalanceCommandExecutor(this, fa));

        getCommand("pay").setExecutor(new PayCommandExecutor(this, fa));

    }

    /**
     *
     */
    @Override
    public void onDisable() {
        //Nothing here yet!
    }

    /**
     *
     * @return
     */
    public UpdateResult getUpdateResult() {
        return updateResult;
    }

    /**
     * Internal use only please!
     *
     * @param result
     */
    public void setUpdateResult(UpdateResult result) {
        updateResult = result;
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
