package fr.gabray.portfoliosh.command.sql;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class Row {

    private final Map<Column, Object> data;

    public Row(final Map<Column, Object> data)
    {
        this.data = data;
    }

    public Map<Column, Object> getData()
    {
        return data;
    }

    public Object get(Column column)
    {
        return data.get(column);
    }

    public Map<Column, Object> get(Collection<Column> columns)
    {
        return data.entrySet().stream()
                   .filter(entry -> columns.contains(entry.getKey()))
                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
