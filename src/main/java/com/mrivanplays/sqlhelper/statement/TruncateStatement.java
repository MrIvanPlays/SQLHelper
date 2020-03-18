package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.statement.executement.StatementCompletionStage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Executor;

/**
 * Represents an sql "TRUNCATE" statement
 */
public final class TruncateStatement
{

    private StringBuilder STATEMENT = new StringBuilder( "TRUNCATE TABLE " );

    private final SQLConnectionFactory connectionFactory;
    private final Executor async;

    public TruncateStatement(SQLConnectionFactory connectionFactory, Executor async)
    {
        this.connectionFactory = connectionFactory;
        this.async = async;
    }

    /**
     * Mark the table that we're going to truncate
     *
     * @param tableName name of the table
     * @return this instance for chaining
     */
    public TruncateStatement table(String tableName)
    {
        STATEMENT.append( tableName );
        return this;
    }

    /**
     * Executes the update.
     *
     * @return statement completion stage
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
