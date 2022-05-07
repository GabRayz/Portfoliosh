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

        assertEquals(TokenType.EOI, token.type());
    }

    @Test
    void ShouldReturnWordTokenWhenPopIsCalledWithInputStartingWithWord() throws IOException
    {
        Lexer lexer = new Lexer("foo");

        Token token = lexer.pop();

        assertEquals(TokenType.WORD, token.type());
        assertEquals("foo", token.value());
    }

    @Test
    void ShouldReturnEOITokenWhenPopIsLastCalledWithInputStartingWithWord() throws IOException
    {
        Lexer lexer = new Lexer("foo");

        lexer.pop();
        Token token = lexer.pop();

        assertEquals(TokenType.EOI, token.type());
    }

    @Test
    void ShouldReturn2WordsWhenPopIsCalledTwiceWith2WordsSeparatedByWhitespace() throws IOException
    {
        Lexer lexer = new Lexer("foo bar");

        Token token = lexer.pop();
        assertEquals(TokenType.WORD, token.type());
        token = lexer.pop();
        assertEquals(TokenType.WORD, token.type());
    }

    @Test
    void ShouldReturn2WordsWhenPopIsCalledTwiceWith2WordsSeparatedByManyWhitespaces() throws IOException
    {
        Lexer lexer = new Lexer("foo     \t  \r   bar");

        Token token = lexer.pop();
        assertEquals(TokenType.WORD, token.type());
        token = lexer.pop();
        assertEquals(TokenType.WORD, token.type());
    }
}
