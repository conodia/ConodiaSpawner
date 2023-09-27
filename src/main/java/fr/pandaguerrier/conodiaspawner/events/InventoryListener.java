package fr.pandaguerrier.conodiaspawner.events;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.Constants;
import fr.pandaguerrier.conodiaspawner.managers.PlayerSpawner;
import fr.pandaguerrier.conodiaspawner.managers.Spawner;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
  @EventHandler
  public void onClick(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();

    if (event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR) || event.getClickedInventory().getName() == null || !event.getClickedInventory().getName().equals(Constants.GUI_NAME)) {
      return;
    }
    event.setCancelled(true);

    if (event.getCurrentItem().getType().equals(Material.MOB_SPAWNER)) {
      event.setCancelled(true);
      PlayerSpawner playerSpawner = ConodiaSpawner.getInstance().getPlayerSpawners().get(player.getUniqueId());
      Spawner spawner = playerSpawner.getSpawners().get(Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§2ID: ", "")));

      ConodiaSpawner.getInstance().getWaitingPlaceSpawners().put(player.getUniqueId(), spawner);
      player.closeInventory();
      player.sendMessage("§aVous avez sélectionné le spawner " + spawner.getId() + ". \n\n\n§aFaites click gauche pour placer le spawner.\n§cFaites click droit pour annuler la pose.");
    }
  }

  @EventHandler
  public void onClickSpawner(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();

    if (event.getClickedInventory() == null || event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR) || event.getClickedInventory().getName() == null || !event.getClickedInventory().getName().startsWith(Constants.GUI_NAME_SPAWNER)) {
      return;
    }

    event.setCancelled(true);
    PlayerSpawner playerSpawner = ConodiaSpawner.getInstance().getPlayerSpawners().get(player.getUniqueId());
    Spawner spawner = playerSpawner.getSpawners().get(Integer.parseInt(event.getClickedInventory().getName().replaceAll(Constants.GUI_NAME_SPAWNER, "")));

    switch (event.getCurrentItem().getType()) {
      case BARRIER:
        spawner.deleteBlock(true);
        player.sendMessage("§aVous avez enlevé votre spawner.\n\n§8ID: " + spawner.getId() + "\n§8Type: " + spawner.getType().toString() + "\n§8Level: " + spawner.getLevel());
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
