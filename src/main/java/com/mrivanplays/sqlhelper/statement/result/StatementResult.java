package com.mrivanplays.sqlhelper.statement.result;

import java.util.Map;

/**
 * Represents a statement result, featuring one row of all the
 * columns a table has.
 */
public final class StatementResult
{

    private Map<String, Object> values;

    public StatementResult(Map<String, Object> values)
    {
        this.values = values;
    }

    /**
     * Returns the column's value specified as a raw object.
     *
     * @param column the column you want to get the value of
     * @return value object
     */
    public Object getObject(String column)
    {
        return values.get( column );
    }

    /**
     * Returns the column's value specified as a string.
     *
     * @param column the column you want to get the value of as a
     * string
     * @return value string
     */
    public String getString(String column)
    {
        Object val = getObject( column );
        return val == null ? null : val instanceof String ? String.valueOf( val ) : null;
    }

    /**
     * Returns the column's value specified as a number
     *
     * @param column the column you want to get the value of as a
     * number
     * @return value number
     */
    public Number getNumber(String column)
    {
        Object val = getObject( column );
        return val == null ? null : val instanceof Number ? (Number) val : null;
    }

    /**
     * Returns the column's value specified as a boolean
     *
     * @param column the column you want to get the value of as a
     * boolean
     * @return value boolean
     */
    public Boolean getBoolean(String column)
    {
        Object val = getObject( column );
        return val == null ? null : val instanceof Boolean ? (Boolean) val : null;
    }
}
