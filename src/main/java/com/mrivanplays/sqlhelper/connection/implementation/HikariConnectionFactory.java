package com.mrivanplays.sqlhelper.connection.implementation;

import com.mrivanplays.sqlhelper.ConnectionConfig;
import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class HikariConnectionFactory implements SQLConnectionFactory
{

    private HikariDataSource hikari;
    protected ConnectionConfig credentials;

    public HikariConnectionFactory(ConnectionConfig credentials)
    {
        this.credentials = credentials;
    }

    protected String getDriverClass()
    {
        return null;
    }

    protected void appendProperties(HikariConfig config, Map<String, String> properties)
    {
        for ( Map.Entry<String, String> propertyEntry : properties.entrySet() )
        {
            config.addDataSourceProperty( propertyEntry.getKey(), propertyEntry.getValue() );
        }
    }

    protected void appendConfigurationInfo(HikariConfig config)
    {
        config.setDataSourceClassName( getDriverClass() );
        config.addDataSourceProperty( "serverName", credentials.getIp() );
        config.addDataSourceProperty( "port", credentials.getPort() );
        config.addDataSourceProperty( "databaseName", credentials.getDatabaseName() );
        config.setUsername( credentials.getUsername() );
        config.setPassword( credentials.getPassword() );
    }

    @Override
    public void connect()
    {
        HikariConfig config = new HikariConfig();
        appendConfigurationInfo( config );

        Map<String, String> properties = new HashMap<>();
        properties.put( "useUnicode", "true" );
        properties.put( "characterEncoding", "utf8" );
        appendProperties( config, properties );

        config.setMaximumPoolSize( 10 );
        config.setMinimumIdle( 10 );
        config.setMaxLifetime( 1800000 );
        config.setConnectionTimeout( 5000 );
        config.setInitializationFailTimeout( -1 );

        hikari = new HikariDataSource( config );
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return hikari.getConnection();
    }

    @Override
    public void close()
    {
        hikari.close();
    }
}
