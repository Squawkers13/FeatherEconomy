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
package net.pekkit.feathereconomy.vault;

import java.util.List;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import net.pekkit.feathereconomy.FeatherEconomy;
import org.bukkit.OfflinePlayer;

public class Economy_FeatherEcon extends AbstractEconomy {

    private final FeatherEconomy plugin;

    public Economy_FeatherEcon(FeatherEconomy pl) {
        plugin = pl;
    }

    /**
     * Checks if economy method is enabled.
     *
     * @return Success or Failure
     */
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    /**
     * Gets name of economy method
     *
     * @return Name of Economy Method
     */
    public String getName() {
        return plugin.getName();
    }

    /**
     * Returns true if the given implementation supports banks.
     *
     * @return true if the implementation supports banks
     */
    public boolean hasBankSupport() {
        return false;
    }

    /**
     * Some economy plugins round off after a certain number of digits. This
     * function returns the number of digits the plugin keeps or -1 if no
     * rounding occurs.
     *
     * @return number of digits after the decimal point kept
     */
    public int fractionalDigits() {
        return 0;
    }

    /**
     * Format amount into a human readable String This provides translation into
     * economy specific formatting to improve consistency between plugins.
     *
     * @param amount to format
     * @return Human readable string describing amount
     */
    public String format(double amount) {
        int i = (int) amount;
        return i + plugin.getAPI().getCurrencyName(i);
    }

    /**
     * Returns the name of the currency in plural form. If the economy being
     * used does not support currency names then an empty string will be
     * returned.
     *
     * @return name of the currency (plural)
     */
    public String currencyNamePlural() {
        return plugin.getAPI().getCurrencyName(true);
    }

    /**
     * Returns the name of the currency in singular form. If the economy being
     * used does not support currency names then an empty string will be
     * returned.
     *
     * @return name of the currency (singular)
     */
    public String currencyNameSingular() {
        return plugin.getAPI().getCurrencyName(false);
    }

    /**
     *
     * @deprecated As of VaultAPI 1.4 use {@link #hasAccount(OfflinePlayer)}
     * instead.
     */
    @Deprecated
    public boolean hasAccount(String playerName) {
        return hasAccount(plugin.getServer().getOfflinePlayer(playerName));
    }

    /**
     * Checks if this player has an account on the server yet This will always
     * return true if the player has joined the server at least once as all
     * major economy plugins auto-generate a player account when the player
     * joins the server
     *
     * @param player to check
     * @return if the player has an account
     */
    public boolean hasAccount(OfflinePlayer player) {
        return plugin.getAPI().hasBalance(player.getUniqueId());
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {@link #hasAccount(OfflinePlayer, String)} instead.
     */
    @Deprecated
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(plugin.getServer().getOfflinePlayer(playerName), worldName);
    }

    /**
     * Checks if this player has an account on the server yet on the given world
     * This will always return true if the player has joined the server at least
     * once as all major economy plugins auto-generate a player account when the
     * player joins the server
     *
     * @param player to check in the world
     * @param worldName world-specific account
     * @return if the player has an account
     */
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return plugin.getAPI().hasBalance(player.getUniqueId());
    }

    /**
     * @deprecated As of VaultAPI 1.4 use {@link #getBalance(OfflinePlayer)}
     * instead.
     */
    @Deprecated
    public double getBalance(String playerName) {
        return getBalance(plugin.getServer().getOfflinePlayer(playerName));
    }

