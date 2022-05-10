package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.ResultSet;
import fr.gabray.portfoliosh.exception.SqlException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SelectBuilder implements StatementBuilder {
    private final Selectable selectable;
    private final List<String> columns;
    private boolean all;

    public SelectBuilder(final Selectable selectable, final List<String> columns, final boolean all)
    {
        this.selectable = selectable;
        this.columns = columns;
        this.all = all;
    }

    public SelectBuilder(final Selectable selectable)
    {
        this.selectable = selectable;
        this.columns = List.of();
    }

    public SelectBuilder all()
    {
        this.all = true;
        return this;
    }

    public SelectBuilder column(String columnName)
    {
        columns.add(columnName);
        return this;
    }

    @Override
    public ResultSet execute(final Database database)
    {
        ResultSet resultSet = selectable.execute(database);
        if (all)
            return resultSet;

        List<String> newColumns = new ArrayList<>();
        for (final String column : columns)
        {
            if (resultSet.columns().contains(column))
                newColumns.add(column);
            else
                throw new SqlException("Unknown column " + column);
        }

        resultSet.data().forEach(row -> row.keySet().removeIf(column -> !columns.contains(column)));
        return new ResultSet(newColumns, resultSet.data());
    }
}
