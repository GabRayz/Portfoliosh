package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.DbData;
import fr.gabray.portfoliosh.command.sql.ResultSet;
import fr.gabray.portfoliosh.exception.SqlException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Getter
public class OrderByBuilder implements StatementBuilder {
    private final StatementBuilder parent;
    private final List<String> columns = new ArrayList<>();
    private boolean desc = false;

    public OrderByBuilder(final StatementBuilder parent, String columnName)
    {
        this.parent = parent;
        columns.add(columnName);
    }

    public OrderByBuilder thenBy(String columnName)
    {
        columns.add(columnName);
        return this;
    }

    public OrderByBuilder desc()
    {
        desc = true;
        return this;
    }

    public LimitBuilder limit(long count)
    {
        return new LimitBuilder(this, count);
    }

    @Override
    public ResultSet execute(final Database database)
    {
        ResultSet result = parent.execute(database);

        Comparator<Map<String, DbData>> comparator = (a, b) -> {
            for (final String column : columns)
            {
                if (!result.columns().contains(column))
                    throw new SqlException("Unknown column " + column + " in order by clause");
                int compare = a.get(column).compareTo(b.get(column));
                if (compare != 0)
                    return desc ? -compare : compare;
            }
            return 0;
        };

        result.data().sort(comparator);
        return result;
    }
}
