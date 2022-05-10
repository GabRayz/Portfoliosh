package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.IOException;

public class EchoCommand implements Command {
    @Override
    public String getName()
    {
        return "echo";
    }

    @Override
    public int execute(final Environment env, final AutoWrapOutputStream outputStream, final String... args) throws IOException
    {
        for (int i = 1; i < args.length; i++)
        {
            if (i > 1)
                outputStream.write(" ");
            outputStream.write(args[i]);
        }
        outputStream.write("\n");
        return 0;
    }
}
