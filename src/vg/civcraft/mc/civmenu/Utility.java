/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vg.civcraft.mc.civmenu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

/**
 *
 * @author Brian
 */
public class Utility {

    static Map<ItemStack, String> prettyNames = new HashMap<>();

    public static void loadPrettyNames() {
        prettyNames = new HashMap<>();
        for (Material mat : Material.values()) {
            prettyNames.put(new ItemStack(mat), getPrettyName(mat));
        }
    }

    private static String getPrettyName(Material mat) {
        String ret = mat.name();
        String[] words = ret.split("_");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.length() > 1)
                word = word.substring(0, 0).toUpperCase() + word.substring(1).toLowerCase();
            else
                word = word.toUpperCase();
            words[i] = word;
        }
        ret = "";
        for (String word : words) {
            ret += word + " ";
        }
        return ret;
    }

    /*
    Don't load from a .csv file, instead iterate through all Material enums because 1.13 spigot update
    public static void loadPrettyNames(File file) {
        prettyNames = new HashMap<>();
        // Read Items
        try {
            BufferedReader CSVFile = new BufferedReader(new FileReader(file));
            String dataRow = CSVFile.readLine();
            while (dataRow != null) {
                String[] dataArray = dataRow.split(",");
                prettyNames.put(new ItemStack(Material.valueOf(dataArray[1])), dataArray[0]);
                dataRow = CSVFile.readLine();
            }
            CSVFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            CivMenu.toConsole("Failed to load materials.csv");
        }
    }
    */

    static String PrettyItem(ItemStack itemStack) {
        ItemStack key = itemStack.clone();
        key.setAmount(1);
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getAmount() + " " + itemStack.getItemMeta().getDisplayName();
        } else {
            if (prettyNames.containsKey(key)) {
                return itemStack.getAmount() + " " + prettyNames.get(key);
            } else {
                return itemStack.getAmount() + " " + itemStack.getType().name() + ":" + ((Damageable) itemStack.getItemMeta()).getDamage();
            }
        }
    }

    /**
     * Convert a ItemSet to a pretty string
     *
     * If the item has a custom DisplayName it uses that, then if it has an
     * entry in the pretty name lookup table use that, otherwise use bukkit name
     *
     * @param itemStacks ItemStakcs to convert to string
     * @return Pretty String representing ItemStacks
     */
    static String PrettyItem(Set<ItemStack> itemStacks) {
        String output = "";
        for (ItemStack itemStack : itemStacks) {
            output += " "+PrettyItem(itemStack) + ",";
        }
        if (output.length() != 0) {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }

}
