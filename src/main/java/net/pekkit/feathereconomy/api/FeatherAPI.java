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
        return getCurrencyName(getBalance(uuid));
    }
    
    /** Fetches the name of the server's currency.
     * This method returns the appropriate form based
     * on the specified balance.
     * 
     * @param i The balance to 
     * @return The name of the server's currency
     */
    public String getCurrencyName(int i) {
        if (i == 1) {
            return getCurrencyName(false);
        } else {
            return getCurrencyName(true);
        }
    }

}
