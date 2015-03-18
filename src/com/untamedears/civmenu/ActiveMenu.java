package com.untamedears.civmenu;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActiveMenu {

    Player player;
    List<MenuCommand> commands;
    List<String[]> args;
    String message;

    public ActiveMenu(Player player) {
        this.player = player;
        commands = new ArrayList<MenuCommand>();
    }

    public void addCommand(MenuCommand command, String[] args) {
        commands.add(command);
        this.args.add(args);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean execute(CommandSender sender, int index) {
        if (index > 0 && index < commands.size()) {
            return commands.get(index).execute(sender, args.get(index));
        }
        return false;
    }

    public MenuCommand getCommand(int index) {
        return commands.get(index);
    }
}
