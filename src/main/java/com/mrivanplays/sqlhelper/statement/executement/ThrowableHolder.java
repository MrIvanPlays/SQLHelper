package com.mrivanplays.sqlhelper.statement.executement;

import java.sql.SQLException;

/**
 * Represents a holder for throwable utilities.
 */
public interface ThrowableHolder
{

    /**
     * Represents a throwable runnable, a {@link
     * FunctionalInterface} which's functional method is {@link
     * #run()}, which can throw an {@link SQLException}
     */
    @FunctionalInterface
    interface ThrowableRunnable
    {

        void run() throws SQLException;
    }

    /**
     * Represents a throwable callable, a {@link
     * FunctionalInterface} which's functional method is {@link
     * #call()}, which can throw an {@link SQLException}
     *
     * @param <T> value
     */
    @FunctionalInterface
    interface ThrowableCallable<T>
    {

        T call() throws SQLException;
    }
}
