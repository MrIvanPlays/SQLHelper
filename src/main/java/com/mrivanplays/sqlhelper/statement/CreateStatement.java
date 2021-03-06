package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.statement.executement.StatementCompletionStage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Executor;

/**
 * Represents an "CREATE" sql statement
 */
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

    /**
     * Mark the name of the created table.
     *
     * @param name the name of the table which is going to be
     * created
     * @return this instance for chaining
     */
    public CreateStatement tableName(String name)
    {
        STATEMENT.append( name );
        return this;
    }

    /**
     * Mark the columns the table is going to have
     *
     * @param columns columns
     * @return this instance for chaining
     */
    public CreateStatement columns(String... columns)
    {
        STATEMENT.append( '(' ).append( String.join( ", ", columns ) ).append( ')' );
        return this;
    }

    /**
     * Executes the update
     *
     * @return completion stage
     */
    public StatementCompletionStage<Void> executeUpdate()
    {
        STATEMENT.append( ';' );
        return new StatementCompletionStage<>( () ->
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
