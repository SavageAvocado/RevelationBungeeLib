package net.revelationmc.lib.storage.implementation;

import java.util.UUID;

public interface StorageImplementation {
    void shutdown();

    void updateLocalUuidCache(UUID uuid, String username);

    String getUsername(UUID uuid);

    UUID getUniqueId(String username);
}
