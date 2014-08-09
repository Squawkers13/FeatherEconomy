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
package net.pekkit.feathereconomy.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.pekkit.feathereconomy.FeatherEconomy;
import net.pekkit.feathereconomy.locale.MessageSender;

/**
 * Database Manager- Manages the database used to store economy data.
 *
 * @author Squawkers13
 */
public class DatabaseManager {

    private final FeatherEconomy plugin;

    private String dbPath;

    private Connection connection;
    private Statement statement;
    private ResultSet results;

    /**
     *
     * @param pl
     */
    public DatabaseManager(FeatherEconomy pl) {
        plugin = pl;
    }

    /**
     * Initializes the database.
     *
     */
    protected void initDB() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            MessageSender.logStackTrace("Could not find driver!", e);
        }

        try {
            dbPath = "jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "FeatherEconomy.db";
            connection = DriverManager.getConnection(dbPath);
        } catch (SQLException ex) {
            MessageSender.logStackTrace("Could not create connection!", ex);
        }

    }

    /**
     * Creates the default tables.
     */
    protected void createTables() {
        update("CREATE TABLE IF NOT EXISTS Econ (UUID TEXT UNIQUE NOT NULL PRIMARY KEY, balance FLOAT DEFAULT 0.0);");

    }

    /**
     * Sends a query to the database and returns the results.
     *
     * @param query
     * @param row
     * @return results of query
     * @throws java.sql.SQLException
     */
    protected int query(String query, String row) throws SQLException {
        statement = connection.createStatement();

        results = statement.executeQuery(query);

        int var = 0;
        while (results.next()) {
            var = results.getInt(row);
        }

        statement.close();
        results.close();

        return var;
    }

    /**
     * Sends a query to the database. Must be a INSERT, UPDATE, or DELETE query!
     *
     * @param query
     */
    protected void update(String query) {
        try {
            statement = connection.createStatement();

            statement.executeUpdate(query);

            statement.close();
        } catch (SQLException ex) {
            MessageSender.logStackTrace("Exception with the query!", ex);
        }
    }

    /**
     *
     * @param player
     * @param table
     * @return whether the query was null or not
     */
    protected boolean checkNull(String player, String table) {
        try {
            statement = connection.createStatement();

            results = statement.executeQuery("SELECT * FROM " + table + " WHERE UUID='" + player + "';");

            String UUID = null;
            while (results.next()) {
                UUID = results.getString("UUID");
            }

            statement.close();
            results.close();

            return UUID == null;
        } catch (SQLException ex) {
            //MessageSender.logStackTrace("Exception with the query!", ex);
            return true;
        }
    }

}