    /**
     * Gets balance of a player
     *
     * @param player of the player
     * @return Amount currently held in players account
     */
    public double getBalance(OfflinePlayer player) {
        return plugin.getAPI().getBalance(player.getUniqueId());
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {@link #getBalance(OfflinePlayer, String)} instead.
     */
    @Deprecated
    public double getBalance(String playerName, String world) {
        return getBalance(plugin.getServer().getOfflinePlayer(playerName), world);
    }

    /**
     * Gets balance of a player on the specified world. IMPLEMENTATION SPECIFIC
     * - if an economy plugin does not support this the global balance will be
     * returned.
     *
     * @param player to check
     * @param world name of the world
     * @return Amount currently held in players account
     */
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    /**
     * @deprecated As of VaultAPI 1.4 use {@link #has(OfflinePlayer, double)}
     * instead.
     */
    @Deprecated
    public boolean has(String playerName, double amount) {
        return has(plugin.getServer().getOfflinePlayer(playerName), amount);
    }

    /**
     * Checks if the player account has the amount - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to check
     * @param amount to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    public boolean has(OfflinePlayer player, double amount) {
        return plugin.getAPI().canAffordWithdrawal(player.getUniqueId(), (int) amount);
    }

    /**
     * @deprecated As of VaultAPI 1.4 use @{link
     * {@link #has(OfflinePlayer, String, double)} instead.
     */
    @Deprecated
    public boolean has(String playerName, String worldName, double amount) {
        return has(plugin.getServer().getOfflinePlayer(playerName), worldName, amount);
    }

    /**
     * Checks if the player account has the amount in a given world - DO NOT USE
     * NEGATIVE AMOUNTS IMPLEMENTATION SPECIFIC - if an economy plugin does not
     * support this the global balance will be returned.
     *
     * @param player to check
     * @param worldName to check with
     * @param amount to check for
     * @return True if <b>player</b> has <b>amount</b>, False else wise
     */
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {@link #withdrawPlayer(OfflinePlayer, double)} instead.
     */
    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(plugin.getServer().getOfflinePlayer(playerName), amount);
    }

    /**
     * Withdraw an amount from a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to withdraw from
     * @param amount Amount to withdraw
     * @return Detailed response of transaction
     */
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        plugin.getAPI().withdrawPlayer(player.getUniqueId(), (int) amount);
        return new EconomyResponse(amount, getBalance(player), ResponseType.SUCCESS, null);
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {@link #withdrawPlayer(OfflinePlayer, String, double)} instead.
     */
    @Deprecated
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(plugin.getServer().getOfflinePlayer(playerName), worldName, amount);
    }

    /**
     * Withdraw an amount from a player on a given world - DO NOT USE NEGATIVE
     * AMOUNTS IMPLEMENTATION SPECIFIC - if an economy plugin does not support
     * this the global balance will be returned.
     *
     * @param player to withdraw from
     * @param worldName - name of the world
     * @param amount Amount to withdraw
     * @return Detailed response of transaction
     */
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {@link #depositPlayer(OfflinePlayer, double)} instead.
     */
    @Deprecated
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(plugin.getServer().getOfflinePlayer(playerName), amount);
    }

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param player to deposit to
     * @param amount Amount to deposit
     * @return Detailed response of transaction
     */
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        plugin.getAPI().depositPlayer(player.getUniqueId(), (int) amount);
        return new EconomyResponse(amount, getBalance(player), ResponseType.SUCCESS, null);
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {@link #depositPlayer(OfflinePlayer, String, double)} instead.
     */
    @Deprecated
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(plugin.getServer().getOfflinePlayer(playerName), worldName, amount);
    }

    /**
     * Deposit an amount to a player - DO NOT USE NEGATIVE AMOUNTS
     * IMPLEMENTATION SPECIFIC - if an economy plugin does not support this the
     * global balance will be returned.
     *
     * @param player to deposit to
     * @param worldName name of the world
     * @param amount Amount to deposit
     * @return Detailed response of transaction
     */
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {{@link #createBank(String, OfflinePlayer)} instead.
     */
    @Deprecated
    public EconomyResponse createBank(String name, String player) {
        return createBank(name, plugin.getServer().getOfflinePlayer(player));
    }

    /**
     * Creates a bank account with the specified name and the player as the
     * owner
     *
     * @param name of account
     * @param player the account should be linked to
     * @return EconomyResponse Object
     */
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, null);
    }

    /**
     * Deletes a bank account with the specified name.
     *
     * @param name of the back to delete
     * @return if the operation completed successfully
     */
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, null);
    }

    /**
     * Returns the amount the bank has
     *
     * @param name of the account
     * @return EconomyResponse Object
     */
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, null);
    }

    /**
     * Returns true or false whether the bank has the amount specified - DO NOT
     * USE NEGATIVE AMOUNTS
     *
     * @param name of the account
     * @param amount to check for
     * @return EconomyResponse Object
     */
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, null);
    }

    /**
     * Withdraw an amount from a bank account - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name of the account
     * @param amount to withdraw
     * @return EconomyResponse Object
     */
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, null);
    }

    /**
     * Deposit an amount into a bank account - DO NOT USE NEGATIVE AMOUNTS
     *
     * @param name of the account
     * @param amount to deposit
     * @return EconomyResponse Object
     */
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, null);
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {{@link #isBankOwner(String, OfflinePlayer)} instead.
     */
    @Deprecated
    public EconomyResponse isBankOwner(String name, String playerName) {
        return isBankOwner(name, plugin.getServer().getOfflinePlayer(playerName));
    }

    /**
     * Check if a player is the owner of a bank account
     *
     * @param name of the account
     * @param player to check for ownership
     * @return EconomyResponse Object
     */
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, null);
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {{@link #isBankMember(String, OfflinePlayer)} instead.
     */
    @Deprecated
    public EconomyResponse isBankMember(String name, String playerName) {
        return isBankMember(name, plugin.getServer().getOfflinePlayer(playerName));
    }

    /**
     * Check if the player is a member of the bank account
     *
     * @param name of the account
     * @param player to check membership
     * @return EconomyResponse Object
     */
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, null);
    }

    /**
     * Gets the list of banks
     *
     * @return the List of Banks
     */
    public List<String> getBanks() {
        return null;
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {{@link #createPlayerAccount(OfflinePlayer)} instead.
     */
    @Deprecated
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(plugin.getServer().getOfflinePlayer(playerName));
    }

    /**
     * Attempts to create a player account for the given player
     *
     * @param player OfflinePlayer
     * @return if the account creation was successful
     */
    public boolean createPlayerAccount(OfflinePlayer player) {
        return true;
    }

    /**
     * @deprecated As of VaultAPI 1.4 use
     * {{@link #createPlayerAccount(OfflinePlayer, String)} instead.
     */
    @Deprecated
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(plugin.getServer().getOfflinePlayer(playerName), worldName);
    }

    /**
     * Attempts to create a player account for the given player on the specified
     * world IMPLEMENTATION SPECIFIC - if an economy plugin does not support
     * this the global balance will be returned.
     *
     * @param player OfflinePlayer
     * @param worldName String name of the world
     * @return if the account creation was successful
     */
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
}
