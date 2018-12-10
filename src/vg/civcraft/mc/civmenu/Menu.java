package vg.civcraft.mc.civmenu;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

/**
 * The Menu class stores a collection of functionalized text Entrys which are
 * displayed simultaneously to the user. Additionally the menu class provides
 * the public interface for displaying the Menu to a player.
 *
 * Methods for adding Entrys to a Menu and altering the most most recently added
 * Entry all return the Menu item, allowing for easy chained construction. For
 * example:
 * <pre>
 * new Menu()
 *   .addEntry("Header Text\n")
 *   .addEntry("Button Text").setCommand(Command)
 *   .addEntry("Tooltip").setHover("Tooltip Text")
 *   .addEntry("Help Text").setInsertion("Insertion Text");
 * Menu.send(player);
 * </pre>
 */
public class Menu implements MenuCommand {

    List<Entry> entrys;
    Manager manager;

    public Menu(Manager manager) {
        this.manager = manager;
        this.entrys = new ArrayList<Entry>();
    }

    /**
     * Constructs the String
     *
     * @return
     */
    ActiveMenu getActiveMenu(Player player) {
        String ID = manager.getID();
        ActiveMenu active = new ActiveMenu(player, ID);
        String message = "[";
        //Iterate through items adding them to the JSON String
        int index = 0;
        //Add divider
        message += new JSONObject().put("color", "yellow")
                .put("text", "_____________________________________________________\n")
                .toString() + ",";
        //Add Entrys
        for (Entry entry : entrys) {
            List<JSONObject> entryJSON;
            //Registers the command with the active menu
            if (entry.hasCommand()) {
                active.addCommand(entry.getCommand(), entry.getArgs());
                entryJSON = entry.toJSON("/" + Manager.COMMMAND_NAME + " " + ID + " " + index);
                index++;
            } else {
                entryJSON = entry.toJSON();
            }
            for (JSONObject jsonObject : entryJSON) {
                message += jsonObject.toString() + ",";
            }
        }
        //Remove last ,
        if (message.length() != 0) {
            message = message.substring(0, message.length() - 1);
        }
        message += "]";
        active.setMessage(message);
        return active;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        CivMenu.toConsole(Boolean.toString(sender instanceof Player));
        if (sender instanceof Player) {
            send((Player) sender);
            return true;
        }
        return false;
    }

    /**
     * Sends an ActiveMenu of this Menu to the Player
     *
     * @param player Player to send menu to
     */
    public void send(Player player) {
        ActiveMenu active = getActiveMenu(player);
        manager.registerActive(active.ID, active);
        CivMenu.toConsole(active.message);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(active.message)));
    }

    /**
     * Adds a new Entry to the menu with defined Text.
     *
     * The text is given as an array of Objects to enable built in parsing by
     * the entry class of objects such as ItemStack, Set{@literal <ItemStack\>},
     * or just String.
     *
     * @param text Text of the Entry in a list form
     * @return This Menu
     */
    public Entry addEntry(Object... text) {
        return addEntry(new Entry(text));
    }

    /**
     * Adds an entry to the menu
     *
     * @param entry Entry to add to menu
     * @return This menu
     */
    public Entry addEntry(Entry entry) {
        entrys.add(entry);
        return entry;
    }

    /**
     * Gets Entry at index
     *
     * @param index Index of Entry
     * @return Entry at Index
     */
    public Entry getEntry(int index) {
        return entrys.get(index);
    }
}
