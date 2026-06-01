package io.github.derec4.excavatorEnchant.utils;

import org.bukkit.Material;
import org.bukkit.Tag;

public class TagUtils {

    public static final Tag<Material> MINEABLE_PICKAXE = Tag.MINEABLE_PICKAXE;

    /**
     * Later I'll think of a better name + implementation, for now this is a way to isolate the excavator mining to
     * just the type of block you are trying to excavate
     * @param material
     * @return
     */
    public static boolean isMiningStone (Material material) {
        return Tag.BASE_STONE_OVERWORLD.isTagged(material) ||
                Tag.TERRACOTTA.isTagged(material) ||
                material.equals(Material.SANDSTONE) ||
                material.equals(Material.RED_SANDSTONE);
    }

    public static boolean isOre(Material material) {
        return Tag.COAL_ORES.isTagged(material) ||
                Tag.IRON_ORES.isTagged(material) ||
                Tag.COPPER_ORES.isTagged(material) ||
                Tag.GOLD_ORES.isTagged(material) ||
                Tag.REDSTONE_ORES.isTagged(material) ||
                Tag.LAPIS_ORES.isTagged(material) ||
                Tag.DIAMOND_ORES.isTagged(material) ||
                Tag.EMERALD_ORES.isTagged(material) ||
                material == Material.NETHER_QUARTZ_ORE;
    }

    /**
     * Blocks (in bulk) shovel mines
     * @param material
     * @return
     */
    public static boolean isShovelBlock(Material material) {
        return Tag.DIRT.isTagged(material) ||
                Tag.SAND.isTagged(material) ||
                material == Material.GRAVEL ||
                material == Material.CLAY ||
                material == Material.SOUL_SOIL ||
                material == Material.SOUL_SAND ||
                material == Material.MUD ||
                material == Material.MYCELIUM ||
                material == Material.GRASS_BLOCK ||
                material == Material.PODZOL ||
                material == Material.COARSE_DIRT ||
                material == Material.ROOTED_DIRT;
    }

    /**
     * Meant for end stones, but right now there is only one, well, end stone lol
     * @param material
     * @return
     */
    public static boolean isEndStone (Material material) {
        return material.equals(Material.END_STONE);
    }

    /**
     * Nether pickaxe blocks
     * @param material
     * @return
     */
    public static boolean isNetherStone (Material material) {
        return Tag.BASE_STONE_NETHER.isTagged(material) || Tag.NYLIUM.isTagged(material) || material.equals(Material.MAGMA_BLOCK);
    }
}
