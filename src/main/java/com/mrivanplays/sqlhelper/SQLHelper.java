package com.mrivanplays.sqlhelper;

import com.mrivanplays.sqlhelper.connection.SQLConnectionFactory;
import com.mrivanplays.sqlhelper.connection.implementation.MariaDBConnectionFactory;
import com.mrivanplays.sqlhelper.connection.implementation.MySQLConnectionFactory;
import com.mrivanplays.sqlhelper.connection.implementation.PostgreSQLConnectionFactory;
import com.mrivanplays.sqlhelper.statement.AlterStatement;
import com.mrivanplays.sqlhelper.statement.CreateStatement;
import com.mrivanplays.sqlhelper.statement.DeleteStatement;
import com.mrivanplays.sqlhelper.statement.DropStatement;
import com.mrivanplays.sqlhelper.statement.InsertStatement;
import com.mrivanplays.sqlhelper.statement.SelectStatement;
import com.mrivanplays.sqlhelper.statement.TruncateStatement;
import com.mrivanplays.sqlhelper.statement.UpdateStatement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Represents the heart of the library "SQLHelper".
 */
public final class SQLHelper
{

    private Map<ConnectionType, SQLConnectionFactory> connectionFactoryRegistry;
    private ConnectionConfig connectionConfig;
    private Executor async;

    /**
     * Creates a new instance of this class.
     *
     * @param connectionConfig connection config
     * @param async executor for asynchronous queries. If you
     * want them synchronous, you can specify null.
     */
    public SQLHelper(ConnectionConfig connectionConfig, Executor async)
    {
        this.connectionConfig = Objects.requireNonNull( connectionConfig, "connectionConfig" );
        this.connectionFactoryRegistry = new HashMap<>();
        this.async = async;
        reRegisterDefaultConnectionTypes();
    }

    public SQLHelper(Executor async)
    {
        this( new ConnectionConfig(), async );
    }

    public SQLHelper()
    {
        this( new ConnectionConfig(), null );
    }

    /**
     * Connects to the remote database.
     */
    public void connect()
    {
        SQLConnectionFactory connectionFactory = connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
        connectionFactory.connect();
    }

    /**
     * Closes the connection to the remote database.
     */
    public void close()
    {
        SQLConnectionFactory connectionFactory = connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
        connectionFactory.close();
    }

    /**
     * Returns the {@link SQLConnectionFactory}, which
     * corresponds to the specified {@link ConnectionType}
     *
     * @return connection factory
     */
    public SQLConnectionFactory getConnectionFactory()
    {
        return connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
    }

    /**
     * Registers a new connection factory
     *
     * @param connectionType the connection type you want to
     * register a factory for
     * @param connectionFactory the connection factory you want
     * to register
     * @throws IllegalArgumentException if the connection type
     * has already a registered factory
     */
    public void registerConnectionFactory(ConnectionType connectionType, SQLConnectionFactory connectionFactory)
    {
        if ( connectionFactoryRegistry.containsKey( connectionType ) )
        {
            throw new IllegalArgumentException( "ConnectionType " + connectionType.getName() + " already registered." );
        }

        connectionFactoryRegistry.put( connectionType, connectionFactory );
    }

    /**
     * Returns whenever the specified {@link ConnectionType} is
     * already registered.
     *
     * @param connectionType the connection type you want to
     * check if its registered or not
     * @return <code>true</code> if registered, <code>false</code>
     * otherwise
     */
    public boolean isRegistered(ConnectionType connectionType)
    {
        return connectionFactoryRegistry.containsKey( connectionType );
    }

    /**
     * Sets a new {@link ConnectionConfig}.
     *
     * @param connectionConfig value
     */
    public void setConnectionConfig(ConnectionConfig connectionConfig)
    {
        this.connectionConfig = Objects.requireNonNull( connectionConfig );
        reRegisterDefaultConnectionTypes();
    }

    /**
     * Sets a new {@link Executor} for async queries.
     *
     * @param async executor
     */
    public void setAsyncExecutor(Executor async)
    {
        this.async = async;
    }

    /**
     * Returns an unmodifiable {@link Set}, containing all known
     * {@link ConnectionType ConnectionTypes}
     *
     * @return connection types
     */
    public Set<ConnectionType> getKnownConnectionTypes()
    {
        return Collections.unmodifiableSet( connectionFactoryRegistry.keySet() );
    }

    /**
     * Creates a new "SELECT" query object.
     *
     * @return select statement
     */
    public SelectStatement select()
    {
        SQLConnectionFactory connectionFactory = connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
        return new SelectStatement( connectionFactory, async );
    }

    /**
     * Creates a new "INSERT" query object.
     *
     * @return insert statement
     */
    public InsertStatement insert()
    {
        SQLConnectionFactory connectionFactory = connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
        return new InsertStatement( connectionFactory, async );
    }

    /**
     * Creates a new "CREATE TABLE IF NOT EXISTS" query object.
     *
     * @return create statement
     */
    public CreateStatement create()
    {
        SQLConnectionFactory connectionFactory = connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
        return new CreateStatement( connectionFactory, async );
    }

    /**
     * Creates a new "UPDATE" query object.
     *
     * @return update statement
     */
    public UpdateStatement update()
    {
        SQLConnectionFactory connectionFactory = connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
        return new UpdateStatement( connectionFactory, async );
    }

    /**
     * Creates a new "DELETE" query object.
     *
     * @return delete statement
     */
    public DeleteStatement delete()
    {
        SQLConnectionFactory connectionFactory = connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
        return new DeleteStatement( connectionFactory, async );
    }

    /**
     * Creates a new "ALTER" query object.
     *
     * @return alter statement
     */
    public AlterStatement alterTable()
    {
        SQLConnectionFactory connectionFactory = connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
        return new AlterStatement( connectionFactory, async );
    }

    /**
     * Creates a new "DROP" query object.
     *
     * @return drop statement
     */
    public DropStatement dropTable()
    {
        SQLConnectionFactory connectionFactory = connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
        return new DropStatement( connectionFactory, async );
    }

    /**
     * Creates a new "TRUNCATE" query object.
     *
     * @return truncate statement
     */
    public TruncateStatement truncate()
    {
        SQLConnectionFactory connectionFactory = connectionFactoryRegistry.get( connectionConfig.getConnectionType() );
        return new TruncateStatement( connectionFactory, async );
    }

    //

    private void reRegisterDefaultConnectionTypes()
    {
        if ( connectionFactoryRegistry.containsKey( ConnectionType.MYSQL ) )
        {
            connectionFactoryRegistry.replace( ConnectionType.MYSQL, new MySQLConnectionFactory( connectionConfig ) );
        } else
        {
            connectionFactoryRegistry.put( ConnectionType.MYSQL, new MySQLConnectionFactory( connectionConfig ) );
        }

        if ( connectionFactoryRegistry.containsKey( ConnectionType.MARIADB ) )
        {
            connectionFactoryRegistry.replace( ConnectionType.MARIADB, new MariaDBConnectionFactory( connectionConfig ) );
        } else
        {
            connectionFactoryRegistry.put( ConnectionType.MARIADB, new MariaDBConnectionFactory( connectionConfig ) );
        }

        if ( connectionFactoryRegistry.containsKey( ConnectionType.POSTGRESQL ) )
        {
            connectionFactoryRegistry.replace( ConnectionType.POSTGRESQL, new PostgreSQLConnectionFactory( connectionConfig ) );
        } else
        {
            connectionFactoryRegistry.put( ConnectionType.POSTGRESQL, new PostgreSQLConnectionFactory( connectionConfig ) );
        }
    }
}
