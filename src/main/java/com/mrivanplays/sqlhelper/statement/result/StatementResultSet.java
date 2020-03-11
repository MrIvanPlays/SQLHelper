package com.mrivanplays.sqlhelper.statement.result;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class StatementResultSet implements Iterable<StatementResult>
{

    private List<StatementResult> results;

    public StatementResultSet(List<StatementResult> results)
    {
        this.results = results;
    }

    public List<StatementResult> getResults()
    {
        return Collections.unmodifiableList( results );
    }

    @Override
    public Iterator<StatementResult> iterator()
    {
        return results.iterator();
    }

    @Override
    public void forEach(Consumer<? super StatementResult> action)
    {
        results.forEach( action );
    }

    @Override
    public Spliterator<StatementResult> spliterator()
    {
        return results.spliterator();
    }
}
