package fr.gabray.portfoliosh.command.sql;

import java.util.List;
import java.util.Map;

public record ResultSet(List<String> columns, List<Map<String, Object>> data) {
}
