package com.untamedears.civmenu;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This Plugin provides and easy way to create a chat based menu
 *
 * This plugin is meant to be an easy way to create standardized menus in
 * minecraft chat. It serves allows easy but restricted access to the features
 * available in the minecraft chat window. Uses could include creating menus to
 * execute preexisting chat commands, to suggest partial chat commands, to
 * execute specific methods in a plugin nested menus with mouseover to display
 * large amounts of information about a plugin. All of these menus are presented
 * with consistent formatting between plugins, decreasing the customization, but
 * simplifying the process and providing a continous user experience between
 * plugins.
 */
public class CivMenu extends JavaPlugin {

    private static CivMenuManager manager;

    @Override
    public void onEnable() {
        manager = new CivMenuManager(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return manager.handelCommand(sender, cmd, label, args);
    }

    public static Menu newMenu() {
        return new Menu(manager);
    }
    
    /**
     * Sends a message to the console
     *
     * @param message The message
     */
    public static void toConsole(String message) {
        Bukkit.getLogger().info("[CivMenu] " + message);
    }
}
