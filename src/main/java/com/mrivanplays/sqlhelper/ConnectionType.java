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

    /**
     * Returns the default values as an array.
     *
     * @return default values
     */
    public static ConnectionType[] defaultValues()
    {
        return new ConnectionType[] { MYSQL, MARIADB, POSTGRESQL };
    }

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

    @Override
    public boolean equals(Object o)
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        ConnectionType that = (ConnectionType) o;
        return getName().equals( that.getName() );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( getName() );
    }

    @Override
    public String toString()
    {
        return name;
    }
}
