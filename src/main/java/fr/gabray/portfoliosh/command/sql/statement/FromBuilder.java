package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.*;
import fr.gabray.portfoliosh.exception.SqlException;
import lombok.Getter;

import java.util.*;

@Getter
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
        List<Map<String, DbData>> resultRows = rows.stream()
                                                   .map(row -> selectOnRow(row, columns, select.isAll()))
                                                   .toList();

        return new ResultSet(select.isAll() ? table.getColumnNames() : select.getColumns(), new ArrayList<>(resultRows));
    }

    private Map<String, DbData> selectOnRow(Row row, List<Column> columns, boolean all)
    {
        Map<String, DbData> map = new HashMap<>();
        if (all)
        {
            for (final Map.Entry<Column, DbData> columnDbDataEntry : row.getData().entrySet())
                map.put(columnDbDataEntry.getKey().name(), columnDbDataEntry.getValue());
            return map;
        }
        for (final Column column : columns)
            map.put(column.name(), row.get(column));
        return map;
    }
}
