package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.statement.executement.StatementCompletionStage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.Executor;

/**
 * Represents an sql "UPDATE" statement
 */
public final class UpdateStatement
{

    private StringBuilder STATEMENT = new StringBuilder().append( "UPDATE " );

    private final SQLConnectionFactory connectionFactory;
    private final Executor async;
    private Map<Integer, Object> setValues;

    public UpdateStatement(SQLConnectionFactory connectionFactory, Executor async)
    {
        this.connectionFactory = connectionFactory;
        this.async = async;
        this.setValues = new HashMap<>();
    }

    /**
     * Mark the table name which we're going to update it's
     * values.
     *
     * @param tableName name of the table
     * @return this instance for chaining
     */
    public UpdateStatement tableName(String tableName)
    {
        STATEMENT.append( tableName ).append( ' ' ).append( "SET" ).append( ' ' );
        return this;
    }

    /**
     * Mark the values we're going to update
     *
     * @param keys keys
     * @param values values
     * @return this instance for chaining
     */
    public UpdateStatement values(String[] keys, Object[] values)
    {
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
     * Mark the update condition the statement should follow.
     *
     * @param keys keys
     * @param values values
     * @return this instance for chaining
     */
    public UpdateStatement where(String[] keys, Object[] values)
    {
        STATEMENT.append( ' ' ).append( "WHERE" ).append( ' ' );
        int nextIndex;
        OptionalInt highestIndexOptional = setValues.keySet().stream().mapToInt( i -> i ).max();
        if ( highestIndexOptional.isPresent() )
        {
            nextIndex = highestIndexOptional.getAsInt() + 1;
        } else
        {
            nextIndex = 1;
        }
        if ( keys.length == 1 )
        {
            String key = keys[0];
            Object value = values[0];
            STATEMENT.append( key ).append( '=' ).append( '?' );
            setValues.put( nextIndex, value );
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
            setValues.put( nextIndex + i, value );
        }
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
