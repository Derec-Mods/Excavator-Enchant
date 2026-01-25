package io.github.derec4.excavatorEnchant;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class PluginBootstrap implements io.papermc.paper.plugin.bootstrap.PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY.newHandler(
                event -> {
                    // All code is contained here.
                    try {
                        // Retrieve the URI of the datapack folder.
                        URI uri = this.getClass().getResource("/excavator_enchant_helper").toURI();
                        // Discover the pack. The ID is set to "provided", which indicates to
                        // a server owner that your plugin includes this data pack.
                        event.registrar().discoverPack(uri, "provided");
                    } catch (URISyntaxException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        ));
    }
}
