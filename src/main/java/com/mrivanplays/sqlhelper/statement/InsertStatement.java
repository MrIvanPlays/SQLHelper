package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.statement.executement.StatementCompletionStage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Executor;

/**
 * Represents an sql "INSERT" statement
 */
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

    /**
     * Mark the table where we're going to insert.
     *
     * @param tableName table
     * @return this instance for chaining
     */
    public InsertStatement into(String tableName)
    {
        STATEMENT.append( tableName );
        return this;
    }

    /**
     * Mark the columns of the table which we're going to
     * insert.
     *
     * @param columns columns
     * @return this instance for chaining
     */
    public InsertStatement columns(String... columns)
    {
        STATEMENT.append( '(' ).append( String.join( ", ", columns ) ).append( ')' );
        columnSize = columns.length;
        return this;
    }

    /**
     * Executes the update.
     *
     * @param values values that we're going to insert
     * @return statement completion stage
     */
    public StatementCompletionStage<Void> executeUpdate(Object... values)
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
        return new StatementCompletionStage<>( () ->
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
