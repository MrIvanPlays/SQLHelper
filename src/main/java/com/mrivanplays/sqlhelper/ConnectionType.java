package com.mrivanplays.sqlhelper;

import java.util.Objects;

/**
 * Represents a type of connection.
 */
public final class ConnectionType
{

    /**
     * Connection type: MySQL ; for connecting to mysql servers.
     */
    public static final ConnectionType MYSQL = new ConnectionType( "mysql" );

    /**
     * Connection type: PostgreSQL ; for connecting to postgresql servers.
     */
    public static final ConnectionType POSTGRESQL = new ConnectionType( "postgresql" );

    /**
     * Connection type: MariaDB ; for connecting to mariadb servers.
     * <p>
     * Recommended over MySQL and PostgreSQL.
     */
    public static final ConnectionType MARIADB = new ConnectionType( "mariadb" );

    private final String name;

    public ConnectionType(String name)
    {
        this.name = Objects.requireNonNull( name, "name" );
    }

    /**
     * Returns the type name of the connection.
     *
     * @return name
     */
    public String getName()
    {
        return name;
    }
}
