package fr.gabray.portfoliosh.lexer;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LexerTest {

    @Test
    void shouldReturnEOITokenWhenPopIsCalledWithEmptyInput() throws IOException
    {
        Lexer lexer = new Lexer("");

        Token token = lexer.pop();

        assertEquals(TokenType.EOI, token.getType());
    }

    @Test
    void ShouldReturnWordTokenWhenPopIsCalledWithInputStartingWithWord() throws IOException
    {
        Lexer lexer = new Lexer("foo");

        Token token = lexer.pop();

        assertEquals(TokenType.WORD, token.getType());
        assertEquals("foo", token.getValue());
    }

    @Test
    void ShouldReturnEOITokenWhenPopIsLastCalledWithInputStartingWithWord() throws IOException
    {
        Lexer lexer = new Lexer("foo");

        lexer.pop();
        Token token = lexer.pop();

        assertEquals(TokenType.EOI, token.getType());
    }

    @Test
    void ShouldReturn2WordsWhenPopIsCalledTwiceWith2WordsSeparatedByWhitespace() throws IOException
    {
        Lexer lexer = new Lexer("foo bar");

        Token token = lexer.pop();
        assertEquals(TokenType.WORD, token.getType());
        token = lexer.pop();
        assertEquals(TokenType.WORD, token.getType());
    }

    @Test
    void doubleOperatorTest() throws IOException
    {
        Lexer lexer = new Lexer(";;");

        Token token = lexer.pop();
        assertEquals(TokenType.OPERATOR, token.getType());
        assertEquals(Operator.DSEMI, ((OperatorToken) token).getOperator());
    }

    @Test
    void simpleOperatorTest() throws IOException
    {
        Lexer lexer = new Lexer(";");

        Token token = lexer.pop();
        assertEquals(TokenType.OPERATOR, token.getType());
        assertEquals(Operator.SEMI, ((OperatorToken) token).getOperator());
    }

    @Test
    void operatorDelimiterTest() throws IOException
    {
        Lexer lexer = new Lexer("foo||bar");

        Token token = lexer.pop();
        assertEquals(TokenType.WORD, token.getType());
        token = lexer.pop();
        assertEquals(TokenType.OPERATOR, token.getType());
        assertEquals(Operator.OR_IF, ((OperatorToken) token).getOperator());
        token = lexer.pop();
        assertEquals(TokenType.WORD, token.getType());
        assertEquals("bar", token.getValue());
    }
}
