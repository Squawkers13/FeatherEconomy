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
package net.pekkit.feathereconomy.commands;

import java.io.File;
import java.io.IOException;
import net.pekkit.feathereconomy.FeatherEconomy;
import net.pekkit.feathereconomy.api.FeatherAPI;
import net.pekkit.feathereconomy.locale.MessageSender;
import net.pekkit.feathereconomy.util.Constants;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Squawkers13
 */
public class EconCommandExecutor implements CommandExecutor {

    private final FeatherEconomy plugin;
    private final FeatherAPI fa;

    /**
     *
     * @param par1
     * @param par2
     */
    public EconCommandExecutor(FeatherEconomy par1, FeatherAPI par2) {
        plugin = par1;
        fa = par2;
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
        if (command.getName().equalsIgnoreCase("fe")) {
            baseCommand(command, sender, args);
        } else if (command.getName().equalsIgnoreCase("balance")) {
            balanceCommand(command, sender, args);
        } else if (command.getName().equalsIgnoreCase("pay")) {
            payCommand(command, sender, args);
        }
        return true;

    }

    public void baseCommand(Command command, CommandSender sender, String[] args) {
        if (args.length < 1) {
            MessageSender.sendMsg(sender, "&bFeatherEconomy &a" + plugin.getDescription().getVersion() + ", &bcreated by &aSquawkers13");
            MessageSender.sendMsg(sender, "&bType &a/fe ? &bfor help.");
        } else if (args[0].equalsIgnoreCase("?")) {
            helpCommand(sender);
        } else if (args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("b")) {
            balanceCommand(command, sender, args);
        } else if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("s")) {
            setCommand(sender, args);
        } else if (args[0].equalsIgnoreCase("pay") || args[0].equalsIgnoreCase("p")) {
            payCommand(command, sender, args);
        } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
            reloadCommand(sender);
        } else { //Invalid arguments
            MessageSender.sendMsg(sender, "&bThat is not a valid sub-command!");
            MessageSender.sendMsg(sender, "&bType &a/fe ?&b for a list of commands.");
        }
    }

    /**
     *
     * @param sender
     */
    public void helpCommand(CommandSender sender) {
        MessageSender.sendMsg(sender, "&3---------- &bFeatherEconomy: &aHelp &3----------");
        if (sender.hasPermission("feathereconomy.balance.view")) {
            MessageSender.sendMsg(sender, "&b/fe &abalance,b &2<player> &b- View a player's balance, or your own.");
        }
        if (sender.hasPermission("feathereconomy.set")) {
            MessageSender.sendMsg(sender, "&b/fe &aset,s &3[player] [value] &b- Set a player's balance.");
        }
        if (sender.hasPermission("feathereconomy.pay")) {
            MessageSender.sendMsg(sender, "&b/fe &apay,p &3[player] [value] &b- Pay a player.");
        }
        if (sender.hasPermission("feathereconomy.reload")) {
            MessageSender.sendMsg(sender, "&b/fe &areload,r &b- Reload the plugin's configuration.");
        }
    }

    public void balanceCommand(Command command, CommandSender sender, String[] args) {
        if (command.getName().equalsIgnoreCase("fe")) {
            if (args.length < 2) {
                if (sender instanceof ConsoleCommandSender) {
                    MessageSender.sendMsg(sender, "&bYour balance: &aInfinite");
                } else {
                    Player player = (Player) sender;
                    if (!player.hasPermission("feathereconomy.balance.view")) {
                        MessageSender.sendMsg(player, "&cYou don't have permission to view your balance!");
                        return;
                    }
                    int balance = fa.getBalance(player.getUniqueId());

                    String currency = fa.getCurrencyName(player.getUniqueId());

                    MessageSender.sendMsg(player, "&bYour balance: &a" + balance + " " + currency);
                }
            } else {
                if (!sender.hasPermission("feathereconomy.balance.other")) {
                    MessageSender.sendMsg(sender, "&cYou don't have permission to view another player's balance!");
                    return;
                }
                Player other = null;
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    if (p.getName().equalsIgnoreCase(args[1])) {
                        other = p;
                        break;
                    }
                }

                if (other == null) {
                    MessageSender.sendMsg(sender, "&4That player is not online!");
                    return;
                }
                int balance = fa.getBalance(other.getUniqueId());

                String currency = fa.getCurrencyName(other.getUniqueId());

                MessageSender.sendMsg(sender, ChatColor.GREEN + other.getName() + "'s &bbalance: &a" + balance + " " + currency);
            }
        } else if (command.getName().equalsIgnoreCase("balance")) {
            if (args.length < 1) {
                if (sender instanceof ConsoleCommandSender) {

                    MessageSender.sendMsg(sender, "&bYour balance: &aInfinite");
                } else {
                    Player player = (Player) sender;
                    if (!player.hasPermission("feathereconomy.balance.view")) {
                        MessageSender.sendMsg(player, "&cYou don't have permission to view your balance!");
                        return;
                    }
                    int balance = fa.getBalance(player.getUniqueId());

                    String currency = fa.getCurrencyName(player.getUniqueId());

                    MessageSender.sendMsg(player, "&bYour balance: &a" + balance + " " + currency);
                }
            } else {

                if (!sender.hasPermission("feathereconomy.balance.other")) {
                    MessageSender.sendMsg(sender, "&cYou don't have permission to view another player's balance!");
                    return;
                }
                Player other = null;
                for (Player p : plugin.getServer().getOnlinePlayers()) {
                    if (p.getName().equalsIgnoreCase(args[0])) {
                        other = p;
                        break;
                    }
                }

                if (other == null) {
                    MessageSender.sendMsg(sender, "&4That player is not online!");
                    return;
                }
                int balance = fa.getBalance(other.getUniqueId());

                String currency = fa.getCurrencyName(other.getUniqueId());

                MessageSender.sendMsg(sender, ChatColor.GREEN + other.getName() + "'s &bbalance: &a" + balance + " " + currency);
            }
        }
    }

    public void payCommand(Command command, CommandSender sender, String[] args) {
        if (!sender.hasPermission("feathereconomy.pay")) {
                MessageSender.sendMsg(sender, "&cYou don't have permission to pay another player!");
                return;
            }
        
        if (command.getName().equalsIgnoreCase("fe")) {
            if (args.length < 3) {
                MessageSender.sendMsg(sender, "&4Invalid arguments!");
                MessageSender.sendMsg(sender, "&4The correct syntax is: &b/fe pay &a[player] [amount]");
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

            if (sender instanceof ConsoleCommandSender) {
                fa.depositPlayer(player.getUniqueId(), amount);

                String currency = fa.getCurrencyName(player.getUniqueId());

                MessageSender.sendMsg(sender, "&bYou gave &a" + player.getName() + " " + amount + " " + currency + ".");
                MessageSender.sendMsg(player, "&bYou were given &a" + +amount + " " + currency + " &bby &a" + sender.getName() + ".");
            } else {
                Player cmd = (Player) sender;
                if (!fa.canAffordWithdrawal(cmd.getUniqueId(), amount)) {
                    MessageSender.sendMsg(cmd, "&4You don't have enough money to do that!");
                    return;
                }

                fa.payPlayer(cmd.getUniqueId(), player.getUniqueId(), amount);

                String currency = fa.getCurrencyName(player.getUniqueId());

                MessageSender.sendMsg(cmd, "&bYou gave &a" + player.getName() + " " + amount + " " + currency + ".");
                MessageSender.sendMsg(player, "&bYou were given &a" + +amount + " " + currency + " &bby &a" + cmd.getName() + ".");
            }
        } else if (command.getName().equalsIgnoreCase("pay")) {
            if (args.length < 2) {
                MessageSender.sendMsg(sender, "&4Invalid arguments!");
                MessageSender.sendMsg(sender, "&4The correct syntax is: &b/pay &a[player] [amount]");
                return;
            }

            int amount;
            try {
                amount = Integer.valueOf(args[1]);
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
                if (p.getName().equalsIgnoreCase(args[0])) {
                    player = p;
                    break;
                }
            }

            if (player == null) {
                MessageSender.sendMsg(sender, "&4That player is not online!");
                return;
            }

            if (sender instanceof ConsoleCommandSender) {
                fa.depositPlayer(player.getUniqueId(), amount);

                String currency = fa.getCurrencyName(player.getUniqueId());

                MessageSender.sendMsg(sender, "&bYou gave &a" + player.getName() + " " + amount + " " + currency + ".");
                MessageSender.sendMsg(player, "&bYou were given &a" + +amount + " " + currency + " &bby &a" + sender.getName() + ".");
            } else {
                Player cmd = (Player) sender;
                if (!fa.canAffordWithdrawal(cmd.getUniqueId(), amount)) {
                    MessageSender.sendMsg(cmd, "&4You don't have enough money to do that!");
                    return;
                }

                fa.payPlayer(cmd.getUniqueId(), player.getUniqueId(), amount);

                String currency = fa.getCurrencyName(player.getUniqueId());

                MessageSender.sendMsg(cmd, "&bYou gave &a" + player.getName() + " " + amount + " " + currency + ".");
                MessageSender.sendMsg(player, "&bYou were given &a" + +amount + " " + currency + " &bby &a" + cmd.getName() + ".");
            }
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
            MessageSender.sendMsg(sender, "&4The correct syntax is: &b/fe &aset &3[player] [value]");
            return;
        }

        int amount;
        try {
            amount = Integer.valueOf(args[2]);
        } catch (NumberFormatException e) {
            MessageSender.sendMsg(sender, "&4The amount must be an integer!");
            return;
        }

        if (amount < 0) {
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
