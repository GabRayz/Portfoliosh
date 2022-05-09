package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.*;
import fr.gabray.portfoliosh.exception.SqlException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FromBuilder implements StatementBuilder {
    private final SelectBuilder select;
    private final String tableName;

    public FromBuilder(final SelectBuilder select, final String tableName)
    {
        this.select = select;
        this.tableName = tableName;
    }

    public OrderByBuilder orderBy(String columnName)
    {
        return new OrderByBuilder(this, columnName);
    }

    public LimitBuilder limit(long count)
    {
        return new LimitBuilder(this, count);
    }

    @Override
    public ResultSet execute(final Database database)
    {
        Table table = database.getTable(tableName);
        if (table == null)
            throw new SqlException("Unknown table " + tableName);

        // Get columns, throw if column does not exist
        List<Column> columns = select.getColumns()
                                     .stream()
                                     .map(columnName -> {
                                         Column column = table.getColumns().get(columnName);
                                         if (column == null)
                                             throw new SqlException("Unknown column " + columnName + " in table " + tableName);
                                         return column;
                                     })
                                     .toList();

        // Get all rows
        Collection<Row> rows = table.getRows().values();
        // copy wanted columns
        List<Map<String, Object>> resultRows = rows.stream()
                                                   .map(row -> selectOnRow(row, columns))
                                                   .toList();

        return new ResultSet(select.getColumns(), resultRows);
    }

    private Map<String, Object> selectOnRow(Row row, List<Column> columns)
    {
        Map<String, Object> map = new HashMap<>();
        for (final Column column : columns)
            map.put(column.name(), row.get(column));
        return map;
    }
}
