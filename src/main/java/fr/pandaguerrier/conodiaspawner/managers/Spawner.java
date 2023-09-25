package fr.pandaguerrier.conodiaspawner.managers;

import fr.pandaguerrier.conodiagameapi.ConodiaGameAPI;
import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import net.minecraft.server.v1_8_R3.MobSpawnerAbstract;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntityMobSpawner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftCreatureSpawner;
import org.bukkit.entity.EntityType;
import org.json.simple.JSONObject;

public class Spawner {
  private int id;
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

  public void create() {
    JSONObject payload = (JSONObject) ConodiaGameAPI.getInstance().getApiManager().post("/spawners/" + this.owner.getPlayer().getUniqueId().toString(), this.toJson()).get("spawner");
    this.id = Integer.parseInt(payload.get("id").toString());
    this.owner.getSpawners().putIfAbsent(this.id, this);
  }

  // Va servir a mettre le spawner dans le monde
  public void spawn() {
    Block block = this.location.getBlock();
    block.setType(Material.MOB_SPAWNER);
    CreatureSpawner spawner = (CreatureSpawner) block.getState();
    spawner.setSpawnedType(this.type);
    // todo reprendre depuis la config
    spawner.setDelay(2);
    spawner.update();

    this.update();
  }

  public void upgrade() {

  }

  public void deleteBlock() {
    Block block = this.location.getBlock();
    block.setType(Material.AIR);
    this.setLocation(null);

    ConodiaSpawner.getInstance().getPlacedSpawners().remove(this.location);
    //this.owner.getPlayer().sendMessage("§aVous avez enlevé votre spawner.\n\n§8ID: " + id + "\n§8Type: " + type.toString() + "\n§8Level: " + level);
  }

  public void update() {
    ConodiaGameAPI.getInstance().getApiManager().put("/spawners/" + this.id + "/update", this.toJson());
  }

  public void delete() {
    ConodiaGameAPI.getInstance().getApiManager().destroy("/spawners/" + this.id, new JSONObject());
    owner.getSpawners().remove(this.id);

    if(this.location != null) {
      this.deleteBlock();
    }
  }

  private void updateDelay(int delay) {
    CreatureSpawner spawner = (CreatureSpawner) this.location.getBlock().getState();
    spawner.setDelay(delay);
    spawner.update();
  }

  private void updateSpawn(short count) {
    CraftCreatureSpawner spawner = (CraftCreatureSpawner) this.location.getBlock().getState();
    TileEntityMobSpawner tems = spawner.getTileEntity();
    MobSpawnerAbstract msa = tems.getSpawner();
    NBTTagCompound tag = new NBTTagCompound();
    tems.b(tag);
    tag.setShort("SpawnCount", count);
    owner.getPlayer().sendMessage("count = " + tag.getShort("SpawnCount"));
    msa.a(tag);
  }

  public JSONObject toJson() {
    JSONObject payload = new JSONObject() {
      {
        this.put("type", type.toString());
        this.put("id", id);
        this.put("level", level);
      }
    };

    if (this.location == null) {
      payload.put("location", null);
      return payload;
    }
    JSONObject locationPayload = new JSONObject();
    locationPayload.put("x", this.location.getX());
    locationPayload.put("y", this.location.getY());
    locationPayload.put("z", this.location.getZ());
    locationPayload.put("world", this.location.getWorld().getName());

    payload.put("location", locationPayload);

    return payload;
  }

  public static Spawner from(JSONObject payload, PlayerSpawner owner) {
    JSONObject location = (JSONObject) payload.get("location");
    return new Spawner(
      ((Long) payload.get("id")).intValue(),
      owner,
      EntityType.valueOf((String) payload.get("type")),
      ((Long) payload.get("level")).intValue(),
      payload.get("location") != null ? new Location(ConodiaSpawner.getInstance().getServer().getWorld((String) ((JSONObject) payload.get("location")).get("world")),Double.parseDouble(location.get("x").toString()), Double.parseDouble(location.get("y").toString()), Double.parseDouble(location.get("z").toString())) : null
    );
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
  public boolean isPlaced() {
    return this.location != null;
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
