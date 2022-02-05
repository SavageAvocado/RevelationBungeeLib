package net.revelationmc.lib.listeners;

import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.revelationmc.lib.services.uuid.implementation.LocalUuidService;

import java.util.UUID;

public class ConnectionListener implements Listener {
    private final LocalUuidService localUuidService;

    public ConnectionListener(LocalUuidService localUuidService) {
        this.localUuidService = localUuidService;
    }

    @EventHandler
    public void on(LoginEvent event) {
        final String username = event.getConnection().getName();
        final UUID uuid = event.getConnection().getUniqueId();

        // Update our local UUID cache.
        this.localUuidService.updateLocals(uuid, username).join();
    }
}
