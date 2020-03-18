package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.statement.executement.StatementCompletionStage;
import com.mrivanplays.sqlhelper.statement.result.StatementResult;
import com.mrivanplays.sqlhelper.statement.result.StatementResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Represents an sql "SELECT" statement
 */
public final class SelectStatement
{

    private StringBuilder STATEMENT = new StringBuilder().append( "SELECT" ).append( ' ' );

    private final SQLConnectionFactory connectionFactory;
    private final Executor async;
    private Map<Integer, Object> setValues;

    public SelectStatement(SQLConnectionFactory connectionFactory, Executor async)
    {
        this.connectionFactory = connectionFactory;
        this.async = async;
        this.setValues = new HashMap<>();
    }

    /**
     * Mark that we should select every value in the table.
     *
     * @return this instance for chaining
     */
    public SelectStatement everything()
    {
        STATEMENT.append( "*" );
        return this;
    }

    /**
     * Mark which columns we should select.
     *
     * @param columns the columns we want to select
     * @return this instance for chaining
     */
    public SelectStatement columns(String... columns)
    {
        STATEMENT.append( ' ' ).append( String.join( ", ", columns ) );
        return this;
    }

    /**
     * Mark the table from which we're going to select
     *
     * @param table table name
     * @return this instance for chaining
     */
    public SelectStatement from(String table)
    {
        STATEMENT.append( ' ' ).append( "FROM" ).append( ' ' ).append( table );
        return this;
    }

    /**
     * Mark the condition which the select should follow.
     *
     * @param keys keys
     * @param values values
     * @return this instance for chaining.
     */
    public SelectStatement where(String[] keys, Object[] values)
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
     * Mark the results limit.
     *
     * @param results maximum queries we should get
     * @return this instance for chaining
     */
    public SelectStatement limit(int results)
    {
        STATEMENT.append( ' ' ).append( "LIMIT" ).append( ' ' ).append( results );
        return this;
    }

    /**
     * Executes the query.
     *
     * @return statement completion stage
     */
    public StatementCompletionStage<StatementResultSet> executeQuery()
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
                    try ( ResultSet result = statement.executeQuery() )
                    {
                        ResultSetMetaData metaData = result.getMetaData();
                        int columns = metaData.getColumnCount();
                        List<StatementResult> results = new ArrayList<>();
                        while ( result.next() )
                        {
                            Map<String, Object> map = new HashMap<>( columns );
                            for ( int i = 1; i <= columns; i++ )
                            {
                                map.put( metaData.getColumnName( i ), result.getObject( i ) );
                            }
                            results.add( new StatementResult( map ) );
                        }
                        return new StatementResultSet( results );
                    }
                } finally
                {
                    setValues.clear();
                }
            }
        }, async );
    }
}
