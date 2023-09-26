package fr.pandaguerrier.conodiaspawner.commands;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.Constants;
import fr.pandaguerrier.conodiaspawner.managers.PlayerSpawner;
import fr.pandaguerrier.conodiaspawner.managers.Spawner;
import fr.pandaguerrier.conodiaspawner.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SpawnerCommand implements TabExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
    Player player = (Player) sender;
    PlayerSpawner playerSpawner = ConodiaSpawner.getInstance().getPlayerSpawners().get(player.getUniqueId());

    if(playerSpawner.getSpawners().isEmpty() || playerSpawner == null) {
      sender.sendMessage("§cVous n'avez pas de spawner.");
      return false;
    }

    sender.sendMessage("§aVous avez " + playerSpawner.getSpawners().size() + " spawner(s).");

    Inventory inventory = Bukkit.createInventory(null, 9, Constants.GUI_NAME);

    for (Spawner spawner : playerSpawner.getSpawners().values()) {
      ItemBuilder itemBuilder = new ItemBuilder(Material.MOB_SPAWNER);
      itemBuilder.setName("ID: " + spawner.getId());
      itemBuilder.setLore("§aType: " + spawner.getType().name(), "§aLevel: " + spawner.getLevel(), "", "§bPlacé: " + (spawner.isPlaced() ? "§aOui" : "§cNon"), (spawner.isPlaced() ? "§aX: " + spawner.getLocation().getX() + " §aY: " + spawner.getLocation().getY() + " §aZ: " + spawner.getLocation().getZ() : ""), "", "§aCliquez pour sélectionner ce spawner.");
      itemBuilder.setGlow(spawner.isPlaced());

      inventory.addItem(itemBuilder.build());
    }

    ((Player) sender).openInventory(inventory);


    return false;
  }

  @Override
  public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
    return null;
  }
}
