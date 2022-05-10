package fr.gabray.portfoliosh.command.sql.statement;

public interface Limitable extends Selectable {

    LimitBuilder limit(long count);
}
