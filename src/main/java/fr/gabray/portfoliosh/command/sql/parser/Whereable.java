package fr.gabray.portfoliosh.command.sql.parser;

import fr.gabray.portfoliosh.command.sql.statement.Orderable;
import fr.gabray.portfoliosh.command.sql.statement.WhereBuilder;

public interface Whereable extends Orderable {

    WhereBuilder where();
}
