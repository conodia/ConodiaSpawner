package fr.pandaguerrier.conodiaspawner.objects;

public class LevelConfiguration {
  int spawnCount;
  int spawnRange;
  int delay;
  int price;

  public LevelConfiguration(int spawnCount, int spawnRange, int delay, int price) {
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
  public void setSpawnCount(int spawnCount) {
    this.spawnCount = spawnCount;
  }
}
