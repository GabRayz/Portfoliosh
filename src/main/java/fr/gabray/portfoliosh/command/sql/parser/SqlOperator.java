package fr.gabray.portfoliosh.command.sql.parser;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum SqlOperator {
    EQUALS("="),
    GREATER(">"),
    LESSER("<"),
    GREATER_EQUALS(">="),
    LESSER_EQUALS("<="),
    NOT_EQUALS("!="),
    ;
    public final String value;

    SqlOperator(final String value)
    {
        this.value = value;
    }

    @Nullable
    public static SqlOperator of(String value)
    {
        for (final SqlOperator sqlOperator : values())
        {
            if (sqlOperator.value.equals(value))
                return sqlOperator;
        }
        return null;
    }

    public String getValue()
    {
        return value;
    }

    public static boolean isPartOfOp(String value)
    {
        return Arrays.stream(values()).map(SqlOperator::getValue).anyMatch(val -> val.startsWith(value));
    }
}
