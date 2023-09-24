package fr.pandaguerrier.conodiaspawner.managers;

import org.bukkit.entity.Player;

import java.util.List;

public class PlayerSpawner {
  private final Player player;
  private final List<Spawner> spawners;

  public PlayerSpawner(Player player, List<Spawner> spawners) {
    this.player = player;
    this.spawners = spawners;
  }

  public Player getPlayer() {
    return player;
  }
  public List<Spawner> getSpawners() {
    return spawners;
  }
}
