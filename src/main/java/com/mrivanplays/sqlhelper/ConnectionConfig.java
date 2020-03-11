package com.mrivanplays.sqlhelper;

import java.util.Objects;

/**
 * Represents a connection config, for specifying connection data.
 */
public final class ConnectionConfig
{

    private String ip, databaseName, username, password;
    private int port;
    private ConnectionType connectionType;

    public ConnectionConfig()
    {
    }

    public ConnectionConfig(ConnectionType connectionType, String ip, int port, String databaseName, String username, String password)
    {
        this.connectionType = Objects.requireNonNull( connectionType, "connectionType" );
        this.ip = Objects.requireNonNull( ip, "ip" );
        this.databaseName = Objects.requireNonNull( databaseName, "databaseName" );
        this.username = Objects.requireNonNull( username, "username" );
        this.password = password;
        this.port = port;
    }

    /**
     * Returns the connection type, which we're gonna attempt to connect with.
     *
     * @return connection type
     */
    public ConnectionType getConnectionType()
    {
        return Objects.requireNonNull( connectionType, "Connection type not specified." );
    }

    /**
     * Sets a new connection type
     *
     * @param connectionType connection type
     */
    public void setConnectionType(ConnectionType connectionType)
    {
        this.connectionType = Objects.requireNonNull( connectionType, "connectionType" );
    }

    /**
     * Returns the ip of the sql server.
     *
     * @return ip
     */
    public String getIp()
    {
        return Objects.requireNonNull( ip, "IP not specified." );
    }

    /**
     * Sets a new ip to use for sql server connection.
     *
     * @param ip new ip
     */
    public void setIp(String ip)
    {
        this.ip = Objects.requireNonNull( ip, "ip" );
    }

    /**
     * Returns the port of the ip, which represents the sql server.
     *
     * @return port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Sets a new port
     *
     * @param port port
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Returns the name of the database.
     *
     * @return database name
     */
    public String getDatabaseName()
    {
        return Objects.requireNonNull( databaseName, "Database name not specified." );
    }

    /**
     * Sets a new database name.
     *
     * @param databaseName name of the database
     */
    public void setDatabaseName(String databaseName)
    {
        this.databaseName = Objects.requireNonNull( databaseName, "databaseName" );
    }

    /**
     * Returns the username, with which we're gonna try to authenticate with the sql server.
     *
     * @return username
     */
    public String getUsername()
    {
        return Objects.requireNonNull( username, "User name not specified." );
    }

    /**
     * Sets a new username
     *
     * @param username username
     */
    public void setUsername(String username)
    {
        this.username = Objects.requireNonNull( username, "username" );
    }

    /**
     * Returns the password, with which we're gonna try to authenticate with the sql server.
     *
     * @return password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets a new password
     *
     * @param password password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
}
