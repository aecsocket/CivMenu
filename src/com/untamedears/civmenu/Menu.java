package com.untamedears.civmenu;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class Menu {

    List<MenuItem> items;
    CivMenuManager manager;

    public Menu(CivMenuManager manager) {
        this.manager = manager;
        this.items = new ArrayList<MenuItem>();
    }

    /**
     * Constructs the String
     *
     * @return
     */
    ActiveMenu getActiveMenu(Player player) {
        ActiveMenu active = new ActiveMenu(player);
        String ID = manager.getID();
        String message = "[";
        int index=0;
        for(MenuItem item:items){
            List<JSONObject> itemJSON;
            
            if (item.isClickable()) {
                active.addCommand(item.getCommand(),item.getArgs());
                itemJSON = item.toJSON(ID + " " + index);
                index++;
            }
            else {
                itemJSON=item.toJSON();
            }
            for (JSONObject jsonObject : itemJSON) {
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

    public void send(Player player) {
        ActiveMenu active = getActiveMenu(player);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(active.message)));
    }
}
