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
        return Tag.BASE_STONE_OVERWORLD.isTagged(material) || Tag.TERRACOTTA.isTagged(material);
    }
}

