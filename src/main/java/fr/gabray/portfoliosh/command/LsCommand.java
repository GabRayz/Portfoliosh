package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.env.FakeFile;
import fr.gabray.portfoliosh.exception.CommandRuntimeException;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class LsCommand implements Command {
    @Override
    public String getName()
    {
        return "ls";
    }

    @Override
    public int execute(final Environment env, final AutoWrapOutputStream outputStream, final String... args) throws IOException
    {
        try
        {
            FakeFile folder = env.getFile(args.length == 1 ? "" : args[1]);
            if (folder.getType() != FakeFile.Type.FOLDER)
                return error(outputStream, "Not a directory");
            Collection<FakeFile> files = folder.getFiles();
            print(outputStream, files);
        }
        catch (FileNotFoundException e)
        {
            return error(outputStream, "No such file or directory");
        }
        return 0;
    }

    private void print(AutoWrapOutputStream outputStream, Collection<FakeFile> files) throws IOException
    {
        int maxWidth = 10 + files.stream()
                                 .map(FakeFile::getName)
                                 .map(String::length)
                                 .max(Integer::compare)
                                 .orElse(0);
        int filesPerLine = outputStream.getMaxWidth() / maxWidth;
        int index = 0;
        for (FakeFile file : files)
        {
            String s = file.getName() + (file.getType() == FakeFile.Type.FOLDER ? "/" : "");
            try
            {
                if (index != 0)
                    outputStream.write(" ".repeat(maxWidth - s.length()).getBytes(StandardCharsets.UTF_8));
                outputStream.write(s.getBytes(StandardCharsets.UTF_8));
                if (++index == filesPerLine)
                {
                    index = 0;
                    outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
                }
            }
            catch (IOException e)
            {
                throw new CommandRuntimeException(e);
            }
        }
        outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
    }
}
