package fr.pandaguerrier.conodiaspawner.spawner;

import fr.pandaguerrier.conodiagameapi.ConodiaGameAPI;
import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.builder.ItemBuilder;
import fr.pandaguerrier.conodiaspawner.spawner.level.SpawnerLevel;
import fr.pandaguerrier.conodiaspawner.spawner.player.PlayerSpawner;
import fr.pandaguerrier.conodiaspawner.utils.Utils;
import net.minecraft.server.v1_8_R3.MobSpawnerAbstract;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntityMobSpawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftCreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;

public class Spawner {
  private int id;
  private final PlayerSpawner owner;
  private EntityType type;
  private int level;
  private Location location;
  private boolean isPremium;
  
  public Spawner(int id, @Nullable PlayerSpawner owner, EntityType type, int level, Location location, boolean isPremium) {
    this.id = id;
    this.owner = owner;
    this.type = type;
    this.level = level;
    this.location = location;
    this.isPremium = isPremium;
  }

  public void create() {
    JSONObject payload = (JSONObject) ConodiaGameAPI.getInstance().getApiManager().post("/spawners/" + owner.getPlayer().getUniqueId().toString(), this.toJson()).get("spawner");
    this.id = Integer.parseInt(payload.get("id").toString());
    this.owner.getSpawners().putIfAbsent(this.id, this);
  }

  // Va servir a mettre le spawner dans le monde
  public void spawn() {
    Block block = this.location.getBlock();
    block.setType(Material.MOB_SPAWNER);

    CreatureSpawner spawner = (CreatureSpawner) block.getState();
    spawner.setSpawnedType(this.type);

    SpawnerLevel spawnerLevel = Utils.getLevelConfig(this.level);
    spawner.update();

    setSpawnCount((short) spawnerLevel.getSpawnCount());
    updateSpawnerDelay((short) spawnerLevel.getDelay());

    ConodiaSpawner.getInstance().getPlacedSpawners().put(this.location, this);
  }

