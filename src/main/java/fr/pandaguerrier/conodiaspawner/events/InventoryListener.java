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

    if (event.getCurrentItem().getType().equals(Material.MOB_SPAWNER)) {
      event.setCancelled(true);
      PlayerSpawner playerSpawner = ConodiaSpawner.getInstance().getPlayerSpawners().get(player.getUniqueId());
      Spawner spawner = playerSpawner.getSpawners().get(Integer.parseInt(event.getCurrentItem().getItemMeta().getDisplayName().replaceAll("ID: ", "")));

      ConodiaSpawner.getInstance().getWaitingPlaceSpawners().put(player.getUniqueId(), spawner);
      player.closeInventory();
      player.sendMessage("§aVous avez sélectionné le spawner " + spawner.getId() + ". \n\n\n§aFaites click gauche pour placer le spawner.\n§cFaites click droit pour annuler la pose.");
    }
  }
}
