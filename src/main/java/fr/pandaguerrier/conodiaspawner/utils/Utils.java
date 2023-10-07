package fr.pandaguerrier.conodiaspawner.utils;

import fr.pandaguerrier.conodiaspawner.ConodiaSpawner;
import fr.pandaguerrier.conodiaspawner.configuration.LevelConfiguration;
import fr.pandaguerrier.conodiaspawner.spawner.level.SpawnerLevel;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Utils {
  public static String GUI_NAME = "§7Spawners";
  public static String GUI_NAME_SPAWNER = "§8Spawner §7- §8ID: ";
  public static int MAX_LEVEL = 10;


  public static ItemStack createGuiItem(Material material, String name, int data, String... lore) {
    ItemStack item = new ItemStack(material, 1, (short) data);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(name);
    meta.setLore(Arrays.asList(lore));
    item.setItemMeta(meta);
    return item;
  }

  public static void setBorders(Inventory inv) {
    inv.setItem(0, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(1, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(7, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(8, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(9, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(17, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(27, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(36, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(37, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(35, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(43, Utils.createGuiItem(Material.STAINED_GLASS_PANE, "", 3, ""));
    inv.setItem(44, Utils.createGuiItem(Material.INK_SACK, "§cRetour", 1, ""));
  }

  public static SpawnerLevel getLevelConfig(int level) {
    LevelConfiguration levelConfiguration = ConodiaSpawner.getInstance().getLevelConfiguration();
    return levelConfiguration.getSpawnerLevels().get(level);
  }
}
