package fr.pandaguerrier.conodiaspawner.commands.subcommands;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.commands.ConodiaCommand;
import fr.pandaguerrier.conodiaspawner.spawner.Spawner;
import fr.pandaguerrier.conodiaspawner.spawner.player.PlayerSpawner;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnerGiveCommand extends ConodiaCommand {
  public SpawnerGiveCommand() {
    super("give");
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
    // /spawner give <player> <type> <isPremium> [amount]
    if(args.length < 5) {
      sender.sendMessage("§cUsage: /spawner give <player> <type> <level> <isPremium> [amount]");
      return false;
    }

    String playerName = args[1];
    if(Bukkit.getPlayer(playerName) == null) {
      sender.sendMessage("§cLe joueur " + playerName + " n'est pas connecté.");
      return false;
    }
    String entityTypeString = args[2];
    EntityType entityType = EntityType.valueOf(entityTypeString);
    if(entityType == null) {
      sender.sendMessage("§cL'entité " + entityTypeString + " n'existe pas.");
      return false;
    }
    int level = Integer.parseInt(args[3]);
    boolean isPremium = Boolean.parseBoolean(args[4]);
    int amount = Integer.parseInt(args[5]);

    System.out.println("playerName: " + playerName);
    System.out.println("entityType: " + entityType);
    System.out.println("level: " + level);
    System.out.println("isPremium: " + isPremium);
    System.out.println("amount: " + amount);

    Player target = ConodiaSpawner.getInstance().getServer().getPlayer(playerName);
    PlayerSpawner playerSpawnerManager = ConodiaSpawner.getInstance().getPlayerSpawners().get(target.getUniqueId());

    for (int i = 0; i < amount; i++) {
      Spawner spawner = new Spawner(0, playerSpawnerManager, entityType, level, null, isPremium);
      spawner.create();
    }

    target.sendMessage("§aVous avez reçu " + amount + " spawner(s) de type " + entityType.name() + " de niveau " + level + " !");
    sender.sendMessage("§aVous avez donné " + amount + " spawner(s) de type " + entityType.name() + " de niveau " + level + " à " + playerName + " !");
    return false;
  }

  private EntityType getEntity(String name) {
    for(EntityType type : EntityType.values()) {
      if(type.name().equalsIgnoreCase(name)) {
        return type;
      }
    }
    return null;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    if(strings.length == 2) {
      return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    } else if(strings.length == 3) {
      // get alls entity types by starting with strings[2]
      return Arrays.stream(EntityType.values()).map(EntityType::name).filter(name -> name.toLowerCase().startsWith(strings[2].toLowerCase())).collect(Collectors.toList());
    } else if(strings.length == 4) {
      return Arrays.asList("0", "1", "2", "3", "4", "5");
    } else if(strings.length == 5) {
      return Arrays.asList("true", "false");
    } else if(strings.length == 6) {
      return Arrays.asList("1", "2", "3", "4", "5");
    }

    return null;
  }
}
