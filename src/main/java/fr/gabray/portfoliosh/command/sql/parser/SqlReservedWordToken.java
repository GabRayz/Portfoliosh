package fr.gabray.portfoliosh.command.sql.parser;

import lombok.Getter;

@Getter
public class SqlReservedWordToken extends SqlToken {
    private final SqlReservedWord reservedWord;

    public SqlReservedWordToken(final String value, final SqlReservedWord reservedWord)
    {
        super(value, SqlTokenType.RESERVED_WORD);
        this.reservedWord = reservedWord;
    }
}
