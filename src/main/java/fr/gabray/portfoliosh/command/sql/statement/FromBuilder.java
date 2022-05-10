package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.*;
import fr.gabray.portfoliosh.command.sql.parser.Whereable;
import fr.gabray.portfoliosh.exception.SqlException;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class FromBuilder implements Joinable {
    private final String tableName;

    public FromBuilder(final String tableName)
    {
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
    public WhereBuilder where()
    {
        return new WhereBuilder(this);
    }

    @Override
    public Whereable join(final String tableName, final String left, final String right)
    {
        return new JoinBuilder(this, tableName, left, right);
    }

    @Override
    public ResultSet execute(final Database database)
    {
        Table table = database.getTable(tableName);
        if (table == null)
            throw new SqlException("Unknown table " + tableName);

        // Get all rows
        Collection<Row> rows = table.getRows().values();
        // copy wanted columns
        List<Map<String, DbData>> resultRows = rows.stream()
                                                   .map(this::selectOnRow)
                                                   .toList();

        return new ResultSet(table.getColumnNames(), new ArrayList<>(resultRows));
    }

    private Map<String, DbData> selectOnRow(Row row)
    {
        return row.getData()
                  .entrySet()
                  .stream()
                  .collect(Collectors.toMap(entry -> entry.getKey().name(), entry -> Objects.requireNonNullElseGet(entry.getValue(), () -> DbData.of(null))));
    }
}
