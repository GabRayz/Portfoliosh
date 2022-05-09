package fr.gabray.portfoliosh.command.sql.parser;

import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.statement.FromBuilder;
import fr.gabray.portfoliosh.command.sql.statement.SelectBuilder;
import fr.gabray.portfoliosh.command.sql.statement.StatementBuilder;
import fr.gabray.portfoliosh.exception.ParsingException;
import fr.gabray.portfoliosh.exception.SqlException;

public class SqlParser {

    private final SqlLexer lexer;

    public SqlParser(final String input)
    {
        this.lexer = new SqlLexer(input);
    }

    public StatementBuilder parse(Database database) throws SqlException, ParsingException
    {
        SqlToken token = lexer.pop();
        if (token.getType() != SqlTokenType.RESERVED_WORD || ((SqlReservedWordToken) token).getReservedWord() != SqlReservedWord.SELECT)
            throw new ParsingException("Expected SELECT");

        SelectBuilder select = new SelectBuilder();

        token = lexer.pop();
        if (token.getType() != SqlTokenType.WORD)
            throw new ParsingException("Expected column name");
        while (token.getType() == SqlTokenType.WORD)
        {
            if (token.getValue().equals("*"))
                select.all();
            else
                select.column(token.getValue());

            token = lexer.pop();
            if (token.getType() != SqlTokenType.COMMA)
                break;
            token = lexer.pop();
        }

        if (token.getType() != SqlTokenType.RESERVED_WORD || ((SqlReservedWordToken) token).getReservedWord() != SqlReservedWord.FROM)
            throw new ParsingException("Expected FROM");

        token = lexer.pop();
        if (token.getType() != SqlTokenType.WORD)
            throw new ParsingException("Expected table name");

        FromBuilder fromBuilder = select.from(token.getValue());

        return fromBuilder;
    }
}

