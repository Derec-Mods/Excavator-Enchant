package io.github.derec4.excavatorEnchant.utils;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collection;

public class BlockUtils {

    /**
     * Finds all blocks in a 3x3x3 area around the given block, excluding the center block.
     * Based on Slimefun's ExplosiveTool block finding algorithm.
     *
     * @param b The center block
     * @return A collection of blocks surrounding the center block
     * @author TheBusyBiscuit
     */
    public static Collection<Block> findBlocks(Block b) {
        Collection<Block> blocks = new ArrayList<>(26);
        // preset arraylist size 26, (3 * 9 - 1)

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
}
