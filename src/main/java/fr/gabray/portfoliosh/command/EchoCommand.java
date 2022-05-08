package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EchoCommand implements Command {
    @Override
    public String getName()
    {
        return "echo";
    }

    @Override
    public int execute(final Environment env, final OutputStream outputStream, final String... args) throws IOException
    {
        for (int i = 1; i < args.length; i++)
        {
            if (i > 1)
                outputStream.write(" ".getBytes(StandardCharsets.UTF_8));
            outputStream.write(args[i].getBytes(StandardCharsets.UTF_8));
        }
        return 0;
    }
}
