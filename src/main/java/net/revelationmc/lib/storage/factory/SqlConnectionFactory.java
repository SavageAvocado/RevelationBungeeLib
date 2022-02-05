package net.revelationmc.lib.storage.factory;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlConnectionFactory implements ConnectionFactory {
    private final HikariDataSource source;

    private SqlConnectionFactory(HikariConfig config) {
        this.source = new HikariDataSource(config);
    }

    @Override
    public void shutdown() {
        this.source.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.source.getConnection();
    }

    public static class Builder {
        private final HikariConfig config = new HikariConfig();

        private String host;
        private int port;
        private String database;

        public Builder setPoolName(String name) {
            this.config.setPoolName(name);
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setDatabase(String database) {
            this.database = database;
            return this;
        }

        public Builder setUsername(String username) {
            this.config.setUsername(username);
            return this;
        }

        public Builder setPassword(String password) {
            this.config.setPassword(password);
            return this;
        }

        public SqlConnectionFactory build() {
            Preconditions.checkNotNull(this.host);
            Preconditions.checkPositionIndexes(this.port, 0, 65535);
            Preconditions.checkNotNull(this.database);

            this.config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", this.host, this.port, this.database));

            return new SqlConnectionFactory(this.config);
        }
    }
}
