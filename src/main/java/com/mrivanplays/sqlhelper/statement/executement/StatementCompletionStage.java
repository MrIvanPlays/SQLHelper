package com.mrivanplays.sqlhelper.statement.executement;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a statement completion stage.
 *
 * @param <T> held value type
 */
public final class StatementCompletionStage<T>
{

    private ThrowableHolder.ThrowableCallable<T> callable;
    private ThrowableHolder.ThrowableRunnable runnable;
    private Executor async;

    public StatementCompletionStage(ThrowableHolder.ThrowableCallable<T> callable, Executor async)
    {
        this.callable = callable;
        this.async = async;
    }

    public StatementCompletionStage(ThrowableHolder.ThrowableRunnable runnable, Executor async)
    {
        this.runnable = runnable;
        this.async = async;
    }

    /**
     * Executes the completion stage synchronously
     *
     * @return held value type
     */
    public T sync()
    {
        return sync( (e) ->
        {
            e.printStackTrace();
            return null;
        } );
    }

    /**
     * Executes the completion stage synchronously, providing
     * callback to onError
     *
     * @param onError on error
     * @return held value type, or if exception, the value
     * returned from onError
     */
    public T sync(Function<Throwable, T> onError)
    {
        if ( callable != null )
        {
            try
            {
                return callable.call();
            } catch ( Throwable e )
            {
                return onError.apply( e );
            }
        } else
        {
            try
            {
                runnable.run();
                return null;
            } catch ( Throwable e )
            {
                return onError.apply( e );
            }
        }
    }

    /**
     * Executes the completion stage asynchronously without
     * providing a callback.
     */
    public void async()
    {
        async( ($) ->
        {
        } );
    }

    /**
     * Executes the completion stage asynchronously if async
     * executor is present
     *
     * @param onSuccess on success callback
     */
    public void async(Consumer<T> onSuccess)
    {
        async( onSuccess, Throwable::printStackTrace );
    }

    /**
     * Executes the completion stage asynchronously if async
     * executor is present, also providing you with onError
     * consumer.
     *
     * @param onSuccess on success callback
     * @param onError on error callback
     */
    public void async(Consumer<T> onSuccess, Consumer<Throwable> onError)
    {
        Objects.requireNonNull( async, "Cannot execute async when no executor is present." );
        async.execute( () ->
        {
            if ( callable != null )
            {
                try
                {
                    onSuccess.accept( callable.call() );
                } catch ( Throwable e )
                {
                    onError.accept( e );
                }
            } else
            {
                try
                {
                    runnable.run();
                    onSuccess.accept( null );
                } catch ( Throwable e )
                {
                    onError.accept( e );
                }
            }
        } );
    }

    /**
     * Completes the statement synchronously, providing you with
     * a combined callback for error and value.
     *
     * @param consumer callback
     */
    public void complete(BiConsumer<T, Throwable> consumer)
    {
        try
        {
            if ( callable == null )
            {
                runnable.run();
                consumer.accept( null, null );
            } else
            {
                consumer.accept( callable.call(), null );
            }
        } catch ( Throwable e )
        {
            consumer.accept( null, e );
        }
    }

    /**
     * Completes the statement asynchronously, providing you with
     * a combined callback for error and value.
     *
     * @param consumer callback
     */
    public void completeAsync(BiConsumer<T, Throwable> consumer)
    {
        Objects.requireNonNull( async, "Cannot execute async when no executor is present." );
        async.execute( () -> complete( consumer ) );
    }
}
