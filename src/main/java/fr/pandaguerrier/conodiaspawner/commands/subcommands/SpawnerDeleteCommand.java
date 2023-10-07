package fr.pandaguerrier.conodiaspawner.commands.subcommands;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.commands.ConodiaCommand;
import fr.pandaguerrier.conodiaspawner.spawner.Spawner;
import fr.pandaguerrier.conodiaspawner.spawner.player.PlayerSpawner;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class SpawnerDeleteCommand extends ConodiaCommand {
  public SpawnerDeleteCommand() {
    super("delete");
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
    // /spawner delete <player> <id>
    if(args.length < 2) {
      sender.sendMessage("§cUsage: /spawner delete <player> <id>");
      return false;
    }

    String playerName = args[1];
    if(Bukkit.getPlayer(playerName) == null) {
      sender.sendMessage("§cLe joueur " + playerName + " n'est pas connecté.");
      return false;
    }
    String spawnerId = args[2];

    Player target = ConodiaSpawner.getInstance().getServer().getPlayer(playerName);
    PlayerSpawner playerSpawnerManager = ConodiaSpawner.getInstance().getPlayerSpawners().get(target.getUniqueId());
    Spawner spawner = playerSpawnerManager.getSpawners().get(Integer.parseInt(spawnerId));

    target.sendMessage("§cLe spawner " + spawner.getId() + " a été supprimé !");
    spawner.delete();

    sender.sendMessage("§aVous avez supprimé le spawner " + spawner.getId() + " de " + target.getName() + " !");
    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    if(strings.length == 2) {
      return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    } else if(strings.length == 3) {
      Player target = ConodiaSpawner.getInstance().getServer().getPlayer(strings[1]);

      if (target == null) return null;
      PlayerSpawner playerSpawnerManager = ConodiaSpawner.getInstance().getPlayerSpawners().get(target.getUniqueId());
      if (playerSpawnerManager == null) return null;

      return playerSpawnerManager.getSpawners().values().stream().map(spawner -> String.valueOf(spawner.getId())).collect(Collectors.toList());
    }

    return null;
  }
}
