package fr.pandaguerrier.conodiaspawner.commands;

import fr.pandaguerrier.conodiaspawner.commands.subcommands.SpawnerDeleteCommand;
import fr.pandaguerrier.conodiaspawner.commands.subcommands.SpawnerGiveCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;
import java.util.stream.Collectors;

public class SpawnerAdminCommand implements TabExecutor {
  List<ConodiaCommand> subCommands = List.of(
      new SpawnerGiveCommand(),
      new SpawnerDeleteCommand()
  );

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

    if (!sender.hasPermission("conodia.admin")) {
      sender.sendMessage("§cVous n'avez pas la permission d'utiliser cette commande.");
      return false;
    }

    if (strings.length == 0) {
      sender.sendMessage("§cUsage: /spawneradmin <subcommand>");
      return false;
    }

    for (ConodiaCommand subCommand : subCommands) {
      if (subCommand.getName().equalsIgnoreCase(strings[0])) {
        subCommand.onCommand(sender, command, s, strings);
        return false;
      }
    }

    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    if (strings.length == 1) {
      return subCommands.stream().map(ConodiaCommand::getName).collect(Collectors.toList());
    }
    for (ConodiaCommand subCommand : subCommands) {
      if (subCommand.getName().equalsIgnoreCase(strings[0])) {
        return subCommand.onTabComplete(commandSender, command, s, strings);
      }
    }
    return null;
  }
}
