package com.untamedears.civmenu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CivMenuManager {

    Plugin plugin;
    Map<String, ActiveMenu> actives;

    //Duration to have menu active
    static int DURATION = 60;
    static String COMMMAND_NAME = "civmenu";

    CivMenuManager(Plugin plugin) {
        this.plugin = plugin;
        this.actives = new HashMap<String, ActiveMenu>();
    }

    public boolean handelCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equals(COMMMAND_NAME)) {
            return true;
        }
        if (args.length < 2) {
            return false;
        }
        String ID = args[0];
        if (!actives.containsKey(ID)) {
            return false;
        }
        int index;
        try {
            index = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        ActiveMenu active = actives.get(ID);
        actives.remove(ID);
        return active.execute(sender, index);
    }

    String getID() {
        String ID = UUID.randomUUID().toString();
        if (!actives.containsKey(ID)) {
            return ID;
        } else {
            return getID();
        }
    }

    void deactivate(String ID) {
        actives.remove(ID);
    }

    public void registerActive(final String ID, ActiveMenu active) {
        actives.put(ID, active);
        new BukkitRunnable() {
            String newID = ID;

            @Override
            public void run() {
                deactivate(ID);
            }

        }.runTaskLater(this.plugin, 20 * DURATION);
    }
}
