package com.untamedears.civmenu;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class Menu {

    List<Entry> entrys;
    CivMenuManager manager;

    public Menu(CivMenuManager manager) {
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
        for (Entry entry : entrys) {
            List<JSONObject> entryJSON;
            //Registers the command with the active menu
            if (entry.isClickable()) {
                active.addCommand(entry.getCommand(), entry.getArgs());
                entryJSON = entry.toJSON("/" + ID + " " + index);
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

    /**
     * Sends an ActiveMenu of this Menu to the Player
     *
     * @param player Player to send menu to
     */
    public void send(Player player) {
        ActiveMenu active = getActiveMenu(player);
        manager.registerActive(active.ID, active);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(active.message)));
    }

    public Menu addEntry(String text) {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(text);
        return addEntry(list);
    }
    
    public Menu addEntry(List<Object> text) {
        return addEntry(new Entry(text));
    }
    
    /**
     * Adds an entry to the menu
     *
     * @param entry Entry to add to menu
     * @return This menu
     */
    public Menu addEntry(Entry entry) {
        entrys.add(entry);
        return this;
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

    /**
     * Sets the text of the last entry in the menu
     *
     * @param text Text to set of Entry
     * @return This menu
     */
    public Menu setText(List<Object> text) {
        entrys.get(entrys.size() - 1).setText(text);
        return this;
    }

    /**
     * Sets the hover of the last Entry in the Menu
     *
     * @param hover Hover to set of Entry
     * @return This menu
     */
    public Menu setHover(List<Object> hover) {
        entrys.get(entrys.size() - 1).setHover(hover);
        return this;
    }

    /**
     * Sets the Insertion of the last Entry in the Menu
     *
     * @param insertion
     * @return This Menu
     */
    public Menu setInsertion(String insertion) {
        entrys.get(entrys.size() - 1).setInsertion(insertion);
        return this;

    }

    /**
     * Sets the command of the last Entry
     *
     * @param command Command to set
     * @return This Menu
     */
    public Menu setCommand(MenuCommand command) {
        entrys.get(entrys.size() - 1).setCommand(command);
        return this;
    }

    /**
     * Sets the command and args of the last Entry
     *
     * @param command Command to set
     * @param args    Args to set
     * @return This menu
     */
    public Menu setCommand(MenuCommand command, String[] args) {
        entrys.get(entrys.size() - 1).setCommand(command, args);
        return this;
    }

}
