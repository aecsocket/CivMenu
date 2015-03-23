package vg.civcraft.mc.civmenu;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

public class Content {

    Object[] parts;

    Content(Object... objects) {
        this.parts = objects;
    }

    private boolean acceptableClass(Object object) {
        if (object instanceof String) {
            return true;
        } else if (object instanceof ItemStack) {
            return true;
        } else if (object instanceof Set<?>) {
            for (Object setObject : (Set<?>) object) {
                if (!(setObject instanceof ItemStack)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    

    /**
     * Converts this Content to a string
     *
     * @return String representing objects
     */
    public String toString() {
        String str = "";
        for (Object part : parts) {
            if (part instanceof String) {
                str += (String) part;
            } else if (part instanceof ItemStack) {
                str += Utility.PrettyItem((ItemStack) part);
            } else if (part instanceof Set<?>) {
                Set<ItemStack> itemStacks = new HashSet<ItemStack>();
                for (Object setObject : (Set<?>) part) {
                    if (setObject instanceof ItemStack) {
                        itemStacks.add((ItemStack) setObject);
                    }
                }
                str += Utility.PrettyItem(itemStacks);
            }
        }
        return str;
    }

    /**
     * Converts this Content to a JSONArray
     * @return JSONArray representing objects
     */
    public JSONArray toJSONArray() {
        JSONArray json = new JSONArray();
        for (Object object : parts) {
            if (object instanceof String) {
                json.put(new JSONObject().put("text", (String) object));
            } else if (object instanceof ItemStack) {
                json.put(new JSONObject().put("text", Utility.PrettyItem((ItemStack) object)));
            } else if (object instanceof Set<?>) {
                Set<ItemStack> itemStacks = new HashSet<ItemStack>();
                for (Object setObject : (Set<?>) object) {
                    if (setObject instanceof ItemStack) {
                        itemStacks.add((ItemStack) setObject);
                    }
                }
                json.put(new JSONObject().put("text", Utility.PrettyItem(itemStacks)));
            }
        }
        return json;

    }
}
