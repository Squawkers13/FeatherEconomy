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
package net.pekkit.feathereconomy.listeners;

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
    }
}
