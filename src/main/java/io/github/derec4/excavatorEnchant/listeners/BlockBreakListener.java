package io.github.derec4.excavatorEnchant.listeners;

import io.github.derec4.excavatorEnchant.utils.BlockUtils;
import io.github.derec4.excavatorEnchant.utils.ItemUtils;
import io.github.derec4.excavatorEnchant.utils.TagUtils;
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

import java.util.Collection;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() == Material.AIR || !ItemUtils.isPickaxe(item.getType())) {
            return;
        }

        NamespacedKey key = new NamespacedKey("excavator_enchant", "excavating");
        Enchantment excavatingEnchant = Registry.ENCHANTMENT.get(key);

        if (excavatingEnchant == null) {
            Bukkit.getConsoleSender().sendMessage("ERROR: excavating enchantment not found in registry!");
            Bukkit.getConsoleSender().sendMessage("Available excavating enchants: " + Registry.ENCHANTMENT.stream()
                    .map(e -> e.getKey().toString())
                    .filter(s -> s.contains("excavating"))
                    .toList());
            return;
        }

        if (!item.containsEnchantment(excavatingEnchant)) {
            return;
        }

        Block origin = event.getBlock();
        Material originType = origin.getType();

        if (!TagUtils.isMiningStone(originType)) {
            return;
        }

        int successfulBreaks = 0;

        for (Block target : BlockUtils.findBlocks(origin)) {
            if (target.getType().isAir() || target.isLiquid()) {
                continue;
            }

            // 2.9.2026 isPreferredTool does not work as I intended
            if (!TagUtils.isMiningStone(target.getType())) {
                continue;
            }

            // CHECK IF THIS RESPECTS FORTUNE/SILK TOUCH
            Collection<ItemStack> drops = target.getDrops(item, player);
            // important for CoreProtect/ToolStats/other plugins, we want to ensure that our breaking event is as
            // vanilla as possible and gets credited to the player, increments their mining stats, etc.
            // Right now the durability counts work correctly at least, and drops are correct. But now need to check
            // unbreaking and fortune calculations
            target.breakNaturally();
            drops.forEach(d -> {
                ItemStack dropCopy = d.clone();
                int newAmount = dropCopy.getAmount() - 1;
                if (newAmount > 0) {
                    dropCopy.setAmount(newAmount);
                    target.getWorld().dropItemNaturally(target.getLocation().add(0.5, 0.5, 0.5), dropCopy);
                }
            });
            successfulBreaks++;
        }

        // 2.9.2026 we will just apply the damage at the end, all at once
        if (successfulBreaks > 0) {
            ItemUtils.damageItem(item, player, successfulBreaks);
        }
    }
}
