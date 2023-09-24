package fr.pandaguerrier.conodiaspawner.managers;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.json.simple.JSONObject;

public class Spawner {
  private final int id;
  private final PlayerSpawner owner;
  private EntityType type;
  private int level;
  private Location location;

  public Spawner(int id, PlayerSpawner owner, EntityType type, int level, Location location) {
    this.id = id;
    this.owner = owner;
    this.type = type;
    this.level = level;
    this.location = location;
  }

  public void spawn() {
    // todo
  }

  public void upgrade() {
    // todo
  }

  public void deleteBlock() {
    // todo
  }

  public void delete() {
    // todo
  }

  public JSONObject toJson() {
    JSONObject payload = new JSONObject() {
      {
        this.put("type", type.toString());
        this.put("id", id);
        this.put("level", level);
      }
    };

    JSONObject locationPayload = new JSONObject();
    locationPayload.put("x", this.location.getX());
    locationPayload.put("y", this.location.getY());
    locationPayload.put("z", this.location.getZ());
    locationPayload.put("world", this.location.getWorld().getName());

    payload.put("location", locationPayload);

    return payload;
  }

  public int getId() {
    return id;
  }
  public PlayerSpawner getOwner() {
    return owner;
  }
  public EntityType getType() {
    return type;
  }
  public int getLevel() {
    return level;
  }
  public Location getLocation() {
    return location;
  }

  public void setType(EntityType type) {
    this.type = type;
  }
  public void setLevel(int level) {
    this.level = level;
  }
  public void setLocation(Location location) {
    this.location = location;
  }
}
