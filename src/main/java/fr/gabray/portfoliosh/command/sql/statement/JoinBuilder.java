package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.DbData;
import fr.gabray.portfoliosh.command.sql.ResultSet;
import fr.gabray.portfoliosh.command.sql.parser.Whereable;
import fr.gabray.portfoliosh.exception.SqlException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class JoinBuilder implements Whereable {
    private final FromBuilder fromBuilder;
    private final String tableName;
    private final String left;
    private final String right;

    public JoinBuilder(final FromBuilder fromBuilder, final String tableName, final String left, final String right)
    {
        this.fromBuilder = fromBuilder;
        this.tableName = tableName;
        this.left = left;
        this.right = right;
    }

    @Override
    public ResultSet execute(final Database database)
    {
        ResultSet leftTable = fromBuilder.execute(database);

        ResultSet rightTable = new FromBuilder(tableName).execute(database);

        List<String> columns = new ArrayList<>(leftTable.columns());
        columns.addAll(computeFullName(rightTable, tableName));

        return new ResultSet(columns, performLeftJoin(leftTable, rightTable));
    }

    private List<Map<String, DbData>> performLeftJoin(ResultSet left, ResultSet right)
    {
        List<Map<String, DbData>> data = left.data();
        for (final Map<String, DbData> dataRight : right.data())
        {
            for (Map<String, DbData> dataLeft : data)
            {
                if (check(dataLeft, dataRight))
                {
                    dataLeft.putAll(dataRight.entrySet()
                                             .stream()
                                             .collect(Collectors.toMap(entry -> tableName + "." + entry.getKey(), Map.Entry::getValue)));
                }
            }
        }
        return data;
    }

    private boolean check(Map<String, DbData> leftTable, Map<String, DbData> rightTable)
    {
        String rightColumn = right.contains(".") ? right.split("\\.")[1] : right;
        if (!leftTable.containsKey(left))
            throw new SqlException("Unknown column " + left);
        if (!rightTable.containsKey(rightColumn))
            throw new SqlException("Unknown column " + right);
        return leftTable.get(left).toString().equals(rightTable.get(rightColumn).toString());
    }

    private List<String> computeFullName(final ResultSet resultSet, String tableName)
    {
        return resultSet.columns()
                        .stream()
                        .map(name -> tableName + "." + name)
                        .toList();
    }

    @Override
    public WhereBuilder where()
    {
        return new WhereBuilder(this);
    }

    @Override
    public LimitBuilder limit(long count)
    {
        return new LimitBuilder(this, count);
    }

    @Override
    public OrderByBuilder orderBy(String columnName)
    {
        return new OrderByBuilder(this, columnName);
    }
}
