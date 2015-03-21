package vg.civcraft.mc.civmenu;

import java.io.File;
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

    private static Manager manager;

    @Override
    public void onEnable() {

        //Load in pretty item names
        toConsole("Loading materials.csv");
        this.saveResource("materials.csv", true);
        Utility.loadPrettyNames(new File(getDataFolder() + "/materials.csv"));

        manager = new Manager(this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return manager.handelCommand(sender, cmd, label, args);
    }

    /**
     * Used to generate Menu objects
     *
     * @return A new menu
     */
    public static Menu newMenu() {
        return new Menu(manager);
    }

    /**
     * Used to generate a Menu with the first entry already containing text
     *
     * @param text Text of the first entry
     * @return Menu containing a single entry with Text
     */
    public static Menu newMenu(String text) {
        Menu menu = new Menu(manager);
        menu.addEntry(text);
        return menu;
    }

    /**
     * Sends a message to the console
     *
     * @param message The message
     */
    static void toConsole(String message) {
        Bukkit.getLogger().info("[CivMenu] " + message);
    }
}
