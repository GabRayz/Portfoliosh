package fr.gabray.portfoliosh.parser;

import fr.gabray.portfoliosh.ast.*;
import fr.gabray.portfoliosh.exception.ParsingException;
import fr.gabray.portfoliosh.lexer.*;
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
    ListAst parseListAst() throws IOException, ParsingException
    {
        AndOrAst andOrAst = parseAndOrAst();
        if (andOrAst == null)
            return null;
        List<AndOrAst> commands = new ArrayList<>();
        while (andOrAst != null)
        {
            commands.add(andOrAst);
            andOrAst = parseAndOrAst();
        }
        return new ListAst(commands);
    }

    @Nullable
    AndOrAst parseAndOrAst() throws IOException, ParsingException
    {
        PipelineAst pipelineAst = parsePipeline();
        if (pipelineAst == null)
            return null;
        Token token = lexer.pop();
        if (token instanceof OperatorToken operatorToken && (operatorToken.getOperator() == Operator.AND_IF || operatorToken.getOperator() == Operator.OR_IF))
        {
            AndOrAst right = parseAndOrAst();
            if (right == null)
                throw new ParsingException();
            return new AndOrAst(pipelineAst, right, operatorToken.getOperator());
        }
        else
            return new AndOrAst(pipelineAst, null, null);
    }

    @Nullable
    PipelineAst parsePipeline() throws IOException
    {
        CommandAst commandAst = parseCommand();
        if (commandAst == null)
            return null;
        List<CommandAst> commands = new ArrayList<>();
        while (commandAst != null)
        {
            commands.add(commandAst);
            commandAst = parseCommand();
        }
        return new PipelineAst(commands);
    }

    @Nullable
    CommandAst parseCommand() throws IOException
    {
        SimpleCommandAst simpleCommandAst = parseSimpleCommand();
        if (simpleCommandAst == null)
            return null;
        return new CommandAst(simpleCommandAst);
    }

    @Nullable
    SimpleCommandAst parseSimpleCommand() throws IOException
    {
        Token token = lexer.peek();
        if (token.getType() != TokenType.WORD)
            return null;

        List<String> words = new ArrayList<>();
        while (token.getType() == TokenType.WORD)
        {
            lexer.pop();
            words.add(token.getValue());
            token = lexer.peek();
        }
        return new SimpleCommandAst(words);
    }
}
