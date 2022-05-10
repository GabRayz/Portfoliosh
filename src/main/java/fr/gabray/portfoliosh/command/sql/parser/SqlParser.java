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
        Whereable whereable = parseJoin(fromBuilder);
        Orderable orderable = parseWhere(whereable);
        Limitable limitable = parseOrderBy(orderable);
        return parseLimit(limitable);
    }

    private Whereable parseJoin(Joinable joinable) throws ParsingException
    {
        SqlToken token = lexer.peek();
        if (token.getType() != SqlTokenType.RESERVED_WORD || ((SqlReservedWordToken) token).getReservedWord() != SqlReservedWord.JOIN)
            return joinable;
        lexer.pop();

        SqlToken tableName = lexer.pop();
        if (tableName.getType() != SqlTokenType.WORD)
            throw new ParsingException("Expected table name");

        token = lexer.pop();
        if (token.getType() != SqlTokenType.RESERVED_WORD || ((SqlReservedWordToken) token).getReservedWord() != SqlReservedWord.ON)
            throw new ParsingException("Expected ON");

        SqlToken left = lexer.pop();
        if (left.getType() != SqlTokenType.WORD)
            throw new ParsingException("Expected column name");
        token = lexer.pop();
        if (token.getType() != SqlTokenType.OPERATOR || ((OperatorSqlToken) token).getOperator() != SqlOperator.EQUALS)
            throw new ParsingException("Expected =");
        SqlToken right = lexer.pop();
        if (right.getType() != SqlTokenType.WORD)
            throw new ParsingException("Expected column name");

        return joinable.join(tableName.getValue(), left.getValue(), right.getValue());
    }

    private Orderable parseWhere(Whereable whereable) throws ParsingException
    {
        SqlToken token = lexer.peek();
        if (token.getType() != SqlTokenType.RESERVED_WORD || ((SqlReservedWordToken) token).getReservedWord() != SqlReservedWord.WHERE)
            return whereable;
        lexer.pop();

        WhereBuilder where = whereable.where();
        SqlReservedWord currentOp = null;
        while (true)
        {
            token = lexer.pop();
            if (token.getType() != SqlTokenType.WORD)
                throw new ParsingException("Expected column name or value");
            String left = token.getValue();
            token = lexer.pop();
            if (token.getType() != SqlTokenType.OPERATOR)
                throw new ParsingException("Expected operator");
            SqlOperator operator = ((OperatorSqlToken) token).getOperator();

            String right = null;
            if (operator == SqlOperator.IS)
            {
                token = lexer.pop();
                boolean not = false;
                if (token.getValue().equals("not"))
                {
                    not = true;
                    token = lexer.pop();
                }
                if (!token.getValue().equals("null"))
                    throw new ParsingException("Expected 'null'");
                operator = not ? SqlOperator.IS_NOT_NULL : SqlOperator.IS_NULL;
            }
            else
            {
                token = lexer.pop();
                if (token.getType() != SqlTokenType.WORD)
                    throw new ParsingException("Expected column name or value");
                right = token.getValue();
            }

            (currentOp == null ? where.column(left) : where.op(currentOp, left))
                    .operator(operator)
                    .column(right);

            token = lexer.peek();
            if (!(token instanceof SqlReservedWordToken reservedWordToken) || (reservedWordToken.getReservedWord() != SqlReservedWord.AND && reservedWordToken.getReservedWord() != SqlReservedWord.OR))
                break;
            lexer.pop();
            currentOp = ((SqlReservedWordToken) token).getReservedWord();
        }
        return where;
    }

    private Limitable parseOrderBy(Orderable orderable) throws ParsingException
    {
        SqlToken token = lexer.peek();
        if (token.getType() != SqlTokenType.RESERVED_WORD || ((SqlReservedWordToken) token).getReservedWord() != SqlReservedWord.ORDER)
            return orderable;
        lexer.pop();
        token = lexer.pop();
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

    private StatementBuilder parseLimit(Limitable limitable) throws ParsingException
    {
        SqlToken token = lexer.peek();
        if (token.getType() != SqlTokenType.RESERVED_WORD || ((SqlReservedWordToken) token).getReservedWord() != SqlReservedWord.LIMIT)
            return limitable;
        lexer.pop();
        token = lexer.pop();
        if (token.getType() != SqlTokenType.WORD)
            throw new ParsingException("Expected number");
        try
        {
            int limit = Integer.parseInt(token.getValue());
            return limitable.limit(limit);
        }
        catch (NumberFormatException e)
        {
            throw new ParsingException("Invalid number");
        }
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

