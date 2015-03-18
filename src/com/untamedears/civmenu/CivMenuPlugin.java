package com.untamedears.civmenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class CivMenuPlugin extends JavaPlugin {

    CivMenuManager manager;

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

    /**
     * Sends a message to the console
     *
     * @param message The message
     */
    public static void toConsole(String message) {
        Bukkit.getLogger().info("[CivMenu] " + message);
    }
}
