package fr.pandaguerrier.conodiaspawner.spawner.level;

public class SpawnerLevel {
  private final int spawnCount;
  private final int spawnRange;
  private final int delay;
  private final int price;

  public SpawnerLevel(int spawnCount, int spawnRange, int delay, int price) {
    this.spawnCount = spawnCount;
    this.spawnRange = spawnRange;
    this.delay = delay;
    this.price = price;
  }

  public int getSpawnCount() {
    return spawnCount;
  }
  public int getSpawnRange() {
    return spawnRange;
  }
  public int getDelay() {
    return delay;
  }
  public int getPrice() {
    return price;
  }
}
