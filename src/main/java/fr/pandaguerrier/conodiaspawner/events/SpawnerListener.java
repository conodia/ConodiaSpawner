package fr.pandaguerrier.conodiaspawner.events;

import com.massivecraft.factions.listeners.FactionsBlockListener;
import fr.pandaguerrier.conodiagameapi.ConodiaGameAPI;
import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.spawner.Spawner;
import fr.pandaguerrier.conodiaspawner.spawner.player.PlayerSpawner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.util.UUID;

public class SpawnerListener implements Listener {
  @EventHandler
  public void playerPlaceSpawner(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) && ConodiaSpawner.getInstance().getWaitingPlaceSpawners().containsKey(player.getUniqueId())) {
      event.setCancelled(true);

      if (!canPose(event.getClickedBlock().getLocation(), player)) {
        player.sendMessage("§cVous ne pouvez pas poser un spawner ici, vous pouvez uniquement poser des spawners sur un block de §4COBBLESTONE§c.");
        return;
      }

      Spawner spawner = ConodiaSpawner.getInstance().getWaitingPlaceSpawners().get(player.getUniqueId());

      if (spawner.isPlaced()) {
        spawner.deleteBlock(false);
      }

      spawner.setLocation(event.getClickedBlock().getLocation());
      spawner.spawn();
      spawner.update();

      ConodiaSpawner.getInstance().getWaitingPlaceSpawners().remove(player.getUniqueId());
      player.sendMessage("§aVous avez placé le spawner " + spawner.getId() + ", de type: " + spawner.getType().getName() + " !");
    } else if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) && ConodiaSpawner.getInstance().getWaitingPlaceSpawners().containsKey(player.getUniqueId())) {
      event.setCancelled(true);
      ConodiaSpawner.getInstance().getWaitingPlaceSpawners().remove(player.getUniqueId());
      player.sendMessage("§cVous avez annulé la pose du spawner.");
    }
  }

  @EventHandler
  public void onBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    if (event.getBlock().getType().equals(Material.MOB_SPAWNER)) {
      Spawner spawner = ConodiaSpawner.getInstance().getPlacedSpawners().get(event.getBlock().getLocation());
      if (spawner != null) {
        event.setCancelled(true);
        player.sendMessage("§cVous ne pouvez pas casser ce spawner.");
      }
    }
  }

  @EventHandler
  public void onExplode(EntityExplodeEvent event) {
    for (Block block : event.blockList()) {
      if (!block.getType().equals(Material.MOB_SPAWNER)) {
        continue;
      }

      Spawner spawner = ConodiaSpawner.getInstance().getPlacedSpawners().get(block.getLocation());

      if (spawner != null) {
        removeSpawner(spawner, block, event);
        continue;
      }

      JSONObject payload = ConodiaGameAPI.getInstance().getApiManager().get("/spawners/coords/" + block.getLocation().getBlockX() + "/" + block.getLocation().getBlockY() + "/" + block.getLocation().getBlockZ() + "/" + block.getLocation().getWorld().getName(), new JSONObject());

      if (payload != null) {
        PlayerSpawner owner = ConodiaSpawner.getInstance().getPlayerSpawners().get(UUID.fromString(payload.get("player_id").toString()));
        removeSpawner(Spawner.from((JSONObject) payload.get("spawner"), owner), block, event);
      }
     }
  }

  private void removeSpawner(Spawner spawner, Block block, EntityExplodeEvent event) {
    /*if (spawner.isPremium()) {
      event.blockList().remove(block);
      return;
    }*/

    block.getWorld().dropItemNaturally(block.getLocation(), spawner.toItem());

    spawner.setLocation(null);
    spawner.delete();

    if (ConodiaSpawner.getInstance().getPlacedSpawners().containsKey(spawner.getLocation())) {
      ConodiaSpawner.getInstance().getPlacedSpawners().remove(spawner.getLocation());
    }

    if (spawner.getOwner() != null) {
      spawner.getOwner().getSpawners().remove(spawner.getId());
    }
  }

  @EventHandler
  public void onInteract(PlayerInteractEvent event) {
    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.MOB_SPAWNER)) {
      Spawner spawner = ConodiaSpawner.getInstance().getPlacedSpawners().get(event.getClickedBlock().getLocation());
      if (spawner != null && spawner.getOwner().getPlayer().getUniqueId().equals(event.getPlayer().getUniqueId())) {
        spawner.openGui();
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onRightClick(PlayerInteractEvent event) {
    if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) && event.getPlayer().getItemInHand().getType().equals(Material.MOB_SPAWNER) && event.getPlayer().getItemInHand().hasItemMeta()) {
      PlayerSpawner playerSpawner = ConodiaSpawner.getInstance().getPlayerSpawners().get(event.getPlayer().getUniqueId());
      ItemStack item = event.getPlayer().getItemInHand();
      String typeString = item.getItemMeta().getDisplayName().replaceAll("§bSpawner à ", "").toUpperCase();
      if (!item.getItemMeta().hasLore()) {
        event.getPlayer().sendMessage("§cUne erreur est survenue lors de la création du spawner, merci de contacter un staff.");
        return;
      }
      String levelString = item.getItemMeta().getLore().get(0).replaceAll("§9Level: §b", "");

      if (typeString == null || levelString == null) {
        event.getPlayer().sendMessage("§cUne erreur est survenue lors de la création du spawner, merci de contacter un staff.");
        return;
      }

      EntityType type = EntityType.valueOf(item.getItemMeta().getDisplayName().replaceAll("§bSpawner à ", "").toUpperCase());
      int level = Integer.parseInt(levelString);

      Spawner spawner = new Spawner(0, playerSpawner, type, level, null, false);
      spawner.create();

      Player player = event.getPlayer();
      player.sendMessage("§aVous avez créé un spawner de type: " + spawner.getType().getName() + " !");
      player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
      player.sendTitle("§aNouveau spawner !", "§2" + spawner.getType().getName() + " §a-§2 Level " + spawner.getLevel());

      event.getPlayer().setItemInHand(null);
    }
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    if (event.getBlock().getType().equals(Material.MOB_SPAWNER)) {
      event.setCancelled(true);
    }
  }

  private boolean canPose(Location location, Player player) {
    Block block = location.getBlock();
    return block.getType().equals(Material.COBBLESTONE) && FactionsBlockListener.playerCanBuildDestroyBlock(player, location, "build", true);
  }
}
