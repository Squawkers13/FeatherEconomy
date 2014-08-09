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

            String currency = fa.getCurrencyName(other.addPotionEffect(null));

            MessageSender.sendMsg(sender, ChatColor.GREEN + other.getName() + "'s &bbalance: &a" + balance + " " + currency);
            return true;
        }

    }
}
