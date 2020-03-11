package com.mrivanplays.sqlhelper.statement.result;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Represents a SELECT statement's result set.
 */
public final class StatementResultSet implements Iterable<StatementResult>
{

    private List<StatementResult> results;

    public StatementResultSet(List<StatementResult> results)
    {
        this.results = results;
    }

    /**
     * Returns a unmodifiable list of the results queried.
     *
     * @return results
     */
    public List<StatementResult> getResults()
    {
        return Collections.unmodifiableList( results );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<StatementResult> iterator()
    {
        return results.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forEach(Consumer<? super StatementResult> action)
    {
        results.forEach( action );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Spliterator<StatementResult> spliterator()
    {
        return results.spliterator();
    }
}
