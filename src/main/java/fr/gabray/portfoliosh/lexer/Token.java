package fr.gabray.portfoliosh.lexer;

import java.util.Objects;

public class Token {
    public static final Token EOI = new Token(TokenType.EOI, "\0");
    private final TokenType type;
    private final String value;

    public Token(TokenType type, String value)
    {
        this.type = type;
        this.value = value;
    }

    public TokenType getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (Token) obj;
        return Objects.equals(this.type, that.type) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(type, value);
    }

    @Override
    public String toString()
    {
        return "Token[" +
                "type=" + type + ", " +
                "value=" + value + ']';
    }
}
