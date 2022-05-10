package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.env.FakeFile;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CdCommand implements Command {
    @Override
    public String getName()
    {
        return "cd";
    }

    @Override
    public int execute(final Environment env, final AutoWrapOutputStream outputStream, final String... args) throws IOException
    {
        if (args.length == 1)
            env.setWorkingDirectory(env.getRootFolder());
        else
        {
            try
            {
                FakeFile file = env.getFile(args[1]);
                if (file.getType() != FakeFile.Type.FOLDER)
                    outputStream.write("portfoliosh: cd: " + args[1] + ": Not a directory");
                else
                    env.setWorkingDirectory(file);
            }
            catch (FileNotFoundException e)
            {
                outputStream.write("portfoliosh: cd: " + args[1] + ": No such file or directory");
            }
        }
        return 0;
    }
}
