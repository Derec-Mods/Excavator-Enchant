package io.github.derec4.excavatorEnchant;

import io.github.derec4.excavatorEnchant.listeners.BlockBreakListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExcavatorEnchant extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);

        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String version = getDescription().getVersion();
        console.sendMessage(Component.text(""));
        console.sendMessage(Component.text("  |_______|                             ").color(NamedTextColor.GREEN));
        console.sendMessage(Component.text("  | Derex |     Excavator Enchant v" + version).color(NamedTextColor.GREEN));
        console.sendMessage(Component.text("  |_______|     Running on " + Bukkit.getName() + " - " + Bukkit.getVersion()).color(NamedTextColor.GREEN));
        console.sendMessage(Component.text(""));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
