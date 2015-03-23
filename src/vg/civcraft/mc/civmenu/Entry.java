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

    Content text;
    Content hover;
    Content suggest;
    Content insertion;
    MenuCommand command;
    String[] args;

    public Entry(Object... text) {
        this.text = new Content(text);
    }

    /**
     * If an action is performed upon clicking this entry
     *
     * @return If Entry is Clickable
     */
    boolean isClickable() {
        return command != null || suggest != null || insertion != null;
    }

    boolean hasCommand() {
        return command != null;
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
        JSONObject coreText = new JSONObject().put("text", text.toString());
        //JSONObject coreText = new JSONObject();
        //content..put("color", "yellow");
        if (hover != null) {
            JSONObject hoverEvent = new JSONObject().put("action", "show_text").put("value", hover.toJSONArray());
            coreText.put("hoverEvent", hoverEvent);
        }
        if (command != null) {
            JSONObject clickEvent = new JSONObject().put("action", "run_command").put("value", ID);
            coreText.put("clickEvent", clickEvent);
        } else if (suggest != null) {
            JSONObject clickEvent = new JSONObject().put("action", "suggest_command").put("value", suggest.toString());
            coreText.put("clickEvent", clickEvent);
        }
        if (insertion != null) {
            coreText.put("insertion", insertion.toString());
        }
        //Add white coloring to clickable entrys
        coreText.put("color", isClickable() ? "white" : "yellow");
        //Add bracket styling to clickable Entrys
        json.add(coreText);
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
     * Sets the text of the Entry
     *
     * The text is given as an array of Objects to enable built in parsing by the
     * entry class of objects such as ItemStack, Set{@literal <ItemStack\>}, or
     * just String.
     *
     * @param text Text of the Entry
     * @return This Entry
     */
    public Entry setText(Object... text) {
        this.text = new Content(text);
        return this;
    }

    /**
     * Sets the hover of the Entry
     *
     * The text is given as a List of Objects to enable built in parsing by the
     * entry class of objects such as ItemStack, Set{@literal <ItemStack\>}, or
     * just String.
     *
     * @param hover What to set the hover to
     * @return This Entry
     */
    public Entry setHover(Object... hover) {
        this.hover = new Content(hover);
        return this;
    }

    /**
     * Sets the insertion of this entry
     *
     * @param insertion The insertion of this Entry
     * @return This Entry
     */
    public Entry setInsertion(Object... insertion) {
        this.insertion = new Content(insertion);
        return this;
    }

    /**
     * Sets the suggest of the Entry
     *
     * The text is given as a List of Objects to enable built in parsing by the
     * entry class of objects such as ItemStack, Set{@literal <ItemStack\>}, or
     * just String.
     *
     * @param suggest What to set the suggest to
     * @return This Entry
     */
    public Entry setSuggest(Object... suggest) {
        this.suggest = new Content(suggest);
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

}
