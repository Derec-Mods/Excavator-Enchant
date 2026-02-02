package io.github.derec4.excavatorEnchant.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class ItemUtils {

    /**
     * Checks if the given material is a pickaxe.
     *
     * @param material The material to check
     * @return true if the material is any type of pickaxe, false otherwise
     */
    public static boolean isPickaxe(Material material) {
        return material == Material.WOODEN_PICKAXE || material == Material.STONE_PICKAXE ||
                material == Material.IRON_PICKAXE || material == Material.GOLDEN_PICKAXE ||
                material == Material.DIAMOND_PICKAXE || material == Material.NETHERITE_PICKAXE;
    }

    /**
     * Damages an item by 1 durability point. If the item reaches maximum durability,
     * it will be removed from the player's inventory.
     *
     * @param item   The item to damage
     * @param player The player holding the item
     */
    public static void damageItem(ItemStack item, Player player) {
        if (!(item.getItemMeta() instanceof Damageable meta)) {
            return;
        }

        meta.setDamage(meta.getDamage() + 1);
        item.setItemMeta(meta);

        if (meta.getDamage() >= item.getType().getMaxDurability()) {
            item.setAmount(0);
            player.updateInventory();
        }
    }
}

