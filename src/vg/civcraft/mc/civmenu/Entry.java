package vg.civcraft.mc.civmenu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;
import static vg.civcraft.mc.civmenu.Utility.PrettyItem;

/**
 * Entrys are the text components that make up a Menu. Each entry display a
 * string of text which can be functionalized to have the following actions:
 *
 * <ul>
 * <li>Execute a command method on click</li>
 * <li>Launch another Menu on click</li>
 * <li>Have a Hover text</li>
 * <li>Suggest text on click by adding it to the text entry window of the
 * player</li>
 * <li>Simulate the player entering in text on click</li>
 * </ul>
 *
 * The Text component of an entry is specified either by a String or a
 * List{@literal <Object>} If a list is used it can include other parseble items
 * such as JSON strings, ItemStack, or Set{@literal <ItemStack\>}, all of which
 * will be parsed by the Entry into a text string.
 *
 * An Entry's text is algorithmically formated unless it already contains
 * overriding formatting in the form of JSON strings. By default non-interactive
 * text will be colored yellow, while interactive text will be white but
 * bracketed by two yellow brackets. These defaults will likely be changed in
 * the future.
 *
 */
public class Entry {

    List<Object> text;
    List<Object> hover;
    String insertion;
    MenuCommand command;
    String[] args;

    public Entry(List<Object> text) {
        this.text = text;
    }

    public Entry(String text) {
        this.text = new ArrayList<Object>();
        this.text.add(text);
    }

    /**
     * If an action is performed upon clicking this entry
     *
     * @return If Entry is Clickable
     */
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
    List<JSONObject> toJSON(String ID) {
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
            JSONObject clickEvent = new JSONObject().put("action", "run_command").put("value", ID);
            content.put("clickEvent", clickEvent);
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

    /**
     * Generates a List of JSON objects representing this text
     *
     * Of the following format: If there is not command (yellow)text(end yellow)
     * If there is a command {yellow)[(end yellow)text(yellow)[(end yellow)
     *
     * @return List of JSONObjects representing output
     */
    List<JSONObject> toJSON() {
        return toJSON("");
    }

    /**
     * Sets the text of the Entry ot the String
     *
     * @param text Text to set
     * @return This Entry
     */
    public Entry setText(String text) {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(text);
        return setText(list);
    }

    /**
     * Sets the text of the Entry
     *
     * The text is given as a List of Objects to enable built in parsing by the
     * entry class of objects such as ItemStack, Set{@literal <ItemStack\>}, or
     * just String. Vanilla objects and objects with custom display names will
     * be parsed according to # DISPLAY_NAME, newer items not contained within
     * the lookup file will be displayed by MATERIAL_NAME:DURABILITY
     *
     * @param text Text of the Entry
     * @return This Entry
     */
    public Entry setText(List<Object> text) {
        this.text = text;
        return this;
    }

    /**
     * Sets the hover of the Entry
     *
     * The text is given as a List of Objects to enable built in parsing by the
     * entry class of objects such as ItemStack, Set{@literal <ItemStack\>}, or
     * just String. Vanilla objects and objects with custom display names will
     * be parsed according to # DISPLAY_NAME, newer items not contained within
     * the lookup file will be displayed by MATERIAL_NAME:DURABILITY
     *
     * @param hover What to set the hover to
     * @return This Entry
     */
    public Entry setHover(List<Object> hover) {
        this.hover = hover;
        return this;
    }

    /**
     * Sets the hover of the Entry to the given String
     *
     * @param hover What to set the hover to
     * @return This Entry
     */
    public Entry setHover(String hover) {
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(hover);
        return setText(list);
    }

    /**
     * Sets the insertion of this entry
     *
     * @param insertion The insertion of this Entry
     * @return This Entry
     */
    public Entry setInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    /**
     * Sets the Command to execute on click
     *
     * @param command Command to execute
     * @return This Entry
     */
    public Entry setCommand(MenuCommand command) {
        return setCommand(command, new String[1]);
    }

    /**
     * Sets the Command to execute on click
     *
     * @param command Command to execute
     * @params The arguments used to execute the command
     * @return This Entry
     */
    public Entry setCommand(MenuCommand command, String[] args) {
        this.command = command;
        this.args = args;
        return this;
    }

    /**
     * Gets the MenuCommand associated with this Entry
     *
     * @return
     */
    public MenuCommand getCommand() {
        return command;
    }

    /**
     * Gets the Arguments used for this Entrys MenuCommand
     *
     * @return The Arguments used for this Entrys MenuCommand
     */
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
                str += Utility.PrettyItem((ItemStack) object);
            } else if (object instanceof Set<?>) {
                Set<ItemStack> itemStacks = new HashSet<ItemStack>();
                for (Object setObject : (Set<?>) object) {
                    if (setObject instanceof ItemStack) {
                        itemStacks.add((ItemStack) setObject);
                    }
                }
                str+=PrettyItem(itemStacks);
            }
        }
        return str;
    }

    /**
     * Takes a list of objects and parses them into a JSON array
     *
     * @param objects Objects to be parsed
     * @return JSONArray representing objects
     */
    private static JSONArray toJSONArray(List<Object> objects) {
        JSONArray json = new JSONArray();
        for (Object object : objects) {
            if (object instanceof String) {
                json.put(objects);
            } else if (object instanceof ItemStack) {
                json.put(new JSONObject().put("text", PrettyItem((ItemStack) object)));
            } else if (object instanceof Set<?>) {
                Set<ItemStack> itemStacks = new HashSet<ItemStack>();
                for (Object setObject : (Set<?>) object) {
                    if (setObject instanceof ItemStack) {
                        itemStacks.add((ItemStack) setObject);
                    }
                }
                json.put(new JSONObject().put("text", PrettyItem(itemStacks)));
            }
        }
        return json;

    }
}
