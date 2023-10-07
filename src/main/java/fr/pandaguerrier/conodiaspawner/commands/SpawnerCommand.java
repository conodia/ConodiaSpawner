package fr.pandaguerrier.conodiaspawner.commands;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.managers.SpawnerGuiManager;
import fr.pandaguerrier.conodiaspawner.spawner.player.PlayerSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnerCommand implements TabExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
    Player player = (Player) sender;
    PlayerSpawner playerSpawnerManager = ConodiaSpawner.getInstance().getPlayerSpawners().get(player.getUniqueId());

    if(playerSpawnerManager.getSpawners().isEmpty() || playerSpawnerManager == null) {
      sender.sendMessage("§cVous n'avez pas de spawner.");
      return false;
    }

    sender.sendMessage("§aVous avez " + playerSpawnerManager.getSpawners().size() + " spawner(s).");

    SpawnerGuiManager spawnerGuiManager = new SpawnerGuiManager(playerSpawnerManager);
    spawnerGuiManager.open(1);

    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    return null;
  }
}
