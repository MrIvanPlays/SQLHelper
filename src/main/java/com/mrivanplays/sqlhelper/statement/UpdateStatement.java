package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.util.FutureFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class UpdateStatement
{

    private StringBuilder STATEMENT = new StringBuilder().append( "UPDATE " );

    private final SQLConnectionFactory connectionFactory;
    private final Executor async;

    public UpdateStatement(SQLConnectionFactory connectionFactory, Executor async)
    {
        this.connectionFactory = connectionFactory;
        this.async = async;
    }

    public UpdateStatement tableName(String tableName)
    {
        STATEMENT.append( tableName ).append( ' ' ).append( "SET" ).append( ' ' );
        return this;
    }

    public UpdateStatement values(String[] keys, Object[] values)
    {
        if ( keys.length == 1 )
        {
            String key = keys[0];
            Object value = values[0];
            STATEMENT.append( key ).append( '=' ).append( value );
            return this;
        }
        for ( int i = 0; i < keys.length; i++ )
        {
            String key = keys[i];
            Object value = values[i];
            if ( i == 0 )
            {
                STATEMENT.append( key ).append( '=' ).append( value ).append( ',' );
            } else if ( i == ( keys.length - 1 ) )
            {
                // last key=value pair
                STATEMENT.append( ' ' ).append( key ).append( '=' ).append( value );
            } else
            {
                STATEMENT.append( ' ' ).append( key ).append( '=' ).append( value ).append( ',' );
            }
        }
        return this;
    }

    public UpdateStatement where(String[] keys, Object[] values)
    {
        STATEMENT.append( ' ' ).append( "WHERE" ).append( ' ' );
        if ( keys.length == 1 )
        {
            String key = keys[0];
            Object value = values[0];
            STATEMENT.append( key ).append( '=' ).append( value );
            return this;
        }
        for ( int i = 0; i < keys.length; i++ )
        {
            String key = keys[i];
            Object value = values[i];
            if ( i == 0 )
            {
                STATEMENT.append( key ).append( '=' ).append( value ).append( ',' );
            } else if ( i == ( keys.length - 1 ) )
            {
                // last key=value pair
                STATEMENT.append( ' ' ).append( key ).append( '=' ).append( value );
            } else
            {
                STATEMENT.append( ' ' ).append( key ).append( '=' ).append( value ).append( ',' );
            }
        }
        return this;
    }

    public CompletableFuture<Void> executeUpdate() throws SQLException
    {
        STATEMENT.append( ';' );
        return FutureFactory.makeFuture( () ->
        {
            try ( Connection connection = connectionFactory.getConnection() )
            {
                try ( PreparedStatement statement = connection.prepareStatement( STATEMENT.toString() ) )
                {
                    statement.executeUpdate();
                }
            }
        }, async );
    }
}
