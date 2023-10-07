package fr.pandaguerrier.conodiaspawner.events;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
public class PlayerListener implements Listener {

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    PlayerManager playerManager = ConodiaSpawner.getInstance().getPlayerManager();
    playerManager.loadPlayer(player);
  }

  @EventHandler
  public void onLeave(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    PlayerManager playerManager = ConodiaSpawner.getInstance().getPlayerManager();
    playerManager.unloadPlayer(player);
  }
}
