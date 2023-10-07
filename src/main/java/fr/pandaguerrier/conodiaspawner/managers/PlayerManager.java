package fr.pandaguerrier.conodiaspawner.managers;

import fr.pandaguerrier.conodiagameapi.ConodiaGameAPI;
import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.spawner.Spawner;
import fr.pandaguerrier.conodiaspawner.spawner.player.PlayerSpawner;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.List;

public class PlayerManager {
  public void loadPlayer(Player player) {
    JSONObject payload = (JSONObject) ConodiaGameAPI.getInstance().getApiManager().get("/player/" + player.getUniqueId().toString(), new JSONObject()).get("player");
    if(payload == null)  return;

    PlayerSpawner playerSpawnerManager = new PlayerSpawner(player);

    for(JSONObject spawnerPayload : (List<JSONObject>) payload.get("spawners")) {
      Spawner spawner = Spawner.from(spawnerPayload, playerSpawnerManager);
      playerSpawnerManager.getSpawners().putIfAbsent(spawner.getId(), spawner);

      if (spawner.isPlaced()) {
        ConodiaSpawner.getInstance().getPlacedSpawners().put(spawner.getLocation(), spawner);
      }
    }
    ConodiaSpawner.getInstance().getPlayerSpawners().put(player.getUniqueId(), playerSpawnerManager);
  }

  public void unloadPlayer(Player player) {
    ConodiaSpawner.getInstance().getPlayerSpawners().remove(player.getUniqueId());
  }

  public void sync() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      PlayerSpawner playerSpawnerManager = ConodiaSpawner.getInstance().getPlayerSpawners().get(player.getUniqueId());
      if (playerSpawnerManager == null) {
        loadPlayer(player);
      }
    }
  }
}
