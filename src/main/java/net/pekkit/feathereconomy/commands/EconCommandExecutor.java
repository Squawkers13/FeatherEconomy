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
package net.pekkit.feathereconomy.commands;

import java.io.File;
import java.io.IOException;
import net.gravitydevelopment.updater.Updater;
import net.pekkit.feathereconomy.FeatherEconomy;
import net.pekkit.feathereconomy.api.FeatherAPI;
import net.pekkit.feathereconomy.locale.MessageSender;
import net.pekkit.feathereconomy.updater.FeatherUpdater;
import net.pekkit.feathereconomy.util.Constants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Squawkers13
 */
public class EconCommandExecutor implements CommandExecutor {

    private final FeatherEconomy plugin;
    private final FeatherAPI fa;
    private final File pluginFile;

    /**
     *
     * @param par1
     * @param par2
     * @param par3
     */
    public EconCommandExecutor(FeatherEconomy par1, FeatherAPI par2, File par3) {
        plugin = par1;
        fa = par2;
        pluginFile = par3;
    }

    /**
     *
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            MessageSender.sendMsg(sender, "&bFeatherEconomy &a" + plugin.getDescription().getVersion() + ", &bcreated by &aSquawkers13");
            if (sender.hasPermission("feathereconomy.update") && plugin.isUpdateAvail()) {
                if (plugin.getUpdateResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE)) { //Needs update
                    MessageSender.sendMsg(sender, "&bUpdate available: Type &a/econ update&b to download.");
                } else if (plugin.getUpdateResult().equals(Updater.UpdateResult.SUCCESS)) { //Update downloaded
                    MessageSender.sendMsg(sender, "&bUpdate downloaded: Restart the server to activate.");
                } else { //Up to date, as far as we know
                    MessageSender.sendMsg(sender, "&bThe plugin is up to date.");
                }
            }
            MessageSender.sendMsg(sender, "&bType &a/econ ? &bfor help.");
        } else if (args[0].equalsIgnoreCase("?")) {
            helpCommand(sender);
        } else if (args[0].equalsIgnoreCase("set")) {
            setCommand(sender, args);
        } else if (args[0].equalsIgnoreCase("update")) {
            updateCommand(sender);
        } else if (args[0].equalsIgnoreCase("reload")) {
            reloadCommand(sender);
        } else { //Invalid arguments
            MessageSender.sendMsg(sender, "&bThat is not a valid sub-command!");
            MessageSender.sendMsg(sender, "&bType &a/econ ?&b for a list of commands.");
        }
        return true;

    }

    /**
     *
     * @param sender
     */
    public void helpCommand(CommandSender sender) {
        MessageSender.sendMsg(sender, "&3---------- &bFeatherEconomy: &aHelp &3----------");
        if (sender.hasPermission("feathereconomy.set")) {
            MessageSender.sendMsg(sender, "&b/econ &aset &3[player] [value] &b- Set a player's balance.");
        }
        if (sender.hasPermission("feathereconomy.update")) {
            MessageSender.sendMsg(sender, "&b/econ &aupdate &b- Download an update to the plugin, if available.");
        }
        if (sender.hasPermission("feathereconomy.reload")) {
            MessageSender.sendMsg(sender, "&b/econ &areload &b- Reload the plugin's configuration.");
        }
    }

    /**
     *
     * @param sender
     * @param args
     */
    public void setCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("feathereconomy.set")) {
            MessageSender.sendMsg(sender, "&cYou don't have permission to set a player's balance!");
            return;
        }

        if (args.length < 3) {
            MessageSender.sendMsg(sender, "&4Invalid arguments!");
            MessageSender.sendMsg(sender, "&4The correct syntax is: &b/econ &aset &3[player] [value]");
            return;
        }

        int amount;
        try {
            amount = Integer.valueOf(args[2]);
        } catch (NumberFormatException e) {
            MessageSender.sendMsg(sender, "&4The amount must be an integer!");
            return;
        }

        if (amount <= 0) {
            MessageSender.sendMsg(sender, "&4The amount must be positive!");
            return;
        }

        Player player = null;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(args[1])) {
                player = p;
                break;
            }
        }

        if (player == null) {
            MessageSender.sendMsg(sender, "&4That player is not online!");
            return;
        }

        fa.setBalance(player.getUniqueId(), amount);

        String currency = fa.getCurrencyName(player.getUniqueId());

        MessageSender.sendMsg(sender, "&bYou set &a" + player.getName() + "'s balance to " + amount + " " + currency + ".");
        MessageSender.sendMsg(player, "&bYour balance was set to &a" + amount + " " + currency + " &bby &a" + sender.getName() + ".");

        MessageSender.log("&a" + sender.getName() + ": &bSet &a" + player.getName() + "'s &b balance to &a" + amount);

    }

    /**
     *
     * @param sender
     */
    public void updateCommand(CommandSender sender) {
        if (!sender.hasPermission("feathereconomy.update")) {
            MessageSender.sendMsg(sender, "&cYou don't have permission to update the plugin!");
            return;
        }

        Updater updater = new FeatherUpdater(plugin, Constants.UPDATER_ID, pluginFile, Updater.UpdateType.DEFAULT, true);

        if (updater.getResult().equals(Updater.UpdateResult.NO_UPDATE)) {
            MessageSender.sendMsg(sender, "&bThe plugin is up to date.");
        } else if (updater.getResult().equals(Updater.UpdateResult.SUCCESS)) {
            MessageSender.sendMsg(sender, "&bUpdate downloaded! Restart the server to activate.");
        } else {
            MessageSender.sendMsg(sender, "&cAn error occured while updating: &r" + updater.getResult().toString());
        }
    }

    /**
     *
     * @param sender
     */
    public void reloadCommand(CommandSender sender) {
        if (!sender.hasPermission("feathereconomy.reload")) {
            MessageSender.sendMsg(sender, "&cYou don't have permission to reload the configuration!");
            return;
        }
        plugin.reloadConfig();

        if (plugin.getConfig() == null) {
            plugin.saveResource("config.yml", true);
        }

        MessageSender.log(ChatColor.GREEN + sender.getName() + ": &bReloaded configuration");

        if (plugin.getConfig().getDouble("settings.config-version", -1.0D) != Constants.CONFIG_VERSION) {
            MessageSender.sendMsg(sender, "&cIncompatible config detected! Renaming it to config-OLD.yml");
            MessageSender.sendMsg(sender, "&cA new config has been created, please transfer your settings.");
            MessageSender.sendMsg(sender, "&cWhen you have finished, type &6/econ reload&c to load your settings.");
            try {
                plugin.getConfig().save(new File(plugin.getDataFolder(), "config-OLD.yml"));
            } catch (IOException ex) {
                MessageSender.logStackTrace("Error while renaming config!", ex);
            }
            plugin.saveResource("config.yml", true);
        } else {
            MessageSender.sendMsg(sender, "&bConfig successfully reloaded.");
        }
    }
}
