package net.revelationmc.lib.storage.factory;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {
    void shutdown();

    Connection getConnection() throws SQLException;
}
