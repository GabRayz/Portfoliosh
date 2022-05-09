package fr.gabray.portfoliosh.command.sql.parser;

public class SqlLexer {
    private final String input;
    private int index = 0;

    public SqlLexer(final String input)
    {
        this.input = input;
    }

    public SqlToken pop()
    {
        String word = getWord();
        if (word.isBlank())
            return new SqlToken("", SqlTokenType.EOI);

        if (word.equals(","))
            return new SqlToken(word, SqlTokenType.COMMA);

        try
        {
            SqlReservedWord sqlReservedWord = SqlReservedWord.valueOf(word.toUpperCase());
            return new SqlReservedWordToken(word, sqlReservedWord);
        }
        catch (IllegalArgumentException e)
        {
            return new SqlToken(word, SqlTokenType.WORD);
        }
    }

    String getWord()
    {
        int start = index;
        while (index < input.length())
        {
            char current = input.charAt(index);
            if (current == ' ' || current == '\t' || current == '\n' || current == ',')
            {
                if (start == index && current == ',')
                {
                    index++;
                    return current + "";
                }
                else if (start == index)
                {
                    start++;
                    index++;
                    continue;
                }
                else
                    return input.subSequence(start, index).toString();
            }

            index++;
        }
        return input.substring(start, index);
    }
}
