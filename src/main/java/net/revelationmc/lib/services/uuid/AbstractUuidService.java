package net.revelationmc.lib.services.uuid;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.revelationmc.lib.services.uuid.UuidService;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public abstract class AbstractUuidService implements UuidService {
    private final Cache<UUID, String> usernameCache = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();
    private final Cache<String, UUID> uuidCache = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();

    protected abstract String getRemoteUsername(UUID uuid);

    protected abstract UUID getRemoteUniqueId(String username);

    @Override
    public UUID getUniqueId(String username) {
        try {
            return this.uuidCache.get(username, () -> this.getRemoteUniqueId(username));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getUsername(UUID uuid) {
        try {
            return this.usernameCache.get(uuid, () -> this.getRemoteUsername(uuid));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
