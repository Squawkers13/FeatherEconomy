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
package net.pekkit.feathereconomy.api;

import java.util.UUID;
import net.pekkit.feathereconomy.FeatherEconomy;
import net.pekkit.feathereconomy.data.EDataHandler;

/**
 * FeatherAPI- Developer API for the plugin
 * 
 * @author Squawkers13
 */
public class FeatherAPI {

    private final FeatherEconomy plugin;
    private final EDataHandler edh;

    /**
     *
     * @param par1
     * @param par2
     */
    public FeatherAPI(FeatherEconomy par1, EDataHandler par2) {
        plugin = par1;
        edh = par2;
    }

    /** Checks whether the player has a balance or not.
     * You probably won't need this method.
     *
     * @param uuid The UUID of the player to check the balance of
     * @return Whether the player has a balance or not
     */
    public boolean hasBalance(UUID uuid) {
        return edh.hasBalance(uuid);
    }

    /**  Fetches a player's balance.
     * Will return 0 if they don't have a balance.
     *
     * @param uuid The UUID of the player to get the balance of
     * @return The player's balance
     */
    public int getBalance(UUID uuid) {
        return edh.getBalance(uuid);
    }
    
    /** Set's a player's balance to the specified integer.
     * NOTE: If you're adding to the balance, subtracting from the balance, 
     * or paying another player with the funds from this balance, please use one of
     * the convenience methods below. 
     * 
     * @param uuid The UUID of the player  to set the balance of
     * @param balance The integer to set their balance to
     */
    public void setBalance(UUID uuid, int balance) {
        edh.setBalance(uuid, balance);
    }
 
    /** Checks if a player can afford a withdrawal of the specified amount.
     * Run this before doing any withdrawals!
     *
     * @param uuid The UUID of the player you plan to withdraw from
     * @param amount The amount you plan to withdraw
     * @return Whether the player can afford the withdrawal
     */
    public boolean canAffordWithdrawal(UUID uuid, int amount) {
        int balance = edh.getBalance(uuid);
        return balance - amount >= 0;
    }

    /** Withdraws the specified amount from a player's balance.
     * WILL THROW AN EXCEPTION IF YOU ATTEMPT TO WITHDRAW MORE
     * THAN THE PLAYER HAS!
     *
     * @param uuid The UUID of the player to withdraw from
     * @param amount The amount to withdraw
     */
    public void withdrawPlayer(UUID uuid, int amount) {
        if (!canAffordWithdrawal(uuid, amount)) {
            throw new IllegalStateException("Attempted to withdraw more than the player had");
        }
        edh.setBalance(uuid, edh.getBalance(uuid) - amount);
    }

    /** Deposits the specified amount into a player's balance.
     *
     * @param uuid The UUID of the player  to deposit to
     * @param amount The amount to deposit
     */
    public void depositPlayer(UUID uuid, int amount) {
        edh.setBalance(uuid, edh.getBalance(uuid) + amount);
    }

    /** Pays a player the specified amount from another player's balance.
     * WILL THROW AN EXCEPTION IF YOU ATTEMPT TO PAY MORE
     * THAN THE PLAYER HAS!
     * 
     * @param sender The player to withdraw from
     * @param reciever The player to pay with the withdrawn funds
     * @param amount The amount to pay
     */
    public void payPlayer(UUID sender, UUID reciever, int amount) {
        withdrawPlayer(sender, amount);
        depositPlayer(reciever, amount);
    }
    
    /** Fetches the name of the server's currency.
     *
     * @param plural Whether to fetch the plural version or not
     * @return The name of the server's currency
     */
    public String getCurrencyName(boolean plural) {
        if (plural) {
            return plugin.getConfig().getString("settings.economy.currency-name-plural");
        } else {
            return plugin.getConfig().getString("settings.economy.currency-name");
        }
    }
    
    /** Fetches the name of the server's currency.
     * This method checks a player's balance and returns
     * the appropriate version.
     * 
     * @param uuid The player to check the balance of
     * @return The name of the server's currency
     */
    public String getCurrencyName(UUID uuid) {
        if (getBalance(uuid) == 1) {
            return getCurrencyName(false);
        } else {
            return getCurrencyName(true);
        }
    }

}
