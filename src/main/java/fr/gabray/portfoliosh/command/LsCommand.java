package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.env.FakeFile;
import fr.gabray.portfoliosh.exception.CommandRuntimeException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class LsCommand implements Command {
    @Override
    public String getName()
    {
        return "ls";
    }

    @Override
    public int execute(final Environment env, final OutputStream outputStream, final String... args)
    {
        Collection<FakeFile> files = env.getWorkingDirectory().getFiles();
        files.forEach(file -> {
            String s = file.getName() + (file.getType() == FakeFile.Type.FOLDER ? "/" : "") + "\t";
            try
            {
                outputStream.write(s.getBytes(StandardCharsets.UTF_8));
            }
            catch (IOException e)
            {
                throw new CommandRuntimeException(e);
            }
        });
        return 0;
    }
}