  public void upgrade() {
    SpawnerLevel spawnerLevel = Utils.getLevelConfig(this.level + 1);
    if (ConodiaSpawner.getEconomy().getBalance(owner.getPlayer()) < spawnerLevel.getPrice()) {
      owner.getPlayer().sendMessage("§cVous n'avez pas assez d'argent pour améliorer votre spawner !");
      return;
    }
    this.level++;

    ConodiaSpawner.getEconomy().withdrawPlayer(owner.getPlayer(), spawnerLevel.getPrice());
    owner.getPlayer().sendMessage("§aVous avez amélioré votre spawner au niveau supérieur: §2" + (this.level) + "§a !");
    owner.getPlayer().playSound(owner.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
    owner.getPlayer().sendTitle("§aAmélioration du spawner !", "§2" + (this.level - 1) + " §a>>>§2 " + (this.level));

    Block block = this.location.getBlock();
    block.setType(Material.AIR);
    this.spawn();
    this.update();
  }

  public void deleteBlock(boolean save) {
    Block block = this.location.getBlock();
    block.setType(Material.AIR);
    this.setLocation(null);

    if (save) {
      this.update();
    }

    ConodiaSpawner.getInstance().getPlacedSpawners().remove(this.location);
  }

  public void update() {
    ConodiaGameAPI.getInstance().getApiManager().put("/spawners/" + this.id + "/update", this.toJson());
  }

  public void delete() {
    ConodiaGameAPI.getInstance().getApiManager().destroy("/spawners/" + this.id, new JSONObject());

    if(owner != null) {
      owner.getSpawners().remove(this.id);
    }

    if (this.location != null) {
      this.deleteBlock(false);
    }
  }

  public ItemStack toItem() {
    ItemBuilder item  = new ItemBuilder(Material.MOB_SPAWNER);
    item.setName("§bSpawner à " + this.getType());
    item.setLore("§9Level: §b" + this.getLevel(), "", "§aClique droit pour récupérer le spawner.", "§8ID: " + this.id);
    item.setGlow(true);

    return item.build();
  }

  public void openGui() {
    Inventory inv = Bukkit.createInventory(null, 9 * 5, Utils.GUI_NAME_SPAWNER + this.id);
    SpawnerLevel spawnerLevel = Utils.getLevelConfig(this.level);
    assert this.owner != null;

    Utils.setBorders(inv);
    inv.setItem(21, new ItemBuilder(Material.PAPER).setName("§aInformations sur votre spawner").setLore("§8§m------------------------------------", "", "§bID: " + this.id, "§bLevel: " + this.level, "", "§aAvantages actuels: ", "", "§2Delai de spawn: §a" + spawnerLevel.getDelay() + "s ", "§2Nombre de mobs / spawn: §a" + spawnerLevel.getSpawnCount(), "§2Activation du spawner: §a" + spawnerLevel.getSpawnRange() + " blocks", "§2Premium: (Si le spawner est cassable ou non) " + (isPremium ? "§aOui" : "§cNon"), "", "§8§m------------------------------------").build());
    if (this.level + 1 <= Utils.MAX_LEVEL) {
      System.out.println(this.level);
      SpawnerLevel nextSpawnerLevel = Utils.getLevelConfig(this.level + 1);
      if (ConodiaSpawner.getEconomy().getBalance(owner.getPlayer()) >= nextSpawnerLevel.getPrice()) {
        inv.setItem(23, new ItemBuilder(Material.EMERALD).setName("§eUpgrade votre spawner: §6" + level + " §e➜ §6" + (level + 1)).setLore("§8§m------------------------------------", "", "§eCette opération vous coutera: §6" + ConodiaSpawner.getEconomy().format(nextSpawnerLevel.getPrice()) + "$§e !", "", "§aAvantages: ", "", "§2Delai de spawn: §a" + nextSpawnerLevel.getDelay() + "s ", "§2Nombre de mobs / spawn: §a" + nextSpawnerLevel.getSpawnCount(), "§2Activation du spawner: §a" + nextSpawnerLevel.getSpawnRange() + " blocks").setGlow(true).build());
      } else {
        inv.setItem(23, new ItemBuilder(Material.REDSTONE).setName("§eUpgrade votre spawner: §6" + level + " §e➜ §6" + (level + 1)).setLore("§8§m------------------------------------", "", "§cVous n'avez pas assez d'argent pour faire cette opération !", "§cCoût: §4" + ConodiaSpawner.getEconomy().format(nextSpawnerLevel.getPrice()) + "$", "§8§m------------------------------------").build());
      }
    } else {
      inv.setItem(23, new ItemBuilder(Material.REDSTONE_BLOCK).setName("§cVotre spawner est déjà au niveau maximum !").setGlow(true).build());
    }
    inv.setItem(39, new ItemBuilder(Material.BARRIER).setName("§4Supprimer").setLore("", "§cClick gauche pour supprimer ce spawner (Il ne sera pas supprimé, juste le block)").build());
    inv.setItem(41, new ItemBuilder(Material.BUCKET).setName("§cRécupérer le spawner en item").setLore("", "§cClick gauche pour récuperer le spawner.").build());

    owner.getPlayer().openInventory(inv);
  }

  private void updateSpawnerDelay(short delay) {
    Block block = this.location.getBlock();
    CreatureSpawner cs = (CreatureSpawner) block.getState();
    CraftCreatureSpawner ccs = (CraftCreatureSpawner) cs;
    TileEntityMobSpawner tems = ccs.getTileEntity();
    MobSpawnerAbstract msa = tems.getSpawner();
    NBTTagCompound tag = new NBTTagCompound();
    tems.b(tag);
    tag.setShort("MinSpawnDelay", delay);
    tag.setShort("MaxSpawnDelay", (short) (delay + 1));
    msa.a(tag);
  }

  private void setSpawnCount(short count) {
    Block block = this.location.getBlock();
    CreatureSpawner cs = (CreatureSpawner) block.getState();
    CraftCreatureSpawner ccs = (CraftCreatureSpawner) cs;
    TileEntityMobSpawner tems = ccs.getTileEntity();
    MobSpawnerAbstract msa = tems.getSpawner();
    NBTTagCompound tag = new NBTTagCompound();
    tems.b(tag);
    tag.setShort("SpawnCount", count);
    //owner.getPlayer().sendMessage("count = " + tag.getShort("SpawnCount"));
    msa.a(tag);
  }

  public JSONObject toJson() {
    JSONObject payload = new JSONObject() {
      {
        this.put("type", type.toString());
        this.put("id", id);
        this.put("level", level);
        this.put("isPremium", isPremium);
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

  public static Spawner from(JSONObject payload, @Nullable PlayerSpawner owner) {
    JSONObject location = (JSONObject) payload.get("location");
    return new Spawner(
        ((Long) payload.get("id")).intValue(),
        owner,
        EntityType.valueOf((String) payload.get("type")),
        ((Long) payload.get("level")).intValue(),
        payload.get("location") != null ? new Location(ConodiaSpawner.getInstance().getServer().getWorld((String) ((JSONObject) payload.get("location")).get("world")), Double.parseDouble(location.get("x").toString()), Double.parseDouble(location.get("y").toString()), Double.parseDouble(location.get("z").toString())) : null,
        (Boolean) payload.get("is_premium")
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

  public boolean isPremium() {
    return isPremium;
  }
}
