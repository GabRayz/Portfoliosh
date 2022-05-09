package fr.gabray.portfoliosh.command.sql.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SqlLexerTest {

    @Test
    void singleWordTest()
    {
        SqlLexer lexer = new SqlLexer("foo");

        SqlToken token = lexer.pop();

        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("foo", token.getValue());
        token = lexer.pop();
        assertEquals(SqlTokenType.EOI, token.getType());
    }

    @Test
    void twoWordsTest()
    {
        SqlLexer lexer = new SqlLexer("foo bar");

        SqlToken token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("foo", token.getValue());
        token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("bar", token.getValue());
        token = lexer.pop();
        assertEquals(SqlTokenType.EOI, token.getType());
    }

    @Test
    void reservedWordTest()
    {
        SqlLexer lexer = new SqlLexer("SELECT");

        SqlToken token = lexer.pop();
        assertEquals(SqlTokenType.RESERVED_WORD, token.getType());
        assertEquals(SqlReservedWord.SELECT, ((SqlReservedWordToken) token).getReservedWord());
        token = lexer.pop();
        assertEquals(SqlTokenType.EOI, token.getType());
    }

    @Test
    void commaTest()
    {
        SqlLexer lexer = new SqlLexer("foo,bar");

        SqlToken token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("foo", token.getValue());
        token = lexer.pop();
        assertEquals(SqlTokenType.COMMA, token.getType());
        token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("bar", token.getValue());
        token = lexer.pop();
        assertEquals(SqlTokenType.EOI, token.getType());
    }

    @Test
    void threeWordsTest()
    {
        SqlLexer lexer = new SqlLexer("foo * bar");

        SqlToken token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("foo", token.getValue());
        token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("*", token.getValue());
        token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("bar", token.getValue());
        token = lexer.pop();
        assertEquals(SqlTokenType.EOI, token.getType());
    }

    @Test
    void operatorTest()
    {
        SqlLexer lexer = new SqlLexer("=");

        SqlToken token = lexer.pop();
        assertEquals(SqlTokenType.OPERATOR, token.getType());
        assertEquals(SqlOperator.EQUALS, ((OperatorSqlToken) token).getOperator());
    }

    @Test
    void doubleOperatorTest()
    {
        SqlLexer lexer = new SqlLexer("==");

        SqlToken token = lexer.pop();
        assertEquals(SqlTokenType.OPERATOR, token.getType());
        assertEquals(SqlOperator.EQUALS, ((OperatorSqlToken) token).getOperator());
    }

    @Test
    void whereConditionTest()
    {
        SqlLexer lexer = new SqlLexer("WHERE key = val");

        SqlToken token = lexer.pop();
        assertEquals(SqlTokenType.RESERVED_WORD, token.getType());
        assertEquals(SqlReservedWord.WHERE, ((SqlReservedWordToken) token).getReservedWord());

        token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("key", token.getValue());
        token = lexer.pop();
        assertEquals(SqlTokenType.OPERATOR, token.getType());
        assertEquals(SqlOperator.EQUALS, ((OperatorSqlToken) token).getOperator());
        token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("val", token.getValue());
    }

    @Test
    void conditionNoWhitespaceTest()
    {
        SqlLexer lexer = new SqlLexer("key!=val");

        SqlToken token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("key", token.getValue());
        token = lexer.pop();
        assertEquals(SqlTokenType.OPERATOR, token.getType());
        assertEquals(SqlOperator.NOT_EQUALS, ((OperatorSqlToken) token).getOperator());
        token = lexer.pop();
        assertEquals(SqlTokenType.WORD, token.getType());
        assertEquals("val", token.getValue());
    }
}