package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.ResultSet;

public record LimitBuilder(StatementBuilder parent, long count) implements StatementBuilder {

    @Override
    public ResultSet execute(final Database database)
    {
        ResultSet result = parent.execute(database);
        return new ResultSet(result.columns(), result.data().subList(0, (int) count));
    }
}
