package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.env.FakeFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CdCommand implements Command {
    @Override
    public String getName()
    {
        return "cd";
    }

    @Override
    public int execute(final Environment env, final OutputStream outputStream, final String... args) throws IOException
    {
        if (args.length == 1)
            env.setWorkingDirectory(env.getRootFolder());
        else
        {
            try
            {
                FakeFile file = env.getFile(args[1]);
                if (file.getType() != FakeFile.Type.FOLDER)
                    outputStream.write(("portfoliosh: cd: " + args[1] + ": Not a directory").getBytes(StandardCharsets.UTF_8));
                else
                    env.setWorkingDirectory(file);
            }
            catch (FileNotFoundException e)
            {
                String s = "portfoliosh: cd: " + args[1] + ": No such file or directory";
                outputStream.write(s.getBytes(StandardCharsets.UTF_8));
            }
        }
        return 0;
    }
}
