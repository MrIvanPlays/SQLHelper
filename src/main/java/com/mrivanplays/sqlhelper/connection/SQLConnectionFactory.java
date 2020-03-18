package com.mrivanplays.sqlhelper.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents a SQL connection factory, for maintaining different
 * connection types.
 */
public interface SQLConnectionFactory
{

    /**
     * Connects to the remote server.
     */
    void connect();

    /**
     * Retrieves connection to the connected remote server /
     * file.
     *
     * @return connection
     * @throws SQLException if something wrong occurs
     */
    Connection getConnection() throws SQLException;

    /**
     * Closes the connection factory, closing all the alive
     * connections
     */
    void close();

}
