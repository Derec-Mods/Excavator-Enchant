package io.github.derec4.excavatorEnchant;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.Collection;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() == Material.AIR) return;

        NamespacedKey key = new NamespacedKey("excavator_enchant", "excavator");
        Enchantment excavatorEnchant = Registry.ENCHANTMENT.get(key);

        if (excavatorEnchant == null) {
            Bukkit.getConsoleSender().sendMessage("ERROR: Excavator enchantment not found in registry!");
            Bukkit.getConsoleSender().sendMessage("Available excavator enchants: " + Registry.ENCHANTMENT.stream()
                    .map(e -> e.getKey().toString())
                    .filter(s -> s.contains("excavator"))
                    .toList());
            return;
        }

        if (!isPickaxe(item.getType())) return;

        assert excavatorEnchant != null;
        if (!item.containsEnchantment(excavatorEnchant)) {
//            Bukkit.getConsoleSender().sendMessage("BREAKPOINT THREE");
            return;
        }
        
        Block origin = event.getBlock();

        for (Block target : findBlocks(origin)) {
            if (target.getType().isAir()) continue;
            if (!target.isPreferredTool(item)) continue;

            // CHECK IF THIS RESPECTS FORTUNE/SILK TOUCH
            Collection<ItemStack> drops = target.getDrops(item, player);
            target.breakNaturally();
            drops.forEach(d -> {
                ItemStack dropCopy = d.clone();
                int newAmount = dropCopy.getAmount() - 1;
                if (newAmount > 0) {
                    dropCopy.setAmount(newAmount);
                    target.getWorld().dropItemNaturally(target.getLocation().add(0.5, 0.5, 0.5), dropCopy);
                }
            });
            damageItem(item, player);
        }
    }

    private Collection<Block> findBlocks(Block b) {
        Collection<Block> blocks = new java.util.ArrayList<>(26);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    // We can skip the center block since that will break as usual
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    blocks.add(b.getRelative(x, y, z));
                }
            }
        }

        return blocks;
    }
    
    private boolean isPickaxe(Material material) {
        return material == Material.WOODEN_PICKAXE || material == Material.STONE_PICKAXE || 
               material == Material.IRON_PICKAXE || material == Material.GOLDEN_PICKAXE || 
               material == Material.DIAMOND_PICKAXE || material == Material.NETHERITE_PICKAXE;
    }
    

    private void damageItem(ItemStack item, Player player) {
        if (!(item.getItemMeta() instanceof Damageable)) return;
        
        Damageable meta = (Damageable) item.getItemMeta();
        meta.setDamage(meta.getDamage() + 1);
        item.setItemMeta(meta);
        
        if (meta.getDamage() >= item.getType().getMaxDurability()) {
            item.setAmount(0);
            player.updateInventory();
        }
    }
}
