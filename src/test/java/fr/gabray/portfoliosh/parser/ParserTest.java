package fr.gabray.portfoliosh.parser;

import fr.gabray.portfoliosh.ast.CompleteCommandAst;
import fr.gabray.portfoliosh.ast.SimpleCommandAst;
import fr.gabray.portfoliosh.exception.ParsingException;
import fr.gabray.portfoliosh.lexer.Lexer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParserTest {

    @Test
    void shouldThrowParsingExceptionWhenParseIsCalledWithEmptyLexer()
    {
        Parser parser = new Parser(new Lexer(""));

        assertThrows(ParsingException.class, parser::parse);
    }

    @Test
    void simpleCommandTest() throws ParsingException
    {
        Parser parser = new Parser(new Lexer("ls -a ./foo"));

        CompleteCommandAst ast = parser.parse();

        List<SimpleCommandAst> commands = ast.ast().commands();
        assertEquals(1, commands.size());
        List<String> words = commands.get(0).words();
        assertEquals(3, words.size());
        assertEquals("ls", words.get(0));
        assertEquals("-a", words.get(1));
        assertEquals("./foo", words.get(2));
    }

    @Test
    void skipLeadingLineFeedsTest() throws ParsingException
    {
        Parser parser = new Parser(new Lexer("\n\n\necho"));

        CompleteCommandAst ast = parser.parse();

        assertEquals(1, ast.ast().commands().size());
    }
}
