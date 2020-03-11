package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.util.FutureFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class CreateStatement
{

    private StringBuilder STATEMENT = new StringBuilder().append( "CREATE TABLE IF NOT EXISTS " );

    private final SQLConnectionFactory connectionFactory;
    private final Executor async;

    public CreateStatement(SQLConnectionFactory connectionFactory, Executor async)
    {
        this.connectionFactory = connectionFactory;
        this.async = async;
    }

    public CreateStatement tableName(String name)
    {
        STATEMENT.append( name );
        return this;
    }

    public CreateStatement columns(String... columns)
    {
        STATEMENT.append( '(' ).append( String.join( ", ", columns ) ).append( ')' );
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
