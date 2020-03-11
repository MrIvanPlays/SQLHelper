package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.statement.result.StatementResult;
import com.mrivanplays.sqlhelper.statement.result.StatementResultSet;
import com.mrivanplays.sqlhelper.util.FutureFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class SelectStatement
{

    private StringBuilder STATEMENT = new StringBuilder().append( "SELECT" ).append( ' ' );

    private final SQLConnectionFactory connectionFactory;
    private final Executor async;

    public SelectStatement(SQLConnectionFactory connectionFactory, Executor async)
    {
        this.connectionFactory = connectionFactory;
        this.async = async;
    }

    public SelectStatement everything()
    {
        STATEMENT.append( "*" );
        return this;
    }

    public SelectStatement columns(String... columns)
    {
        STATEMENT.append( ' ' ).append( String.join( ", ", columns ) );
        return this;
    }

    public SelectStatement from(String table)
    {
        STATEMENT.append( ' ' ).append( "FROM" ).append( ' ' ).append( table );
        return this;
    }

    public SelectStatement where(String[] keys, Object[] values)
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

    public CompletableFuture<StatementResultSet> executeQuery() throws SQLException
    {
        STATEMENT.append( ';' );
        return FutureFactory.makeFuture( () ->
        {
            try ( Connection connection = connectionFactory.getConnection() )
            {
                try ( PreparedStatement statement = connection.prepareStatement( STATEMENT.toString() ) )
                {
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
                }
            }
        }, async );
    }
}
