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
