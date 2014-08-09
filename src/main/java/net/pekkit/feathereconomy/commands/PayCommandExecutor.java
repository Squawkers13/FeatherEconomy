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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Squawkers13
 */
public class PayCommandExecutor implements CommandExecutor {

    private final FeatherEconomy plugin;
    private final FeatherAPI fa;

    /**
     *
     * @param par1
     * @param par2
     */
    public PayCommandExecutor(FeatherEconomy par1, FeatherAPI par2) {
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
        if (!sender.hasPermission("feathereconomy.pay")) {
            MessageSender.sendMsg(sender, "&cYou don't have permission to pay another player!");
            return true;
        }
        
        if (args.length < 2) {
            MessageSender.sendMsg(sender, "&4Invalid arguments!");
            MessageSender.sendMsg(sender, "&4The correct syntax is: &b/pay &a[player] [amount]");
            return true;
        }

        int amount;
        try {
            amount = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            MessageSender.sendMsg(sender, "&4The amount must be an integer!");
            return true;
        }

        if (amount <= 0) {
            MessageSender.sendMsg(sender, "&4The amount must be positive!");
            return true;
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
            return true;
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
                return true;
            }

            fa.payPlayer(cmd.getUniqueId(), player.getUniqueId(), amount);

            String currency = fa.getCurrencyName(player.getUniqueId());

            MessageSender.sendMsg(cmd, "&bYou gave &a" + player.getName() + " " + amount + " " + currency + ".");
            MessageSender.sendMsg(player, "&bYou were given &a" + +amount + " " + currency + " &bby &a" + cmd.getName() + ".");
        }
        return true;
    }

}
