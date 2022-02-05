package net.revelationmc.lib.storage.implementation.sql;

import net.revelationmc.lib.storage.factory.ConnectionFactory;
import net.revelationmc.lib.storage.factory.SqlConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlStorage extends AbstractSqlStorage {
    private final ConnectionFactory factory;

    public MySqlStorage(String host, int port, String database, String username, String password) {
        this.factory = new SqlConnectionFactory.Builder()
                .setPoolName(AbstractSqlStorage.POOL_NAME)
                .setHost(host)
                .setPort(port)
                .setDatabase(database)
                .setUsername(username)
                .setPassword(password)
                .build();
    }

    @Override
    public void shutdown() {
        this.factory.shutdown();
    }

    @Override
    protected Connection getConnection() {
        try {
            return this.factory.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
