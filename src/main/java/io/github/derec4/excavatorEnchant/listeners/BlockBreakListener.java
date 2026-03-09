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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BlockBreakListener implements Listener {

    // Track blocks being broken by the excavating enchant to prevent infinite recursion with our fake event
    private final Set<Block> processingBlocks = new HashSet<>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (processingBlocks.contains(event.getBlock())) {
            return;
        }

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

        // if mined block isnt even valid, dont start
        if (!TagUtils.isMiningStone(originType) && !TagUtils.isOre(originType)
                && !TagUtils.isEndStone(originType) && !TagUtils.isNetherStone(originType)) {
            return;
        }

        int successfulBreaks = 0;

        for (Block target : BlockUtils.findBlocks(origin)) {
            if (target.getType().isAir() || target.isLiquid() || target.getType().getHardness() < 0) {
                continue;
            }

            // 2.9.2026 isPreferredTool does not work as I intended
            if (!TagUtils.isMiningStone(target.getType())
                    && !TagUtils.isOre(target.getType())
                    && !TagUtils.isEndStone(target.getType())
                    && !TagUtils.isNetherStone(target.getType())) {
                continue;
            }

            processingBlocks.add(target);

            try {
                // Trying to fake an event so coreprotect logs broken blocks
                // 2.19.2026 it works!
                BlockBreakEvent fakeEvent = new BlockBreakEvent(target, player);
                Bukkit.getPluginManager().callEvent(fakeEvent);

                if (!fakeEvent.isCancelled()) {
                    target.breakNaturally(item,true,true);
                }

                // CHECK IF THIS RESPECTS FORTUNE/SILK TOUCH
//                Collection<ItemStack> drops = target.getDrops(item, player);
//                // important for CoreProtect/ToolStats/other plugins, we want to ensure that our breaking event is as
//                // vanilla as possible and gets credited to the player, increments their mining stats, etc.
//                // Right now the durability counts work correctly at least, and drops are correct. But now need to check
//                // unbreaking and fortune calculations
//                target.breakNaturally(item, true, true);
//                drops.forEach(d -> {
//                    ItemStack dropCopy = d.clone();
//                    int newAmount = dropCopy.getAmount() - 1;
//                    if (newAmount > 0) {
//                        dropCopy.setAmount(newAmount);
//                        target.getWorld().dropItemNaturally(target.getLocation().add(0.5, 0.5, 0.5), dropCopy);
//                    }
//                });
                successfulBreaks++;
            } finally {
                // Always remove from processing set, even if an exception occurs
                processingBlocks.remove(target);
            }
        }

        // 2.9.2026 we will just apply the damage at the end, all at once
        if (successfulBreaks > 0) {
            ItemUtils.damageItem(item, player, successfulBreaks);
        }
    }
}
