// $Id$
/*
 * WorldGuard
 * Copyright (C) 2010 sk89q <http://www.sk89q.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.net.URL;
import java.io.*;

/**
 * Entry point for the plugin for hey0's mod.
 *
 * @author sk89q
 */
public class WorldGuard extends Plugin {
    /**
     * Logger.
     */
    private static final Logger logger = Logger.getLogger("Minecraft.WorldGuard");
    /**
     * Listener for the plugin system.
     */
    private WorldGuardListener listener;

    /**
     * Initialize the plugin.
     */
    public WorldGuard() {
        listener = new WorldGuardListener(this);
    }

    /**
     * Initializes the plugin.
     */
    @Override
    public void initialize() {
        PluginLoader loader = etc.getLoader();

        loader.addListener(PluginLoader.Hook.COMMAND, listener, this,
                PluginListener.Priority.MEDIUM);
        loader.addListener(PluginLoader.Hook.SERVERCOMMAND, listener, this,
                PluginListener.Priority.MEDIUM);
        loader.addListener(PluginLoader.Hook.EXPLODE, listener, this,
                PluginListener.Priority.HIGH);
        loader.addListener(PluginLoader.Hook.IGNITE, listener, this,
                PluginListener.Priority.HIGH);
        loader.addListener(PluginLoader.Hook.FLOW, listener, this,
                PluginListener.Priority.HIGH);
        loader.addListener(PluginLoader.Hook.LOGINCHECK, listener, this,
                PluginListener.Priority.HIGH);
        loader.addListener(PluginLoader.Hook.LOGIN, listener, this,
                PluginListener.Priority.MEDIUM);
        loader.addListener(PluginLoader.Hook.BLOCK_CREATED, listener, this,
                PluginListener.Priority.HIGH);
        loader.addListener(PluginLoader.Hook.BLOCK_DESTROYED, listener, this,
                PluginListener.Priority.CRITICAL);
        loader.addListener(PluginLoader.Hook.BLOCK_BROKEN, listener, this,
                PluginListener.Priority.HIGH);
        loader.addListener(PluginLoader.Hook.DISCONNECT, listener, this,
                PluginListener.Priority.HIGH);
        loader.addListener(PluginLoader.Hook.ITEM_DROP , listener, this,
                PluginListener.Priority.HIGH);
        loader.addListener(PluginLoader.Hook.COMPLEX_BLOCK_CHANGE, listener, this,
                PluginListener.Priority.HIGH);
        loader.addListener(PluginLoader.Hook.COMPLEX_BLOCK_SEND, listener, this,
                PluginListener.Priority.HIGH);
    }

    /**
     * Enables the plugin.
     */
    @Override
    public void enable() {
        logger.log(Level.INFO, "WorldGuard version " + getVersion() + " loaded");
        listener.loadConfiguration();

        etc.getInstance().addCommand("/stopfire", "Globally stop fire spread");
        etc.getInstance().addCommand("/allowfire", "Globally re-enable fire spread");
    }

    /**
     * Disables the plugin.
     */
    @Override
    public void disable() {
        try {
            listener.disable();
            BlacklistEntry.forgetAllPlayers();
        } catch (Throwable t) {
        }

        etc.getInstance().removeCommand("/stopfire");
        etc.getInstance().removeCommand("/allowfire");
    }

    /**
     * Get the WorldGuard version.
     *
     * @return
     */
    private String getVersion() {
        try {
            String classContainer = WorldGuard.class.getProtectionDomain()
                    .getCodeSource().getLocation().toString();
            URL manifestUrl = new URL("jar:" + classContainer + "!/META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(manifestUrl.openStream());
            Attributes attrib = manifest.getMainAttributes();
            String ver = (String)attrib.getValue("WorldGuard-Version");
            return ver != null ? ver : "(unavailable)";
        } catch (IOException e) {
            return "(unknown)";
        }
    }
}