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
