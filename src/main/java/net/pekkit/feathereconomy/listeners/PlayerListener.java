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
package net.pekkit.feathereconomy.listeners;

import net.gravitydevelopment.updater.Updater;
import net.pekkit.feathereconomy.FeatherEconomy;
import net.pekkit.feathereconomy.api.FeatherAPI;
import net.pekkit.feathereconomy.locale.MessageSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author Squawkers13
 */
public class PlayerListener implements Listener {

    private final FeatherEconomy plugin;
    private final FeatherAPI fa;

    /**
     *
     * @param par1
     * @param par2
     */
    public PlayerListener(FeatherEconomy par1, FeatherAPI par2) {
        plugin = par1;
        fa = par2;
    }

    /**
     *
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (fa.hasBalance(player.getUniqueId())) {
            return;
        }

        int balance = plugin.getConfig().getInt("settings.economy.default-balance", 0);

        if (balance > 0) {
            fa.setBalance(player.getUniqueId(), balance);
            MessageSender.log("&bSet &a" + player.getName() + "'s &bbalance to &a" + balance);
        }

        if (player.hasPermission("feathereconomy.update")) {
            if (plugin.getUpdateResult().equals(Updater.UpdateResult.UPDATE_AVAILABLE)) { //Needs update
                MessageSender.sendMsg(player, "&bAn update is available! Type &a/econ update&b to download.");
            } else if (plugin.getUpdateResult().equals(Updater.UpdateResult.SUCCESS)) {
                MessageSender.sendMsg(player, "&bAn update has been downloaded! Restart the server to activate.");
            }
        }
    }
}
