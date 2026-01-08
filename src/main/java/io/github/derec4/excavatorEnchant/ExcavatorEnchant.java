package io.github.derec4.excavatorEnchant;

import io.papermc.paper.datapack.Datapack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.apache.maven.model.InputLocation;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class ExcavatorEnchant extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY, event -> {

        });

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);

        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String version = getDescription().getVersion();
        console.sendMessage(Component.text("") );
        console.sendMessage(Component.text("  |_______|                             ").color(NamedTextColor.GREEN));
        console.sendMessage(Component.text("  | Derex |     Excavator Enchant v" + version).color(NamedTextColor.GREEN));
        console.sendMessage(Component.text("  |_______|     Running on " + Bukkit.getName() + " - " + Bukkit.getVersion()).color(NamedTextColor.GREEN));
        console.sendMessage(Component.text("") );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
