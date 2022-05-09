package fr.gabray.portfoliosh.command.sql.parser;

import lombok.Getter;

@Getter
public class SqlToken {
    private final String value;
    private final SqlTokenType type;

    public SqlToken(final String value, final SqlTokenType type)
    {
        this.value = value;
        this.type = type;
    }
}
