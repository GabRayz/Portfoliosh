package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.ResultSet;

public interface StatementBuilder {

    default ResultSet execute(Database database)
    {
        throw new UnsupportedOperationException();
    }
}
