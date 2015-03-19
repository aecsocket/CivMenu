package com.untamedears.civmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Entry {

    List<Object> text;
    List<Object> hover;
    String insertion;
    MenuCommand command;
    String[] args;

    public Entry(List<Object> text) {
        this.text = text;
    }

    boolean isClickable() {
        return command != null || insertion != null;
    }

    /**
     * Generates a List of JSON objects representing this text
     *
     * Of the following format: If there is not command (yellow)text(end yellow)
     * If there is a command {yellow)[(end yellow)text(yellow)[(end yellow)
     * 
     * @param ID Text associated with running this command
     * @return List of JSONObjects representing output
     */
    public List<JSONObject> toJSON(String ID) {
        List<JSONObject> json = new ArrayList<JSONObject>();
        JSONObject content = new JSONObject().put("text", toString(text));
        content.put("color", isClickable() ? "white" : "yellow");
        //JSONObject content = new JSONObject();
        //content..put("color", "yellow");
        if (hover != null) {
            JSONObject hoverEvent = new JSONObject().put("action", "showText").put("value", toJSONArray(hover));
            content.put("hoverEvent", hoverEvent.toString());
        }
        if (command != null) {
            JSONObject clickEvent = new JSONObject().put("action", "run_command").put("value", "ID");
            content.put("clickEvent", clickEvent.toString());
        }
        if (insertion != null) {
            content.put("insertion", insertion);
        }

        json.add(content);
        if (isClickable()) {
            JSONObject leftBracket = new JSONObject().put("text", "[").put("color", "yellow");
            JSONObject rightBracket = new JSONObject().put("text", "]").put("color", "yellow");
            json.add(0, leftBracket);
            json.add(rightBracket);
        }
        return json;
    }

    public List<JSONObject> toJSON() {
        return toJSON("");
    }

    public Entry setText(List<Object> text) {
        this.text = text;
        return this;
    }

    public Entry setHover(List<Object> hover) {
        this.hover = hover;
        return this;
    }

    /**
     * Sets the insertion of this entry
     * @param insertion The insertion of this Entry
     * @return This Entry
     */
    public Entry setInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }
    
    public Entry setCommand(MenuCommand command) {
        return setCommand(command, new String[1]);
    }

    public Entry setCommand(MenuCommand command, String[] args) {
        this.command = command;
        this.args = args;
        return this;
    }

    public MenuCommand getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    /**
     * Takes a list of objects and parses them into a str to display
     *
     * @param objects objects to be parsed
     * @return String representing objects
     */
    private static String toString(List<Object> objects) {
        String str = "";
        for (Object object : objects) {
            if (object instanceof String) {
                str += (String) object;
            } else if (object instanceof ItemStack) {
                str += PrettyItem((ItemStack) object);
            } else if (object instanceof Set<?>) {
                List<Object> setObjects = new ArrayList<Object>();
                setObjects.addAll((Set<?>) object);
                str += toString(setObjects);
            }
        }
        return str;
    }

    /**
     * Takes a list of objects and parses them into a JSON array
     * @param objects Objects to be parsed
     * @return JSONArray representing objects
     */
    private static JSONArray toJSONArray(List<Object> objects) {
        JSONArray json = new JSONArray();
        for (Object object : objects) {
            if (object instanceof String) {
                json.put(objects);
            } else if (object instanceof ItemStack) {
                json.put(new JSONObject().put("test", PrettyItem((ItemStack) object)));
            } else if (object instanceof Set<?>) {
                List<Object> setObjects = new ArrayList<Object>();
                setObjects.addAll((Set<?>) object);
                JSONArray setArray = toJSONArray(setObjects);
                for (int i = 0; i < setArray.length(); i++) {
                    json.put(setArray.get(i));
                }
            }
        }
        return json;

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
            output += PrettyItem(itemStack) + ",";
        }
        //Remove trailing ","
        if (output.length() != 0) {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }

    public static String PrettyItem(ItemStack itemStack) {
        ItemStack key = itemStack.clone();
        key.setAmount(1);

        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getAmount() + " " + itemStack.getItemMeta().getDisplayName();
        } else {
            if (prettyNames.containsKey(key)) {
                return itemStack.getAmount() + " " + prettyNames.get(key);
            } else {
                return itemStack.getAmount() + " " + itemStack.getType().name() + ":" + itemStack.getDurability();
            }
        }
    }
}
