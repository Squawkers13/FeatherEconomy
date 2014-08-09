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
package net.pekkit.feathereconomy.updater;

import java.io.File;
import net.gravitydevelopment.updater.Updater;
import net.pekkit.feathereconomy.FeatherEconomy;
import net.pekkit.feathereconomy.locale.MessageSender;
import net.pekkit.feathereconomy.util.Constants;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Squawkers13
 */
public class TaskUpdateCheck extends BukkitRunnable {

    private final FeatherEconomy plugin;
    private final File pluginFile;

    private Updater updater;

    /**
     *
     * @param par1
     * @param par2
     */
    public TaskUpdateCheck(FeatherEconomy par1, File par2) {
        plugin = par1;
        pluginFile = par2;
    }

    @Override
    public void run() {
        MessageSender.log("&bPerforming update check...");
        if (plugin.getConfig().getBoolean("settings.general.auto-update", true)) {
            updater = new FeatherUpdater(plugin, Constants.UPDATER_ID, pluginFile, Updater.UpdateType.DEFAULT, true);
        } else {
            updater = new FeatherUpdater(plugin, Constants.UPDATER_ID, pluginFile, Updater.UpdateType.NO_DOWNLOAD, true);
        }
        
        if(updater.getResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE)) { //Needs update
            MessageSender.log("&bUpdate available! Type &a/econ update&b to download.");
        } else if (updater.getResult().equals(Updater.UpdateResult.SUCCESS)) { //Update downloaded
            MessageSender.log("&bUpdate downloaded! Restart the server to activate.");
        } else if (updater.getResult().equals(Updater.UpdateResult.NO_UPDATE)) { //Up to date
            MessageSender.log("&bThe plugin is up to date.");
        } else { //Error
            MessageSender.log("&cAn error occured while updating: &r" + updater.getResult().toString());
        }
        
        plugin.setUpdateResult(updater.getResult());

    }

}
