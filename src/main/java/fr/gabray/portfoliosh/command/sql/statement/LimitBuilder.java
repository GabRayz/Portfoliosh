package fr.gabray.portfoliosh.command.sql.statement;

public record LimitBuilder(StatementBuilder parent, long count) implements StatementBuilder {
}
