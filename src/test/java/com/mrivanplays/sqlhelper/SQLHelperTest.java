package com.mrivanplays.sqlhelper;

import com.mrivanplays.sqlhelper.statement.result.StatementResult;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;

public class SQLHelperTest
{

    @Test
    public void connectAndPerformTasks()
    {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setIp( "localhost" );
        connectionConfig.setPort( 3306 );
        connectionConfig.setUsername( "root" );
        connectionConfig.setPassword( "" );
        connectionConfig.setConnectionType( ConnectionType.MYSQL );
        connectionConfig.setDatabaseName( "testserver" );

        SQLHelper sqlHelper = new SQLHelper( connectionConfig, null );
        sqlHelper.connect();
        try
        {
            sqlHelper.create().tableName( "test_table" ).columns( "id INTEGER", "name VARCHAR(255)" ).executeUpdate();
            sqlHelper.insert().into( "test_table" ).columns( "id", "name" ).executeUpdate( 1, "MrIvanPlays" );
            sqlHelper.insert().into( "test_table" ).columns( "id", "name" ).executeUpdate( 2, "Gbtank" );
            sqlHelper.select().everything().from( "test_table" ).executeQuery().whenComplete( (resultSet, error) ->
            {
                if ( error != null )
                {
                    error.printStackTrace();
                    Assert.fail();
                    return;
                }

                for ( StatementResult result : resultSet )
                {
                    System.out.println( "ID: " + result.getNumber( "id" ).intValue() );
                    System.out.println( "Name: " + result.getObject( "name" ) );
                }
            } );

            sqlHelper.create().tableName( "another_test" ).columns( "id INTEGER", "performAction BOOLEAN" ).executeUpdate();
            sqlHelper.insert().into( "another_test" ).columns( "id", "performAction" ).executeUpdate( 1, true );
            sqlHelper.select().everything().from( "another_test" ).executeQuery().whenComplete( (resultSet, error) ->
            {
                if ( error != null )
                {
                    error.printStackTrace();
                    Assert.fail();
                    return;
                }

                for ( StatementResult result : resultSet )
                {
                    System.out.println( "ID: " + result.getNumber( "id" ).intValue() );
                    System.out.println( "Perform action: " + result.getBoolean( "performAction" ) );
                }
            } );
            sqlHelper.update().tableName( "another_test" )
                .values( new String[] { "performAction" }, new Object[] { false } )
                .where( new String[] { "id" }, new Object[] { 1 } )
                .executeUpdate();
            sqlHelper.select().everything().from( "another_test" )
                .where( new String[] { "id" }, new Object[] { 1 } )
                .executeQuery()
                .whenComplete( (resultSet, error) ->
                {
                    if ( error != null )
                    {
                        error.printStackTrace();
                        Assert.fail();
                        return;
                    }

                    for ( StatementResult result : resultSet )
                    {
                        System.out.println( "ID: " + result.getNumber( "id" ).intValue() );
                        System.out.println( "Perform action: " + result.getBoolean( "performAction" ) );
                    }
                } );

            sqlHelper.create().tableName( "only_one_column" ).columns( "id INTEGER" ).executeUpdate();
            sqlHelper.insert().into( "only_one_column" ).columns( "id" ).executeUpdate( 1 );
        } catch ( SQLException e )
        {
            e.printStackTrace();
            Assert.fail();
        }
        sqlHelper.close();
    }
}
