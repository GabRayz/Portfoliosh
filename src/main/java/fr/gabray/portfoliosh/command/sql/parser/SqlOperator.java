package fr.gabray.portfoliosh.command.sql.parser;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.BiPredicate;

public enum SqlOperator {
    EQUALS("=", String::equals),
    GREATER(">", (a, b) -> a.compareTo(b) > 0),
    LESSER("<", (a, b) -> a.compareTo(b) < 0),
    GREATER_EQUALS(">=", (a, b) -> a.compareTo(b) >= 0),
    LESSER_EQUALS("<=", (a, b) -> a.compareTo(b) <= 0),
    NOT_EQUALS("!=", (a, b) -> a.compareTo(b) != 0),
    ;
    public final String value;
    private final BiPredicate<String, String> predicate;

    SqlOperator(final String value, BiPredicate<String, String> predicate)
    {
        this.value = value;
        this.predicate = predicate;
    }

    public boolean test(String a, String b)
    {
        return predicate.test(a, b);
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
