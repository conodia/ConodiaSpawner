package fr.pandaguerrier.conodiaspawner.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
  final private ItemStack itemStack;
  final private ItemMeta itemMeta;
  final private Material material;

  public ItemBuilder(Material material) {
    this.material = material;
    this.itemStack = new ItemStack(material);
    this.itemMeta = this.itemStack.getItemMeta();
  }

  public ItemBuilder setAmount(int amount) {
    itemStack.setAmount(amount);
    return this;
  }

  public ItemBuilder setName(String displayName) {
    itemMeta.setDisplayName(displayName);
    return this;
  }

  public ItemBuilder setLore(String... lore) {
    itemMeta.setLore(java.util.Arrays.asList(lore));
    return this;
  }

  public ItemBuilder setGlow(boolean glow) {
    if (glow) {
      itemMeta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true);
      itemMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
    } else {
      itemMeta.removeEnchant(Enchantment.DURABILITY);
    }

    return this;
  }

  public ItemStack build() {
    itemStack.setItemMeta(itemMeta);
    return itemStack;
  }
}
