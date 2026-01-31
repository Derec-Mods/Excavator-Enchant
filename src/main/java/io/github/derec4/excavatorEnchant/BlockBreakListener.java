package io.github.derec4.excavatorEnchant;

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
import org.bukkit.util.Vector;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        NamespacedKey key = new NamespacedKey("excavator_enchant_helper", "excavator");
        Enchantment excavatorEnchant = Registry.ENCHANTMENT.get(key);

        if (excavatorEnchant == null) {
            System.out.println("ERROR: Excavator enchantment not found in registry!");
            System.out.println("Available enchantments: " + Registry.ENCHANTMENT.stream()
                    .map(e -> e.getKey().toString())
                    .filter(s -> s.contains("excavator"))
                    .toList());
            return;
        }

        System.out.println("Found enchantment: " + excavatorEnchant.getKey());
        System.out.println("Item enchantments: " + item.getEnchantments().keySet());


        if (!isPickaxe(item.getType())) return;

        assert excavatorEnchant != null;
        if (!item.containsEnchantment(excavatorEnchant)) {
            System.out.println("BREAKPOINT THREE");
            return;
        }
        
        Block origin = event.getBlock();
        Vector direction = player.getLocation().getDirection().normalize();
        Vector primary = getPrimaryAxis(direction);
        
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    
                    Vector offset = getOffset(primary, x, y, z);
                    Block target = origin.getRelative(offset.getBlockX(), offset.getBlockY(), offset.getBlockZ());
                    
                    if (target.getType().isAir()) continue;
                    if (!target.isPreferredTool(item)) continue;
                    
                    target.breakNaturally(item);
                    damageItem(item, player);
                }
            }
        }
    }
    
    private boolean isPickaxe(Material material) {
        return material == Material.WOODEN_PICKAXE || material == Material.STONE_PICKAXE || 
               material == Material.IRON_PICKAXE || material == Material.GOLDEN_PICKAXE || 
               material == Material.DIAMOND_PICKAXE || material == Material.NETHERITE_PICKAXE;
    }
    
    private Vector getPrimaryAxis(Vector direction) {
        double absX = Math.abs(direction.getX());
        double absY = Math.abs(direction.getY());
        double absZ = Math.abs(direction.getZ());
        
        if (absX > absY && absX > absZ) return new Vector(Math.signum(direction.getX()), 0, 0);
        if (absY > absZ) return new Vector(0, Math.signum(direction.getY()), 0);
        return new Vector(0, 0, Math.signum(direction.getZ()));
    }
    
    private Vector getOffset(Vector primary, int x, int y, int z) {
        if (primary.getY() != 0) return new Vector(x, (int)primary.getY() * z, y);
        if (primary.getX() != 0) return new Vector((int)primary.getX() * z, y, x);
        return new Vector(x, y, (int)primary.getZ() * z);
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
