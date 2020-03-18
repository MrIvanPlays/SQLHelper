package com.mrivanplays.sqlhelper.statement;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.statement.executement.StatementCompletionStage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.Executor;

/**
 * Represents an "ALTER" sql statement.
 */
public final class AlterStatement
{

    private StringBuilder STATEMENT = new StringBuilder().append( "ALTER TABLE " );

    private final SQLConnectionFactory connectionFactory;
    private final Executor async;

    public AlterStatement(SQLConnectionFactory connectionFactory, Executor async)
    {
        this.connectionFactory = connectionFactory;
        this.async = async;
    }

    /**
     * Mark the statement to execute for the specified table.
     *
     * @param tableName the table you want to alter's name
     * @return this instance for chaining
     */
    public AlterStatement table(String tableName)
    {
        STATEMENT.append( tableName );
        return this;
    }

    /**
     * Mark the statement to add a column with the specified
     * column name and column type
     *
     * @param columnName name of the column you want to add
     * @param type type of the column you want to add
     * @return this instance for chaining
     */
    public AlterStatement add(String columnName, String type)
    {
        STATEMENT.append( ' ' ).append( "ADD" ).append( columnName ).append( ' ' ).append( type );
        return this;
    }

    /**
     * Mark the statement to drop the column specified.
     *
     * @param columnName the column you want to drop's name
     * @return this instance for chaining
     */
    public AlterStatement dropColumn(String columnName)
    {
        STATEMENT.append( ' ' ).append( "DROP" ).append( ' ' ).append( "COLUMN" ).append( columnName );
        return this;
    }

    /**
     * Make the statement to modify the specified column name's
     * type to the specified new type.
     *
     * @param columnName the column's name you want to change the
     * type
     * @param type the new type of the column
     * @return this instance for chaining
     */
    public AlterStatement alterColumn(String columnName, String type)
    {
        STATEMENT.append( ' ' ).append( "ALTER" ).append( ' ' ).append( "COLUMN" ).append( columnName ).append( ' ' ).append( type );
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
