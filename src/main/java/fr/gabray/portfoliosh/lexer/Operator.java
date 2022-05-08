package fr.gabray.portfoliosh.lexer;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum Operator {
    AND_IF("&&"),
    OR_IF("||"),
    DSEMI(";;"),
    SEMI(";"),
    PIPE("|"),
    SAND("&"),
    ;

    public final String value;

    Operator(final String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public static boolean isOp(String str)
    {
        return Arrays.stream(values()).map(Operator::getValue)
                     .anyMatch(str::equals);
    }

    @Nullable
    public static Operator of(String str)
    {
        for (final Operator value : values())
        {
            if (value.getValue().equals(str))
                return value;
        }
        return null;
    }
}
