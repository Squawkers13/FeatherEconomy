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

import net.pekkit.feathereconomy.FeatherEconomy;
import net.pekkit.feathereconomy.api.FeatherAPI;
import net.pekkit.feathereconomy.locale.MessageSender;
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
public class BalanceCommandExecutor implements CommandExecutor {

    private final FeatherEconomy plugin;
    private final FeatherAPI fa;

    /**
     *
     * @param par1
     * @param par2
     */
    public BalanceCommandExecutor(FeatherEconomy par1, FeatherAPI par2) {
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
        if (args.length < 1) {
            if (sender instanceof ConsoleCommandSender) {

                MessageSender.sendMsg(sender, "&bYour balance: &aInfinite");
                return true;
            } else {
                Player player = (Player) sender;
                if (!player.hasPermission("feathereconomy.balance.view")) {
                    MessageSender.sendMsg(player, "&cYou don't have permission to view your balance!");
                    return true;
                }
                int balance = fa.getBalance(player.getUniqueId());

                String currency = fa.getCurrencyName(player.getUniqueId());

                MessageSender.sendMsg(player, "&bYour balance: &a" + balance + " " + currency);
                return true;
            }
        } else {

            if (!sender.hasPermission("feathereconomy.balance.other")) {
                MessageSender.sendMsg(sender, "&cYou don't have permission to view another player's balance!");
                return true;
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
                return true;
            }
            int balance = fa.getBalance(other.getUniqueId());

            String currency = fa.getCurrencyName(other.getUniqueId());

            MessageSender.sendMsg(sender, ChatColor.GREEN + other.getName() + "'s &bbalance: &a" + balance + " " + currency);
            return true;
        }

    }
}
