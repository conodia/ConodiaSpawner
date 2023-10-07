package fr.pandaguerrier.conodiaspawner.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public abstract class ConodiaCommand implements TabExecutor {
  private String name;

  public ConodiaCommand(String name) {
    this.name = name;
  }

  public abstract boolean onCommand(CommandSender sender, Command command, String s, String[] strings);
  public abstract List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings);

  public String getName() {
    return name;
  }
}
