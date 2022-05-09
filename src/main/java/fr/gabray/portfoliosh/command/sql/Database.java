package fr.gabray.portfoliosh.command.sql;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Database {

    private final Map<String, Table> tables;

    public Database()
    {
        tables = new HashMap<>();
    }

    public void addTable(Table table)
    {
        tables.put(table.getName(), table);
    }

    public Collection<Table> getTables()
    {
        return tables.values();
    }

    @Nullable
    public Table getTable(String name)
    {
        return tables.get(name);
    }
}
