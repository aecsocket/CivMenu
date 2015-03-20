
package vg.civcraft.mc.civmenu;

import org.bukkit.command.CommandSender;

public interface MenuCommand {
    abstract boolean execute(CommandSender sender, String[] args);
}
