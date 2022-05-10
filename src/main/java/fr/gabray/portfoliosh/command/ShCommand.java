package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.ast.CompleteCommandAst;
import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.env.FakeFile;
import fr.gabray.portfoliosh.exception.ParsingException;
import fr.gabray.portfoliosh.lexer.Lexer;
import fr.gabray.portfoliosh.parser.Parser;

import java.io.IOException;
import java.io.OutputStream;

public class ShCommand implements Command {
    @Override
    public String getName()
    {
        return "sh";
    }

    @Override
    public int execute(final Environment env, final OutputStream outputStream, final String... args) throws IOException
    {
        if (args.length == 1)
            return error(outputStream, "Expected one argument");
        FakeFile file = env.getFile(args[1]);
        if (file.getType() != FakeFile.Type.FILE)
            return error(outputStream, "Not a file");

        String content = file.read();
        Parser parser = new Parser(new Lexer(content));
        try
        {
            CompleteCommandAst ast = parser.parse();
            return ast.execute(env, outputStream);
        }
        catch (ParsingException e)
        {
            return error(outputStream, e.getMessage());
        }
    }
}
