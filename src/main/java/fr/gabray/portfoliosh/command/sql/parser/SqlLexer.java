package fr.gabray.portfoliosh.command.sql.parser;

import java.util.ArrayDeque;
import java.util.Deque;

public class SqlLexer {
    private final String input;
    private int index = 0;

    private final Deque<SqlToken> stack = new ArrayDeque<>();

    public SqlLexer(final String input)
    {
        this.input = input;
    }

    public SqlToken pop()
    {
        if (!stack.isEmpty())
            return stack.pop();
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

    public SqlToken peek()
    {
        if (!stack.isEmpty())
            return stack.peek();
        return pushBack(pop());
    }

    public SqlToken pushBack(SqlToken token)
    {
        stack.push(token);
        return token;
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
