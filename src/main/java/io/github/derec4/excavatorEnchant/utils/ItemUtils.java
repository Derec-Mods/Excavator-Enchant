package io.github.derec4.excavatorEnchant.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class ItemUtils {

    public static boolean isPickaxe(Material material) {
        return material == Material.WOODEN_PICKAXE || material == Material.STONE_PICKAXE ||
                material == Material.IRON_PICKAXE || material == Material.GOLDEN_PICKAXE ||
                material == Material.DIAMOND_PICKAXE || material == Material.NETHERITE_PICKAXE;
    }

    public static void damageItem(ItemStack item, Player player) {
        damageItem(item, player, 1);
    }

    public static void damageItem(ItemStack item, Player player, int amount) {
        if (amount <= 0 || !(item.getItemMeta() instanceof Damageable meta)) {
            return;
        }

        meta.setDamage(meta.getDamage() + amount);
        item.setItemMeta(meta);

        if (meta.getDamage() >= item.getType().getMaxDurability()) {
            item.setAmount(0);
            player.updateInventory();
        }
    }
}

