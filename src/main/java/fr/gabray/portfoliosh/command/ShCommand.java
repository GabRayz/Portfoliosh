package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.ast.CompleteCommandAst;
import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.env.FakeFile;
import fr.gabray.portfoliosh.exception.ParsingException;
import fr.gabray.portfoliosh.lexer.Lexer;
import fr.gabray.portfoliosh.parser.Parser;

import java.io.FileNotFoundException;
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
        FakeFile file;
        try
        {
            file = env.getFile(args[1]);
        }
        catch (FileNotFoundException e)
        {
            return error(outputStream, "No such file or directory");
        }
        if (file.getType() != FakeFile.Type.FILE)
            return error(outputStream, "Not a file");

        String content = file.read();
        Parser parser = new Parser(new Lexer(content));
        try
        {
            int result = 0;
            CompleteCommandAst ast = parser.parse();
            while (ast != null)
            {
                result = ast.execute(env, outputStream);
                ast = parser.parse();
            }
            return result;
        }
        catch (ParsingException e)
        {
            return error(outputStream, e.getMessage());
        }
    }
}
