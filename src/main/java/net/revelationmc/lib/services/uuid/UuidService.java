package net.revelationmc.lib.services.uuid;

import java.util.UUID;

public interface UuidService {
    /**
     * Gets a player's UUID from their username.
     *
     * @param username - The player's username.
     * @return - The player's UUID.
     */
    UUID getUniqueId(String username);

    /**
     * Gets a player's username from their UUID.
     *
     * @param uuid - The player's UUID.
     * @return - The player's username.
     */
    String getUsername(UUID uuid);
}
