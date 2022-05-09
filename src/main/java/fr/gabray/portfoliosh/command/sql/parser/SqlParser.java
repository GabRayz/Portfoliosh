package fr.gabray.portfoliosh.command.sql.parser;

import fr.gabray.portfoliosh.command.sql.Database;
import fr.gabray.portfoliosh.command.sql.statement.*;
import fr.gabray.portfoliosh.exception.ParsingException;
import fr.gabray.portfoliosh.exception.SqlException;

import java.util.ArrayList;
import java.util.List;

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

        token = lexer.pop();
        if (token.getType() == SqlTokenType.EOI || token.getType() == SqlTokenType.COMMA)
            return fromBuilder;
        if (token.getType() != SqlTokenType.RESERVED_WORD)
            throw new ParsingException("Unexpected token");

        SqlReservedWord reservedWord = ((SqlReservedWordToken) token).getReservedWord();
        if (reservedWord == SqlReservedWord.ORDER)
            return parseOrderBy(fromBuilder);
        return fromBuilder;
    }

    private StatementBuilder parseOrderBy(Orderable orderable) throws ParsingException
    {
        SqlToken token = lexer.pop();
        if (token.getType() != SqlTokenType.RESERVED_WORD || ((SqlReservedWordToken) token).getReservedWord() != SqlReservedWord.BY)
            throw new ParsingException("Expected BY");

        List<String> columnNames = parseColumnNames();
        String first = columnNames.get(0);
        OrderByBuilder orderBy = orderable.orderBy(first);
        for (int i = 1; i < columnNames.size(); i++)
            orderBy.thenBy(columnNames.get(i));

        token = lexer.peek();
        if (token.getType() == SqlTokenType.RESERVED_WORD && ((SqlReservedWordToken) token).getReservedWord() == SqlReservedWord.DESC)
        {
            lexer.pop();
            orderBy.desc();
        }
        return orderBy;
    }

    private List<String> parseColumnNames() throws ParsingException
    {
        List<String> list = new ArrayList<>();
        SqlToken token = lexer.pop();
        if (token.getType() != SqlTokenType.WORD)
            throw new ParsingException("Expected column name");
        while (token.getType() == SqlTokenType.WORD)
        {
            list.add(token.getValue());
            token = lexer.pop();
            if (token.getType() != SqlTokenType.COMMA)
                break;
            token = lexer.pop();
        }
        lexer.pushBack(token);
        return list;
    }
}

