package com.randomcraft.command;

import com.randomcraft.RandomCraft;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RandomCraftCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;
        if (args.length == 0) {
            sender.sendMessage("/randomcraft <player> <add|remove|set> <health>");
            return true;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            RandomCraft.getInstance().getWorldManager().reset();
            return true;
        }

        String player = args[0];
        String operation = args[1];
        int health = Integer.parseInt(args[2]);

        switch (operation) {
            case "ADD": {
                RandomCraft.getInstance().getData().set("players." + player + ".lives", RandomCraft.getInstance().getData().getInt("players." + player + ".lives") + health);
                break;
            }
            case "REMOVE": {
                RandomCraft.getInstance().getData().set("players." + player + ".lives", RandomCraft.getInstance().getData().getInt("players." + player + ".lives") - health);
                break;
            }
            case "SET": {
                RandomCraft.getInstance().getData().set("players." + player + ".lives", health);
                break;
            }
        }
        sender.sendMessage("lives updated " + player + ":" + health);
        return false;
    }
}
