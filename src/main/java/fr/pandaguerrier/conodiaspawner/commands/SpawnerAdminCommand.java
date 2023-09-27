package fr.pandaguerrier.conodiaspawner.commands;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.managers.PlayerSpawner;
import fr.pandaguerrier.conodiaspawner.managers.Spawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnerAdminCommand implements TabExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
    PlayerSpawner playerSpawner = ConodiaSpawner.getInstance().getPlayerSpawners().get(((Player) sender).getUniqueId());
    Spawner spawner = new Spawner(0, playerSpawner, EntityType.ZOMBIE, 0, null, true);
    spawner.setLevel(6);
    spawner.create();

    sender.sendMessage("§aVous avez reçu un spawner zombie.");
    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    return null;
  }
}
