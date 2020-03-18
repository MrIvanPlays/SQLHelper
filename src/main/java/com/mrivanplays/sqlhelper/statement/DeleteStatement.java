package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.statement.executement.StatementCompletionStage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Represents an "DELETE" sql statement
 */
public final class DeleteStatement
{

    private StringBuilder STATEMENT = new StringBuilder().append( "DELETE FROM " );
    private Map<Integer, Object> setValues;

    private final SQLConnectionFactory connectionFactory;
    private final Executor async;

    public DeleteStatement(SQLConnectionFactory connectionFactory, Executor async)
    {
        this.connectionFactory = connectionFactory;
        this.async = async;
        this.setValues = new HashMap<>();
    }

    /**
     * Mark the table from which we're going to delete.
     *
     * @param tableName the table name
     * @return this instance for chaining
     */
    public DeleteStatement from(String tableName)
    {
        STATEMENT.append( tableName );
        return this;
    }

    /**
     * Mark the sql statement's "where" values
     *
     * @param keys keys
     * @param values values
     * @return this instance for chaining
     */
    public DeleteStatement where(String[] keys, Object[] values)
    {
        STATEMENT.append( ' ' ).append( "WHERE" ).append( ' ' );
        if ( keys.length == 1 )
        {
            String key = keys[0];
            Object value = values[0];
            STATEMENT.append( key ).append( '=' ).append( '?' );
            setValues.put( 1, value );
            return this;
        }
        for ( int i = 0; i < keys.length; i++ )
        {
            String key = keys[i];
            Object value = values[i];
            if ( i == 0 )
            {
                STATEMENT.append( key ).append( '=' ).append( '?' ).append( ',' );
            } else if ( i == ( keys.length - 1 ) )
            {
                // last key=value pair
                STATEMENT.append( ' ' ).append( key ).append( '=' ).append( '?' );
            } else
            {
                STATEMENT.append( ' ' ).append( key ).append( '=' ).append( '?' ).append( ',' );
            }
            setValues.put( i + 1, value );
        }
        return this;
    }

    /**
     * Executes the update.
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
                    for ( Map.Entry<Integer, Object> entry : setValues.entrySet() )
                    {
                        statement.setObject( entry.getKey(), entry.getValue() );
                    }
                    statement.executeUpdate();
                } finally
                {
                    setValues.clear();
                }
            }
        }, async );
    }
}
