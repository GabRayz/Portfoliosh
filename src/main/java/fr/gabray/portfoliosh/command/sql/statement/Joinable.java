package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.parser.Whereable;

public interface Joinable extends Whereable {

    Whereable join(String tableName, String left, String right);
}
