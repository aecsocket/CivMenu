package vg.civcraft.mc.civmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Manager {

    Plugin plugin;
    Map<String, ActiveMenu> actives;

    //Duration to have menu active
    static int DURATION = 60;
    static String COMMMAND_NAME = "civmenu";

    Manager(Plugin plugin) {
        this.plugin = plugin;
        this.actives = new HashMap<String, ActiveMenu>();

    }

    boolean handelCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equals(COMMMAND_NAME)) {
            return true;
        }
        if (debug(sender, args[0])) {
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
        CivMenu.toConsole("Handeling command "+args.toString());
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

    void registerActive(final String ID, ActiveMenu active) {
        actives.put(ID, active);
        CivMenu.toConsole(actives.toString());
        new BukkitRunnable() {
            String newID = ID;

            @Override
            public void run() {
                deactivate(ID);
            }

        }.runTaskLater(this.plugin, 20 * DURATION);
    }

    boolean debug(CommandSender sender, String mode) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;
        switch (mode.toLowerCase()) {
            case "text":
                debugText(player);
                return true;
            case "fancytext":
                debugFancyText(player);
                return true;
            case "insertion":
                debugInsertion(player);
                return true;
            case "nested":
                debugNestedMenus(player);
                return true;
            case "hover":
                debugHover(player);
                return true;
            case "suggest":
                debugSuggest(player);
                return true;
            case "namelayer":
                namelayer(player);
                return true;
        }
        return false;
    }

    void debugText(Player player) {
        Menu menu = CivMenu.newMenu();
        menu.addEntry(new Entry("Entry created directly"));
        List list = new ArrayList();
        list.add("Text in List added directly to Entry");
        menu.addEntry(new Entry(list));
        List list2 = new ArrayList();
        list2.add("Text in List via menu");
        menu.addEntry(list2);
        menu.addEntry("Entry created from text via menu");
        menu.send(player);
    }

    void debugFancyText(Player player) {
        Menu menu = CivMenu.newMenu();
        List list = new ArrayList();
        list.add("ItemStack converted to text\n");
        list.add(new ItemStack(Material.CHEST));
        menu.addEntry(list);
        List list2 = new ArrayList();
        list2.add("Set<ItemStack> converted to text");
        Set set = new HashSet();
        set.add(new ItemStack(Material.CHEST, 2));
        set.add(new ItemStack(Material.STONE));
        list2.add(set);
        menu.addEntry(list2);
        menu.send(player);
    }

    void debugInsertion(Player player) {
        Menu menu = CivMenu.newMenu();
        menu.addEntry("Insertion Text: ");
        menu.addEntry("Shift Click Me").setInsertion("Inserted Text");
        menu.send(player);
    }
    
    void debugNestedMenus(Player player) {
        Menu menu = CivMenu.newMenu("Nested Menu test: ");
        Menu menu2 = CivMenu.newMenu("Second Menu");
        menu.addEntry("Second Menu").setCommand(menu2);
        menu.send(player);
    }
    
    void debugHover(Player player) {
        Menu menu = CivMenu.newMenu("Hover test\n");
        menu.addEntry("Hover Me!").setHover("Hover Text");
        menu.send(player);
    }
    
    void debugSuggest(Player player) {
        CivMenu.newMenu("Suggest text\n").addEntry("Suggestion").setSuggest("\\ctr").send(player);
    }
    
    void namelayer(Player player) {
        Menu menu = CivMenu.newMenu("Namelaye Help - Hover for info, click to use:\n");
        
        menu.addEntry("Create")
                .setHover("Creates a group, default is private unless public is specified\n/nlcg <group> [group-type] [password]")
                .setSuggest("/nlcg ");
        menu.addEntry("Delete")
                .setHover("Deletes a group\n/nldg <group>")
                .setSuggest("/nldg ");
        menu.addEntry("Invite")
                .setHover("Invite player to the group, default is member unless another rank is specified\n/nlip <group> <player-name> [rank]")
                .setSuggest("/nlip ");
        menu.addEntry("Revoke")
                .setHover("Revoke Player's invitation to a grou\n/nlri <group> <player-name>")
                .setSuggest("/nlr ");
        menu.addEntry("Join")
                .setHover("Join a password protected group\n/nljg <group> <password>")
                .setSuggest("/nljg ");
        menu.addEntry("Accept")
                .setHover("Accept the invite the group\n/nlag <group>")
                .setSuggest("/nlag ");
        menu.addEntry("Leave")
                .setHover("Leave a group group\n/nlleg <group>")
                .setSuggest("/nlleg ");
        menu.addEntry("List")
                .setHover("List groups\n/nllg [page]")
                .setSuggest("/nllg ");
        menu.addEntry("Types")
                .setHover("List group types (private and public)\n/nllgt")
                .setSuggest("/nllgt ");
        menu.addEntry("Invitations")
                .setHover("List all group invitations\n/nllci")
                .setSuggest("/nllci ");
        menu.addEntry("Promote")
                .setHover("Promote/Demote Player's rank within group\n/nlpp <group> <player> <rank>")
                .setSuggest("/nlpp ");
        menu.addEntry("Remove")
                .setHover("Remove a player from a group\n/nlrm <group> <member>")
                .setSuggest("/nlrm ");
        menu.addEntry("Transfer")
                .setHover("Transfer ownership of a group to a player\n/nltg <group> <player>")
                .setSuggest("/nltg ");
        menu.addEntry("Password")
                .setHover("Add a password to a group\n/nlsp <group> <password>")
                .setSuggest("/nlsp ");
        menu.addEntry("+Permission")
                .setHover("Grant a permission to a rank within a group\n/nlmp <group> <add> <Rank> <PermissionType>")
                .setSuggest("/nlmp ");
        menu.addEntry("-Permission")
                .setHover("Remove a permission from a rank within a group\n/nlmp <group> <remove> <Rank> <PermissionType>")
                .setSuggest("/nlmp ");
        menu.addEntry("Permissions")
                .setHover("List the permissions a rank has within a group\n/nllp <group> <Rank>")
                .setSuggest("/nllp ");
        menu.addEntry("Auto")
                .setHover("Toggle auto acceptance of invites\n/nltaai")
                .setSuggest("/nltaai ");
        menu.addEntry("Default")
                .setHover("Set or Change Default Group\n/nlsdg <group>")
                .setSuggest("/nlsdg ");
        menu.addEntry("Default?")
                .setHover("Returns current default group\n/nlgdg")
                .setSuggest("/nlgdg ");
        menu.send(player);
    }
}
