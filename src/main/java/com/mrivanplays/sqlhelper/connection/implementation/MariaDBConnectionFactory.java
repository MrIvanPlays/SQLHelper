package com.mrivanplays.sqlhelper.connection.implementation;

import com.mrivanplays.sqlhelper.ConnectionConfig;
import com.zaxxer.hikari.HikariConfig;
import java.util.Map;
import java.util.stream.Collectors;

public class MariaDBConnectionFactory extends HikariConnectionFactory
{

    public MariaDBConnectionFactory(ConnectionConfig credentials)
    {
        super( credentials );
    }

    @Override
    protected String getDriverClass()
    {
        return "org.mariadb.jdbc.MariaDbDataSource";
    }

    @Override
    protected void appendProperties(HikariConfig config, Map<String, String> properties)
    {
        if ( properties.isEmpty() )
        {
            return;
        }

        String propertiesString = properties.entrySet().stream().map( e -> e.getKey() + "=" + e.getValue() ).collect( Collectors.joining( ";" ) );

        config.addDataSourceProperty( "properties", propertiesString );
    }
}
