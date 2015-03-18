package com.untamedears.civmenu;

import com.mojang.authlib.yggdrasil.response.Response;
import static java.lang.System.getProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

public class MenuItem {

    List<Object> text;
    List<Object> hover;
    MenuCommand command;
    String[] args;

    public MenuItem(List<Object> text) {
        this.text = text;
    }

    public MenuItem(List<Object> text, List<Object> hover, MenuCommand command,String[] args) {
        this(text);
        this.hover = hover;
        this.command = command;
        this.args = args;
    }

    public boolean isClickable() {
        return command != null;
    }
    /**
     * Generates a List of JSON objects representing this text
     * 
     * Of the following format:
     * If there is not command
     *   (yellow)text(end yellow)
     * If there is a command
     *   {yellow)[(end yellow)text(yellow)[(end yellow)
     * @return List of JSONObjects representing output
     */
    public List<JSONObject> toJSON(String ID) {
        List<JSONObject> json = new ArrayList<JSONObject>();
        JSONObject content = new JSONObject();

        throw new UnsupportedOperationException();
    }
    
    public List<JSONObject> toJSON() {
        return toJSON("");
    }
    
    public MenuCommand getCommand(){
        return command;
    }
    public String[] getArgs() {
        return args;
    }

    public static Map<ItemStack, String> prettyNames = new HashMap<ItemStack, String>();

    /**
     * Convert a ItemSet to a pretty string
     *
     * If the item has a custom DisplayName it uses that, then if it has an
     * entry in the pretty name lookup table use that, otherwise use bukkit name
     *
     * @param itemStacks ItemStakcs to convert to string
     * @return Pretty String representing ItemStacks
     */
    public static String PrettyItem(Set<ItemStack> itemStacks) {
        String output = "";
        for (ItemStack itemStack : itemStacks) {
            ItemStack key = itemStack.clone();
            key.setAmount(1);

            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
                output += itemStack.getAmount() + " " + itemStack.getItemMeta().getDisplayName() + ",";
            } else {
                if (prettyNames.containsKey(key)) {
                    output += itemStack.getAmount() + " " + prettyNames.get(key) + ",";
                } else {
                    output += itemStack.getAmount() + " " + itemStack.getType().name() + ":" + itemStack.getDurability() + ",";
                }
            }
        }
        //Remove trailing ","
        if (output.length() != 0) {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }
}
