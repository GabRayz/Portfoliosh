package fr.gabray.portfoliosh.lexer;

public class OperatorToken extends Token {

    private final Operator operator;

    public OperatorToken(final String value, final Operator operator)
    {
        super(TokenType.OPERATOR, value);
        this.operator = operator;
    }

    public Operator getOperator()
    {
        return operator;
    }
}
