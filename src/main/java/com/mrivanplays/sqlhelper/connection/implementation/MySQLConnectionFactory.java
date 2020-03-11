package com.mrivanplays.sqlhelper.connection.implementation;

import com.mrivanplays.sqlhelper.ConnectionConfig;
import com.zaxxer.hikari.HikariConfig;
import java.util.Map;

public class MySQLConnectionFactory extends HikariConnectionFactory
{

    public MySQLConnectionFactory(ConnectionConfig credentials)
    {
        super( credentials );
    }

    @Override
    protected String getDriverClass()
    {
        return "com.mysql.jdbc.jdbc2.optional.MysqlDataSource";
    }

    @Override
    protected void appendProperties(HikariConfig config, Map<String, String> properties)
    {
        properties.putIfAbsent( "cachePrepStmts", "false" );
        properties.putIfAbsent( "cacheServerConfiguration", "true" );

        super.appendProperties( config, properties );
    }
}
