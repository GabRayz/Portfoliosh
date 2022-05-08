package fr.gabray.portfoliosh.lexer;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;

public class Lexer {

    private final PushBackInputStream inputStream;
    private final Deque<Token> stack = new ArrayDeque<>();

    public Lexer(@NotNull final InputStream inputStream)
    {
        this.inputStream = new PushBackInputStream(inputStream);
    }

    public Lexer(@NotNull final String input)
    {
        this(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    @NotNull
    public Token pop() throws IOException
    {
        if (!stack.isEmpty())
            return stack.pop();
        String word = getWord();

        if (word.isEmpty())
            return Token.EOI;

        Operator operator = Operator.of(word);
        if (operator != null)
            return new OperatorToken(word, operator);

        return new Token(TokenType.WORD, word);
    }

    @NotNull
    public Token peek() throws IOException
    {
        if (!stack.isEmpty())
            return stack.peek();
        return pushBack(pop());
    }

    public Token pushBack(Token token)
    {
        stack.push(token);
        return token;
    }

    private boolean isSeparator(final char c)
    {
        return c == ' ' || c == '\t' || c == '\r';
    }

    private String getWord() throws IOException
    {
        StringBuilder builder = new StringBuilder();
        char last = '\0';
        while (true)
        {
            char read = inputStream.read();
            if (read == '\0')
                return builder.toString(); // rule 1
            if (last != '\0' && Operator.isOp("" + last + read))
                return builder.append(read).toString(); // rule 2
            else if (last != '\0' && Operator.isOp("" + last))
            {
                inputStream.pushBack(read);
                return builder.toString(); // rule 3
            }
            // rule 4
            if (read == '\\')
            {
                char next = inputStream.read();
                if (next != '\n')
                    builder.append(read).append(next);
                continue;
            }
            if (read == '\"' || read == '\'')
            {
                builder.append(read);
                handleQuote(inputStream, builder, read);
                continue;
            }

            if (last != '\0' && Operator.isOp(read + ""))
            {
                inputStream.pushBack(read);
                return builder.toString();
            }

            if (isSeparator(read))
            {
                if (!builder.isEmpty())
                    return builder.toString();
                else
                    continue;
            }

            builder.append(read);
            last = read;
        }
    }

    private void handleQuote(PushBackInputStream inputStream, StringBuilder builder, char quoteChar) throws IOException
    {
        char read = inputStream.read();
        while (read != quoteChar && read != '\0')
        {
            builder.append(read);
            if (read == '\\')
                builder.append(inputStream.read());
            read = inputStream.read();
        }
        if (read == '\0')
            throw new IllegalStateException("Expected end of quote");
        builder.append(read);
    }
}

class PushBackInputStream {
    private final InputStream stream;
    private final ArrayDeque<Character> stack = new ArrayDeque<>();

    public PushBackInputStream(final InputStream stream)
    {
        this.stream = stream;
    }

    public void pushBack(char c)
    {
        stack.push(c);
    }

    public char read() throws IOException
    {
        if (!stack.isEmpty())
            return stack.pop();
        int read = stream.read();
        return read == -1 ? '\0' : (char) read;
    }
}