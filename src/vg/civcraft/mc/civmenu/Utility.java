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

/**
 *
 * @author Brian
 */
public class Utility {

    static Map<ItemStack, String> prettyNames = new HashMap<ItemStack, String>();

    public static void loadPrettyNames(File file) {
        prettyNames = new HashMap<ItemStack, String>();
        // Read Items
        try {
            BufferedReader CSVFile = new BufferedReader(new FileReader(file));
            String dataRow = CSVFile.readLine();
            while (dataRow != null) {
                String[] dataArray = dataRow.split(",");
                prettyNames.put(new ItemStack(Material.getMaterial(Integer.valueOf(dataArray[2]).intValue()), 1, Short.valueOf(dataArray[3])), dataArray[0]);
                dataRow = CSVFile.readLine();
            }
            CSVFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            CivMenu.toConsole("Failed to load materials.csv");
        }
    }

    static String PrettyItem(ItemStack itemStack) {
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
