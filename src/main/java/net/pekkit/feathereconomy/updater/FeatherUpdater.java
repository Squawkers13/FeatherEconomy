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

package net.pekkit.feathereconomy.updater;

import java.io.File;
import net.gravitydevelopment.updater.Updater;
import net.pekkit.feathereconomy.util.Version;
import org.bukkit.plugin.Plugin;

/**
 * 
 * @author Squawkers13
 */
public class FeatherUpdater extends Updater {

    /**
     *
     * @param plugin
     * @param id
     * @param file
     * @param type
     * @param announce
     */
    public FeatherUpdater(Plugin plugin, int id, File file, UpdateType type, boolean announce) {
        super(plugin, id, file, type, announce);
    }

    /**
     *
     * @param localVersion
     * @param remoteVersion
     * @return
     */
    @Override
    public boolean shouldUpdate(String localVersion, String remoteVersion) {
        Version local = new Version(localVersion);
        Version remote = new Version(remoteVersion);

        return local.compareTo(remote) == -1;
    }
    
    
    
}
