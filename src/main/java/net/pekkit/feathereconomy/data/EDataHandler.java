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

import java.sql.SQLException;
import java.util.UUID;
import net.pekkit.feathereconomy.FeatherEconomy;

/**
 *
 * @author Squawkers13
 */
public class EDataHandler {

    private final FeatherEconomy plugin;
    private final DatabaseManager dm;

    /**
     *
     * @param par1
     * @param par2
     */
    public EDataHandler(FeatherEconomy par1, DatabaseManager par2) {
        plugin = par1;
        dm = par2;

        dm.initDB();
        dm.createTables();

    }

    /**
     *
     * @param uuid
     * @return
     */
    public boolean hasBalance(UUID uuid) {
        return !dm.checkNull(uuid.toString(), "Econ");
    }

    /**
     *
     * @param uuid
     * @return
     */
    public int getBalance(UUID uuid) {
        int count;
        try {
            count = dm.query("SELECT * FROM Econ WHERE UUID='" + uuid.toString() + "';", "balance");
        } catch (SQLException ex) {
            return 0;
        }

        return count;
    }

    /**
     *
     * @param uuid
     * @param balance
     */
    public void setBalance(UUID uuid, int balance) {
        if (dm.checkNull(uuid.toString(), "Econ")) {
            // Time to create a new row
            dm.update("INSERT INTO Econ (UUID, balance) VALUES ('" + uuid.toString() + "','" + balance + "'); ");
        } else {
            // Row already exists
            dm.update("UPDATE Econ SET balance='" + balance + "' WHERE UUID='" + uuid.toString() + "'; ");
        }
    }
}
