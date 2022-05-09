package fr.gabray.portfoliosh.command.sql.statement;

public interface Limitable extends StatementBuilder {

    LimitBuilder limit(long count);
}
