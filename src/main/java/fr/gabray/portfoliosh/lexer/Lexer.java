package fr.gabray.portfoliosh.lexer;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Lexer {

    private final InputStream inputStream;

    public Lexer(@NotNull final InputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    public Lexer(@NotNull final String input)
    {
        this.inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    }

    @NotNull
    public Token pop() throws IOException
    {
        String word = getWord();

        if (word.isEmpty())
            return Token.EOI;

        return new Token(TokenType.WORD, word);
    }

    private boolean isSeparator(final char c)
    {
        return c == ' ' || c == '\t' || c == '\r';
    }

    private String getWord() throws IOException
    {
        StringBuilder builder = new StringBuilder();
        while (true)
        {
            int charInt = inputStream.read();
            if (charInt == -1)
                return builder.toString();
            char read = (char) charInt;

            if (isSeparator(read))
            {
                if (!builder.isEmpty())
                    return builder.toString();
                else
                    continue;
            }

            builder.append(read);
        }
    }
}
