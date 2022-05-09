package fr.gabray.portfoliosh.command.sql.statement;

public interface Orderable extends Limitable {

    OrderByBuilder orderBy(String columnName);
}
