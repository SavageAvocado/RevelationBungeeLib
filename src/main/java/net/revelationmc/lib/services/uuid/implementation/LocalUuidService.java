package net.revelationmc.lib.services.uuid.implementation;

import net.revelationmc.lib.services.uuid.AbstractUuidService;
import net.revelationmc.lib.storage.AsyncStorageContainer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Gets player information from the local database.
 * This service is internally maintained.
 */
public class LocalUuidService extends AbstractUuidService {
    private final AsyncStorageContainer asyncStorageContainer;

    public LocalUuidService(AsyncStorageContainer asyncStorageContainer) {
        this.asyncStorageContainer = asyncStorageContainer;
    }

    public CompletableFuture<Void> updateLocals(UUID uuid, String username) {
        return this.asyncStorageContainer.updateLocalUuidCache(uuid, username);
    }

    @Override
    protected UUID getRemoteUniqueId(String username) {
        return this.asyncStorageContainer.getUniqueId(username).join();
    }

    @Override
    protected String getRemoteUsername(UUID uuid) {
        return this.asyncStorageContainer.getUsername(uuid).join();
    }
}
