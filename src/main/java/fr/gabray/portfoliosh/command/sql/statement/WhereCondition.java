package fr.gabray.portfoliosh.command.sql.statement;

import fr.gabray.portfoliosh.command.sql.DbData;

import java.util.Map;

public interface WhereCondition {
    boolean check(Map<String, DbData> row);
}
