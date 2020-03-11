package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.util.FutureFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class InsertStatement
{

    private StringBuilder STATEMENT = new StringBuilder().append( "INSERT INTO " );

    private final SQLConnectionFactory connectionFactory;
    private final Executor async;
    private int columnSize = 0;

    public InsertStatement(SQLConnectionFactory connectionFactory, Executor async)
    {
        this.connectionFactory = connectionFactory;
        this.async = async;
    }

    public InsertStatement into(String tableName)
    {
        STATEMENT.append( tableName );
        return this;
    }

    public InsertStatement columns(String... columns)
    {
        STATEMENT.append( '(' ).append( String.join( ", ", columns ) ).append( ')' );
        columnSize = columns.length;
        return this;
    }

    public CompletableFuture<Void> executeUpdate(Object... values) throws SQLException
    {
        STATEMENT.append( ' ' ).append( "VALUES" ).append( ' ' ).append( '(' );
        if ( columnSize == 1 )
        {
            STATEMENT.append( '?' );
        } else
        {
            for ( int i = 0; i < columnSize; i++ )
            {
                if ( i == 0 )
                {
                    STATEMENT.append( '?' ).append( ',' ).append( ' ' );
                } else if ( i == ( columnSize - 1 ) )
                {
                    STATEMENT.append( '?' );
                } else
                {
                    STATEMENT.append( '?' ).append( ',' ).append( ' ' );
                }
            }
        }
        STATEMENT.append( ')' ).append( ';' );
        return FutureFactory.makeFuture( () ->
        {
            try ( Connection connection = connectionFactory.getConnection() )
            {
                try ( PreparedStatement statement = connection.prepareStatement( STATEMENT.toString() ) )
                {
                    for ( int i = 0; i < values.length; i++ )
                    {
                        statement.setObject( i + 1, values[i] );
                    }
                    statement.executeUpdate();
                }
            }
        }, async );
    }
}
