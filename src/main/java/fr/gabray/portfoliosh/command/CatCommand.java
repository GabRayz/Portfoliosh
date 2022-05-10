package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.env.FakeFile;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CatCommand implements Command {
    @Override
    public String getName()
    {
        return "command";
    }

    @Override
    public int execute(final Environment env, final AutoWrapOutputStream outputStream, final String... args) throws IOException
    {
        if (args.length == 1)
            return error(outputStream, "Expected one argument");
        try
        {
            FakeFile file = env.getFile(args[1]);
            if (file.getType() == FakeFile.Type.FOLDER)
                return error(outputStream, args[1] + " is a directory");
            outputStream.write(file.read());
            return 0;
        }
        catch (FileNotFoundException e)
        {
            return error(outputStream, "No such file or directory");
        }
    }
}
