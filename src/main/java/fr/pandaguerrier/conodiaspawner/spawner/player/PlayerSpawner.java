package fr.pandaguerrier.conodiaspawner.spawner.player;

import fr.pandaguerrier.conodiaspawner.spawner.Spawner;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerSpawner {
  private final Player player;
  private final Map<Integer, Spawner> spawners = new HashMap<>();

  public PlayerSpawner(Player player) {
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
  public Map<Integer, Spawner> getSpawners() {
    return spawners;
  }
}
