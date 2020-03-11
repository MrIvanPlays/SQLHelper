package com.mrivanplays.sqlhelper.statement.result;

import java.util.Map;

public final class StatementResult
{

    private Map<String, Object> values;

    public StatementResult(Map<String, Object> values)
    {
        this.values = values;
    }

    public Object getObject(String column)
    {
        return values.get( column );
    }

    public String getString(String column)
    {
        Object val = getObject( column );
        return val == null ? null : val instanceof String ? String.valueOf( val ) : null;
    }

    public Number getNumber(String column)
    {
        Object val = getObject( column );
        return val == null ? null : val instanceof Number ? (Number) val : null;
    }

    public Boolean getBoolean(String column)
    {
        Object val = getObject( column );
        return val == null ? null : val instanceof Boolean ? (Boolean) val : null;
    }
}
