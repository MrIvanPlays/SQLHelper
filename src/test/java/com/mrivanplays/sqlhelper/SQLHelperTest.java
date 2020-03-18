package com.mrivanplays.sqlhelper;

import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SQLHelperTest
{

    private SQLHelper sqlHelper;

    @Before
    public void initialize()
    {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setIp( "localhost" );
        connectionConfig.setPort( 3306 );
        connectionConfig.setUsername( "root" );
        connectionConfig.setPassword( "" );
        connectionConfig.setConnectionType( ConnectionType.MYSQL );
        connectionConfig.setDatabaseName( "testserver" );

        sqlHelper = new SQLHelper( connectionConfig, Executors.newSingleThreadExecutor() );
        sqlHelper.connect();
    }

    @After
    public void closeConnection()
    {
        sqlHelper.close();
    }

    @Test
    public void connectAndPerformTasks()
    {
        sqlHelper.create().tableName( "test_table" ).columns( "id INTEGER", "name VARCHAR(255)" ).executeUpdate();
        sqlHelper.insert().into( "test_table" ).columns( "id", "name" ).executeUpdate( 1, "MrIvanPlays" );
        sqlHelper.insert().into( "test_table" ).columns( "id", "name" ).executeUpdate( 2, "Gbtank" );
        sqlHelper.select().everything().from( "test_table" ).executeQuery().async( (resultSet) ->
        {
            Assert.assertTrue( resultSet.getResults().size() > 0 );
        }, error ->
        {
            error.printStackTrace();
            Assert.fail();
        } );

        sqlHelper.create().tableName( "another_test" ).columns( "id INTEGER", "performAction BOOLEAN" ).executeUpdate();
        sqlHelper.insert().into( "another_test" ).columns( "id", "performAction" ).executeUpdate( 1, true );
        sqlHelper.select().everything().from( "another_test" ).executeQuery().async( (resultSet) ->
        {
            Assert.assertTrue( resultSet.getResults().size() > 0 );
        }, error ->
        {
            error.printStackTrace();
            Assert.fail();
        } );
        sqlHelper.update().tableName( "another_test" )
            .values( new String[] { "performAction" }, new Object[] { false } )
            .where( new String[] { "id" }, new Object[] { 1 } )
            .executeUpdate();
        sqlHelper.select().everything().from( "another_test" )
            .where( new String[] { "id" }, new Object[] { 1 } )
            .executeQuery()
            .async( (resultSet) ->
            {
                Assert.assertTrue( resultSet.getResults().size() > 0 );
            }, error ->
            {
                error.printStackTrace();
                Assert.fail();
            } );

        sqlHelper.create().tableName( "only_one_column" ).columns( "id INTEGER" ).executeUpdate();
        sqlHelper.insert().into( "only_one_column" ).columns( "id" ).executeUpdate( 1 );
    }
}
