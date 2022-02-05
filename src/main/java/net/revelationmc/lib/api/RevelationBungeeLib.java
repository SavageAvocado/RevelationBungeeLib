package net.revelationmc.lib.api;

import net.revelationmc.lib.services.uuid.UuidService;

public interface RevelationBungeeLib {
    /**
     * This UUID service requests values from the Mojang API.
     * This service will be more accurate, however requests to it are limited my Mojang.
     *
     * @return - The Mojang uuid service implementation.
     */
    UuidService getMojangUuidService();

    /**
     * This UUID service gets values from the local database.
     * To avoid rate limits of the Mojang API, it is recommended to use this service.
     *
     * @return - The local uuid service implementation.
     */
    UuidService getLocalUuidService();
}
