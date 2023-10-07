package fr.pandaguerrier.conodiaspawner.configuration;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.spawner.level.SpawnerLevel;
import fr.pandaguerrier.conodiaspawner.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LevelConfiguration {
  private final FileConfiguration configuration = ConodiaSpawner.getInstance().getConfig();
  private final List<SpawnerLevel> spawnerLevels = new ArrayList<>();

  public void sync() {
    for (int i = 0; i <= Utils.MAX_LEVEL; i++) {
      spawnerLevels.add(getLevelConfig(i));
    }
  }

  private SpawnerLevel getLevelConfig(int level) {
    HashMap<String, Integer> levelConfig = (HashMap<String, Integer>) configuration.getList("levels").get(level);
    return new SpawnerLevel(levelConfig.get("spawnCount"), levelConfig.get("spawnRange"), levelConfig.get("delay"), levelConfig.get("price"));
  }

  public List<SpawnerLevel> getSpawnerLevels() {
    return spawnerLevels;
  }
}
