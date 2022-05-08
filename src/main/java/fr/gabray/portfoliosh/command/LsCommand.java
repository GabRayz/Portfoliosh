package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.env.FakeFile;
import fr.gabray.portfoliosh.exception.CommandRuntimeException;

import java.io.FileNotFoundException;
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
    public int execute(final Environment env, final OutputStream outputStream, final String... args) throws IOException
    {
        try
        {
            FakeFile folder = env.getFile(args.length == 1 ? "" : args[1]);
            if (folder.getType() != FakeFile.Type.FOLDER)
                return error(outputStream, "Not a directory");
            Collection<FakeFile> files = folder.getFiles();
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
            outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        catch (FileNotFoundException e)
        {
            return error(outputStream, "No such file or directory");
        }
        return 0;
    }
}
