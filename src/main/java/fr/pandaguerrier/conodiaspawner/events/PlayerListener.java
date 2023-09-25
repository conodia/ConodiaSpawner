package fr.pandaguerrier.conodiaspawner.events;

import fr.pandaguerrier.conodiagameapi.ConodiaGameAPI;
import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.managers.PlayerSpawner;
import fr.pandaguerrier.conodiaspawner.managers.Spawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;

import java.util.List;

public class PlayerListener implements Listener {

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    JSONObject payload = (JSONObject) ConodiaGameAPI.getInstance().getApiManager().get("/player/" + player.getUniqueId().toString(), new JSONObject()).get("player");
    if(payload == null)  return;

    PlayerSpawner playerSpawner = new PlayerSpawner(player);

    for(JSONObject spawnerPayload : (List<JSONObject>) payload.get("spawners")) {
      Spawner spawner = Spawner.from(spawnerPayload, playerSpawner);
      playerSpawner.getSpawners().putIfAbsent(spawner.getId(), spawner);

      if (spawner.isPlaced()) {
        ConodiaSpawner.getInstance().getPlacedSpawners().put(spawner.getLocation(), spawner);
      }
    }
    ConodiaSpawner.getInstance().getPlayerSpawners().put(player.getUniqueId(), playerSpawner);
  }
}
