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
