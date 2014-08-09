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
package net.pekkit.feathereconomy.locale;

import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.translateAlternateColorCodes;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;

/**
 *
 * @author Squawkers13
 */
public class MessageSender {
    
    private static final String prefix = "&f[&bFeatherEconomy&f] ";

    /**
     * Plugin method to easily send messages to players (and console too). This
     * is the core of all the sender methods included here.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg The message to be sent.
     * @param pagination Whether to paginate the message or not.
     *
     * @since 1.0
     */
    public static void sendMsg(CommandSender sender, String msg, boolean pagination) {
        String message = translateAlternateColorCodes('&', msg);
        if (message.length() > ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH && pagination) {
            String[] multiline = ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
            sender.sendMessage(multiline);
        } else {
            sender.sendMessage(message);
        }
    }

    /**
     * Plugin method to easily send messages to players (and console too). This
     * version assumes you want to paginate the message.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg The message to be sent.
     *
     * @since 1.0
     */
    public static void sendMsg(CommandSender sender, String msg) {
        sendMsg(sender, msg, true);
    }

    /**
     * Plugin method to easily send messages prefixed with the plugin name.
     * Prefixes the message with the plugin name and calls sendMsg.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg The message to be sent.
     * @param pagination Whether to paginate the message or not.
     *
     * @see sendMsg
     *
     * @since 1.0
     */
    public static void sendPluginMsg(CommandSender sender, String msg, boolean pagination) {
        sendMsg(sender, prefix + msg, pagination);
    }

    /**
     * Plugin method to easily log messages to the console. Actually just calls
     * sendPluginMsg and specifies the sender as the server's
     * ConsoleCommandSender.
     *
     * @param msg The message to be sent to the console.
     *
     * @see sendPluginMsg
     *
     * @since 1.0
     */
    public static void log(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(translateAlternateColorCodes('&', prefix + msg));
    }

    /**
     * Plugin method to easily log caught exceptions to the console. 
     * 
     *
     * @param msg Message to log
     * @param ex The Exception to log a stacktrace for.
     *
     * @see log
     *
     * @since 1.0
     */
    public static void logStackTrace(String msg, Exception ex) {        
        log("&4An error occured: &f" + msg);
        log(ex.getClass().getName());
        for (StackTraceElement ee : ex.getStackTrace()) {
            log("   at " + ee.getClassName() + ":" + ee.getMethodName() + " (line " + ee.getLineNumber() + ")");
        }
    }

}
