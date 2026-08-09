package io.github.derec4.excavatorEnchant.config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public final class AllowlistReader {

    public static Set<Material> pickaxeBlocks = Set.of();
    public static Set<Material> shovelBlocks = Set.of();

    public static void load(FileConfiguration config, Logger logger) {
        ConfigurationSection root = config.getConfigurationSection("excavatable-blocks");
        if (root == null) {
            logger.warning("No excavatable-blocks in config");
            pickaxeBlocks = Set.of();
            shovelBlocks = Set.of();
            return;
        }

        pickaxeBlocks = Collections.unmodifiableSet(resolveToolSection(root.getConfigurationSection("pickaxe"), "pickaxe", logger));
        shovelBlocks = Collections.unmodifiableSet(resolveToolSection(root.getConfigurationSection("shovel"), "shovel", logger));

        logger.info("Allowlist loaded: " + pickaxeBlocks.size() + " pickaxe, " + shovelBlocks.size() + " shovel");
    }

    private static Set<Material> resolveToolSection(ConfigurationSection section, String toolName, Logger logger) {
        Set<Material> materials = new HashSet<>();
        if (section == null) {
            logger.warning("Missing excavatable-blocks." + toolName);
            return materials;
        }

        List<String> tags = section.getStringList("tags");
        for (String tagId : tags) {
            addTagMaterials(tagId, materials, toolName, logger);
        }

        List<String> materialNames = section.getStringList("materials");
        for (String materialName : materialNames) {
            addMaterial(materialName, materials, toolName, logger);
        }

        return materials;
    }

    private static void addTagMaterials(String tagId, Set<Material> materials, String toolName, Logger logger) {
        if (tagId == null || tagId.isBlank()) {
            return;
        }

        NamespacedKey key = NamespacedKey.fromString(tagId);
        if (key == null) {
            logger.warning("Bad tag '" + tagId + "' (" + toolName + ")");
            return;
        }

        Tag<Material> tag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, key, Material.class);
        if (tag == null) {
            logger.warning("Unknown tag '" + tagId + "' (" + toolName + ")");
            return;
        }

        materials.addAll(tag.getValues());
    }

    private static void addMaterial(String materialName, Set<Material> materials, String toolName, Logger logger) {
        if (materialName == null || materialName.isBlank()) {
            return;
        }

        Material material = Material.matchMaterial(materialName);
        if (material == null || !material.isBlock()) {
            logger.warning("Unknown material '" + materialName + "' (" + toolName + ")");
            return;
        }

        materials.add(material);
    }
}
