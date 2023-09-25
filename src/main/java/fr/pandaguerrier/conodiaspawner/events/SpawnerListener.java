package fr.pandaguerrier.conodiaspawner.events;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.managers.Spawner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpawnerListener implements Listener {
  @EventHandler
  public void onClick(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) && ConodiaSpawner.getInstance().getWaitingPlaceSpawners().containsKey(player.getUniqueId())) {
      event.setCancelled(true);

      if(!canPose(event.getClickedBlock().getLocation(), player)){
        player.sendMessage("§cVous ne pouvez pas poser un spawner ici.");
        return;
      }

      Spawner spawner = ConodiaSpawner.getInstance().getWaitingPlaceSpawners().get(player.getUniqueId());

      if (spawner.isPlaced()) {
        spawner.deleteBlock();
      }

      spawner.setLocation(event.getClickedBlock().getLocation());
      spawner.spawn();

      ConodiaSpawner.getInstance().getWaitingPlaceSpawners().remove(player.getUniqueId());
      player.sendMessage("§aVous avez placé le spawner " + spawner.getId() + ", de type: " + spawner.getType().getName() + " !");
    } else if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) && ConodiaSpawner.getInstance().getWaitingPlaceSpawners().containsKey(player.getUniqueId())) {
      event.setCancelled(true);
      ConodiaSpawner.getInstance().getWaitingPlaceSpawners().remove(player.getUniqueId());
      player.sendMessage("§cVous avez annulé la pose du spawner.");
    }
  }

  private boolean canPose(Location location, Player player) {
    Material block = location.getBlock().getType();
    if(block.equals(Material.BEDROCK) || block.equals(Material.BARRIER)){
      player.sendMessage("§cVous ne pouvez pas poser un spawner ici.");
      return false;
    }
    return true;
  }
}
