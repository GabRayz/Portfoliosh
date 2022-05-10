package fr.gabray.portfoliosh.parser;

import fr.gabray.portfoliosh.ast.*;
import fr.gabray.portfoliosh.exception.ParsingException;
import fr.gabray.portfoliosh.lexer.Lexer;
import fr.gabray.portfoliosh.lexer.Operator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void shouldThrowParsingExceptionWhenParseIsCalledWithEmptyLexer() throws ParsingException
    {
        Parser parser = new Parser(new Lexer(""));

        assertNull(parser.parse());
    }

    @Test
    void simpleCommandTest() throws ParsingException
    {
        Parser parser = new Parser(new Lexer("ls -a ./foo"));

        CompleteCommandAst ast = parser.parse();

        List<AndOrAst> commands = ast.ast().commands();
        assertEquals(1, commands.size());
        Ast left = commands.get(0).left();
        assertInstanceOf(PipelineAst.class, left);
        List<String> words = ((SimpleCommandAst) ((PipelineAst) left).commands().get(0).command()).words();
        assertEquals(3, words.size());
        assertEquals("ls", words.get(0));
        assertEquals("-a", words.get(1));
        assertEquals("./foo", words.get(2));
    }

    @Test
    void andOrTest() throws ParsingException, IOException
    {
        Parser parser = new Parser(new Lexer("command a || command b"));

        AndOrAst andOrAst = parser.parseAndOrAst();

        assertNotNull(andOrAst);
        assertNotNull(andOrAst.right());
        assertEquals(Operator.OR_IF, andOrAst.operator());
    }

    @Test
    void commandTest() throws IOException
    {
        Parser parser = new Parser(new Lexer("ls -la test"));

        CommandAst commandAst = parser.parseCommand();

        assertNotNull(commandAst);
        assertInstanceOf(SimpleCommandAst.class, commandAst.command());
        assertEquals(3, ((SimpleCommandAst) commandAst.command()).words().size());
    }

    @Test
    void simplePipelineTest() throws IOException
    {
        Parser parser = new Parser(new Lexer("ls -la test"));

        PipelineAst pipelineAst = parser.parsePipeline();
        assertNotNull(pipelineAst);

        CommandAst commandAst = pipelineAst.commands().get(0);
        assertInstanceOf(SimpleCommandAst.class, commandAst.command());
        assertEquals(3, ((SimpleCommandAst) commandAst.command()).words().size());
    }
}
