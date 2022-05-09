package fr.gabray.portfoliosh.command.sql;

import org.jetbrains.annotations.NotNull;

public abstract class DbData implements Comparable<DbData> {

    protected DbData()
    {
    }

    abstract Object get();

    @Override
    public String toString()
    {
        return String.valueOf(get());
    }

    public static DbData of(Object object)
    {
        if (object instanceof Number number)
            return new NumberDbData(number);
        else if (object instanceof String str)
            return new StringDbData(str);
        else if (object == null)
            return new StringDbData(null);
        throw new IllegalArgumentException();
    }
}

class StringDbData extends DbData {
    private final String string;

    StringDbData(final String string)
    {
        this.string = string;
    }

    @Override
    String get()
    {
        return string;
    }

    @Override
    public int compareTo(@NotNull final DbData o)
    {
        if (!(o instanceof StringDbData stringData))
            throw new IllegalArgumentException();
        return string.compareTo(stringData.string);
    }
}

class NumberDbData extends DbData {
    private final Number number;

    NumberDbData(final Number number)
    {
        this.number = number;
    }

    @Override
    Number get()
    {
        return number;
    }

    @Override
    public int compareTo(@NotNull final DbData o)
    {
        if (!(o instanceof NumberDbData numberDbData))
            throw new IllegalArgumentException();
        return Float.compare(number.floatValue(), numberDbData.number.floatValue());
    }
}