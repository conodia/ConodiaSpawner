package fr.pandaguerrier.conodiaspawner.events;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.managers.SpawnerGuiManager;
import fr.pandaguerrier.conodiaspawner.spawner.Spawner;
import fr.pandaguerrier.conodiaspawner.spawner.player.PlayerSpawner;
import fr.pandaguerrier.conodiaspawner.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
  @EventHandler
  public void onClick(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();

    if (event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR) || event.getClickedInventory().getName() == null || !event.getClickedInventory().getName().startsWith(Utils.GUI_NAME)) {
      return;
    }
    event.setCancelled(true);
    PlayerSpawner playerSpawnerManager = ConodiaSpawner.getInstance().getPlayerSpawners().get(player.getUniqueId());

    if (event.getCurrentItem().getType().equals(Material.MOB_SPAWNER)) {
      Spawner spawner = playerSpawnerManager.getSpawners().get(Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§2ID: ", "")));

      ConodiaSpawner.getInstance().getWaitingPlaceSpawners().put(player.getUniqueId(), spawner);
      player.closeInventory();
      player.sendMessage("§aVous avez sélectionné le spawner " + spawner.getId() + ". \n\n\n§aFaites click gauche pour placer le spawner.\n§cFaites click droit pour annuler la pose.");
      return;
    }
    SpawnerGuiManager spawnerGuiManager = new SpawnerGuiManager(playerSpawnerManager);

    switch (event.getCurrentItem().getType()) {
      case NETHER_STAR:
        player.closeInventory();
        spawnerGuiManager.open(1);
        break;
      case ARROW:
        player.closeInventory();
        spawnerGuiManager.open(Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§2Page: ", "")));
        break;
    }
  }

  @EventHandler
  public void onClickSpawner(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();

    if (event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR) || event.getClickedInventory().getName() == null || !event.getClickedInventory().getName().startsWith(Utils.GUI_NAME_SPAWNER)) {
      return;
    }

    event.setCancelled(true);
    PlayerSpawner playerSpawnerManager = ConodiaSpawner.getInstance().getPlayerSpawners().get(player.getUniqueId());
    Spawner spawner = playerSpawnerManager.getSpawners().get(Integer.parseInt(event.getClickedInventory().getName().replaceAll(Utils.GUI_NAME_SPAWNER, "")));

    switch (event.getCurrentItem().getType()) {
      case BARRIER:
        spawner.deleteBlock(true);
        player.sendMessage("§aVous avez enlevé votre spawner.\n\n§8ID: " + spawner.getId() + "\n§8Type: " + spawner.getType().toString() + "\n§8Level: " + spawner.getLevel());
        player.closeInventory();
        break;
      case BUCKET:
        if (player.getInventory().firstEmpty() == -1) {
          player.sendMessage("§cVous n'avez pas de place dans votre inventaire.");
          return;
        }

        spawner.delete();
        player.getInventory().addItem(spawner.toItem());
        player.sendMessage("§aVous avez récupéré votre spawner.\n\n§8ID: " + spawner.getId() + "\n§8Type: " + spawner.getType().toString() + "\n§8Level: " + spawner.getLevel());
        player.closeInventory();
        break;
      case EMERALD:
        spawner.upgrade();
        player.closeInventory();
        break;
      case INK_SACK:
        player.closeInventory();
        break;
      default:
        break;
    }
  }
}
