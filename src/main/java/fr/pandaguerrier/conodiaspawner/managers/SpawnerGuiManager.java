package fr.pandaguerrier.conodiaspawner.managers;

import fr.pandaguerrier.conodiaspawner.builder.ItemBuilder;
import fr.pandaguerrier.conodiaspawner.spawner.Spawner;
import fr.pandaguerrier.conodiaspawner.spawner.player.PlayerSpawner;
import fr.pandaguerrier.conodiaspawner.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class SpawnerGuiManager {
  private final PlayerSpawner owner;

  public SpawnerGuiManager(PlayerSpawner owner) {
    this.owner = owner;
  }

  public final int getMaxPage() {
    return (int) Math.ceil(this.owner.getSpawners().size() / 21.0);
  }

  public void open(int page) {
    Inventory inv = Bukkit.createInventory(null, 9 * 5, Utils.GUI_NAME + " - Page " + page +"/" + getMaxPage());
    Utils.setBorders(inv);
    setSpawners(page, inv);

    if(page == 1) {
      ItemBuilder nextPage = new ItemBuilder(Material.ARROW);
      nextPage.setName("§2Page: " + (page + 1));
      inv.setItem(41, nextPage.build());
    } else if (page == getMaxPage()) {
      ItemBuilder previousPage = new ItemBuilder(Material.ARROW);
      previousPage.setName("§2Page: " + (page - 1));
      inv.setItem(39, previousPage.build());

      ItemBuilder homePage = new ItemBuilder(Material.NETHER_STAR);
      homePage.setName("§aPage principale");
      inv.setItem(40, homePage.build());
    } else {
      ItemBuilder nextBuilder = new ItemBuilder(Material.ARROW);
      nextBuilder.setName("§2Page: " + (page + 1));
      inv.setItem(41, nextBuilder.build());

      ItemBuilder previousPage = new ItemBuilder(Material.ARROW);
      previousPage.setName("§2Page: " + (page - 1));
      inv.setItem(39, previousPage.build());

      ItemBuilder homePage = new ItemBuilder(Material.NETHER_STAR);
      homePage.setName("§aPage principale");
      inv.setItem(40, homePage.build());
    }

    this.owner.getPlayer().openInventory(inv);
  }

  public void setSpawners(int page, Inventory inv) {
    int slot = 0;
    Integer[] allowedSlots = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
    };

    List<Spawner> spawners = new ArrayList<>(this.owner.getSpawners().values()).subList((page - 1) * 21, Math.min(page * 21, this.owner.getSpawners().size()));
    for (Spawner spawner : spawners) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.MOB_SPAWNER);
        itemBuilder.setName("§2ID: " + spawner.getId());
        itemBuilder.setLore("§aType: " + spawner.getType().name(), "§aLevel: " + spawner.getLevel(), "", "§bPlacé: " + (spawner.isPlaced() ? "§aOui" : "§cNon"), (spawner.isPlaced() ? "§aX: " + spawner.getLocation().getX() + " §aY: " + spawner.getLocation().getY() + " §aZ: " + spawner.getLocation().getZ() : ""), "", "§aPremium: " + (spawner.isPremium() ? "§2Oui :)" : "§cNon :("), "§8Premium = Si le spawneur peut être cassé ou non, ou d'autre avantages.", "", "§aCliquez pour sélectionner ce spawner.");
        itemBuilder.setGlow(spawner.isPlaced());

        inv.setItem(allowedSlots[slot], itemBuilder.build());
        slot++;
    }
  }
}
