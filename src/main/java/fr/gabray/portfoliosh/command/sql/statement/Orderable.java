package fr.gabray.portfoliosh.command.sql.statement;

public interface Orderable extends StatementBuilder {

    OrderByBuilder orderBy(String columnName);
}
