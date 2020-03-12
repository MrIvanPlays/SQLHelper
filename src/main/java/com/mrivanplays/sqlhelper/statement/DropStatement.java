package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.util.FutureFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Represents a "DROP" sql statement, usually used to delete tables.
 */
public final class DropStatement
{

    private StringBuilder STATEMENT = new StringBuilder().append( "DROP TABLE" );

    private final SQLConnectionFactory connectionFactory;
    private final Executor async;

    public DropStatement(SQLConnectionFactory connectionFactory, Executor async)
    {
        this.connectionFactory = connectionFactory;
        this.async = async;
    }

    /**
     * Marks the statement to execute only if the specified table exists.
     *
     * @return this instance for chaining
     */
    public DropStatement ifExists()
    {
        STATEMENT.append( ' ' ).append( "IF EXISTS" );
        return this;
    }

    /**
     * Marks the statement to drop the specified table.
     *
     * @param tableName the table you want to drop's name.
     * @return this instance for chaining
     */
    public DropStatement table(String tableName)
    {
        STATEMENT.append( ' ' ).append( tableName );
        return this;
    }

    /**
     * Executes the query.
     *
     * @return future
     * @throws SQLException if an sql error occurred
     */
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
