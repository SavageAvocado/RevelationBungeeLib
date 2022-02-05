package net.revelationmc.lib.storage.implementation.sql;

import net.revelationmc.lib.storage.implementation.StorageImplementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public abstract class AbstractSqlStorage implements StorageImplementation {
    protected static final String POOL_NAME = "revelation-bungee-lib-hikari";

    // Create statements:
    private static final String CREATE_UUID_CACHE_TABLE = "CREATE TABLE IF NOT EXISTS uuid_cache (id VARCHAR(36), name VARCHAR(16));";

    // Insert statements:
    private static final String INSERT_UUID_CACHE = "INSERT INTO uuid_cache (id, name) VALUES (?, ?);";

    // Select statements:
    private static final String SELECT_USERNAME = "SELECT name FROM uuid_cache WHERE id = ?;";
    private static final String SELECT_UUID = "SELECT id FROM uuid_cache WHERE name = ?;";

    public AbstractSqlStorage() {
        this.setupTables();
    }

    private void setupTables() {
        try (final Connection connection = this.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(CREATE_UUID_CACHE_TABLE)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLocalUuidCache(UUID uuid, String username) {
        try (final Connection connection = this.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(INSERT_UUID_CACHE)) {
                statement.setString(1, uuid.toString());
                statement.setString(2, username);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUsername(UUID uuid) {
        try (final Connection connection = this.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(SELECT_USERNAME)) {
                statement.setString(1, uuid.toString());
                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return result.getString("name");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UUID getUniqueId(String username) {
        try (final Connection connection = this.getConnection()) {
            try (final PreparedStatement statement = connection.prepareStatement(SELECT_UUID)) {
                statement.setString(1, username);
                try (final ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        return UUID.fromString(result.getString("id"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract Connection getConnection();
}
