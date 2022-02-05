package net.revelationmc.lib.services.uuid.implementation;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import net.revelationmc.lib.services.uuid.AbstractUuidService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Gets player information from the Mojang API.
 */
public class MojangUuidService extends AbstractUuidService {
    private static final String UUID_REGEX_PATTERN = "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)";

    /**
     * The base URL of the Mojang API. API docs can be found here: https://wiki.vg/Mojang_API#
     */
    private static final String MOJANG_API_URL_BASE = "https://api.mojang.com/";

    private final JsonParser parser = new JsonParser();

    @Override
    protected UUID getRemoteUniqueId(String username) {
        final JsonElement response = this.createRequest(ApiRoute.USERNAME_TO_UUID.toUrl(username));
        if (response.isJsonNull()) {
            return null;
        }
        return UUID.fromString(response.getAsJsonObject().get("id").getAsString()
                .replaceFirst(UUID_REGEX_PATTERN, "$1-$2-$3-$4-$5"));
    }

    @Override
    protected String getRemoteUsername(UUID uuid) {
        final JsonElement response = this.createRequest(ApiRoute.UUID_TO_USERNAME.toUrl(uuid.toString()));
        if (response.isJsonNull()) {
            return null;
        }
        return response.getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
    }

    private JsonElement createRequest(URI uri) {
        final HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        try {
            final HttpResponse<String> response = HttpClient.newBuilder().build()
                    .send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            final int statusCode = response.statusCode();

            // What does the status code it returned mean then? Idk, look for it here: https://wiki.vg/Mojang_API#
            // This shouldn't really ever happen though.
            if (statusCode != 200) {
                throw new IllegalStateException("API returned a status code of " + statusCode);
            }

            return this.parser.parse(response.body()).getAsJsonObject();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return JsonNull.INSTANCE;
    }

    private enum ApiRoute {
        USERNAME_TO_UUID("users/profiles/minecraft/%s"), // https://wiki.vg/Mojang_API#Username_to_UUID
        UUID_TO_USERNAME("user/profiles/%s/names"); // https://wiki.vg/Mojang_API#UUID_to_Name_History

        private final String route;

        ApiRoute(String route) {
            this.route = route;
        }

        public URI toUrl(String identifier) {
            return URI.create(String.format(MOJANG_API_URL_BASE + this.route, identifier));
        }
    }
}
