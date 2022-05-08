package fr.gabray.portfoliosh.parser;

import fr.gabray.portfoliosh.ast.CompleteCommandAst;
import fr.gabray.portfoliosh.ast.ListAst;
import fr.gabray.portfoliosh.ast.SimpleCommandAst;
import fr.gabray.portfoliosh.exception.ParsingException;
import fr.gabray.portfoliosh.lexer.Lexer;
import fr.gabray.portfoliosh.lexer.Token;
import fr.gabray.portfoliosh.lexer.TokenType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final Lexer lexer;

    public Parser(final Lexer lexer)
    {
        this.lexer = lexer;
    }

    public CompleteCommandAst parse() throws ParsingException
    {
        try
        {
            ListAst listAst = parseListAst();
            if (listAst == null)
                throw new ParsingException("Unexpected end of input");
            return new CompleteCommandAst(listAst);
        }
        catch (IOException e)
        {
            throw new ParsingException(e);
        }
    }

    @Nullable
    public ListAst parseListAst() throws IOException
    {
        SimpleCommandAst simpleCommandAst = parseSimpleCommand();
        if (simpleCommandAst == null)
            return null;
        List<SimpleCommandAst> commands = new ArrayList<>();
        while (simpleCommandAst != null)
        {
            commands.add(simpleCommandAst);
            simpleCommandAst = parseSimpleCommand();
        }
        return new ListAst(commands);
    }

    @Nullable
    public SimpleCommandAst parseSimpleCommand() throws IOException
    {
        Token token = lexer.pop();
        if (token.type() != TokenType.WORD)
            return null;

        List<String> words = new ArrayList<>();
        while (token.type() == TokenType.WORD)
        {
            words.add(token.value());
            token = lexer.pop();
        }
        return new SimpleCommandAst(words);
    }
}
