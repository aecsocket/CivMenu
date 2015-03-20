
package vg.civcraft.mc.civmenu;

import org.bukkit.command.CommandSender;

interface MenuCommand {
     abstract boolean execute(CommandSender sender, String[] args);
}
