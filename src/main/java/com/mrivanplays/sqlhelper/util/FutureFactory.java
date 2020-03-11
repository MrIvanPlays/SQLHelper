package com.mrivanplays.sqlhelper.util;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

public class FutureFactory
{

    public static CompletableFuture<Void> makeFuture(ThrowableRunnable runnable, Executor async) throws SQLException
    {
        if ( async != null )
        {
            return CompletableFuture.runAsync( () ->
            {
                try
                {
                    runnable.run();
                } catch ( SQLException e )
                {
                    throw new CompletionException( e );
                }
            }, async );
        } else
        {
            runnable.run();
            return CompletableFuture.completedFuture( null );
        }
    }

    public static <T> CompletableFuture<T> makeFuture(ThrowableConsumer<T> callable, Executor async) throws SQLException
    {
        if ( async != null )
        {
            return CompletableFuture.supplyAsync( () ->
            {
                try
                {
                    return callable.call();
                } catch ( SQLException e )
                {
                    throw new CompletionException( e );
                }
            }, async );
        } else
        {
            return CompletableFuture.completedFuture( callable.call() );
        }
    }

    @FunctionalInterface
    public static interface ThrowableRunnable
    {

        void run() throws SQLException;
    }

    @FunctionalInterface
    public static interface ThrowableConsumer<T>
    {

        T call() throws SQLException;
    }
}
