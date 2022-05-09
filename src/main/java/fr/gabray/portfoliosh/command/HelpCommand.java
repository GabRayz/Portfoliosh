package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HelpCommand implements Command {
    @Override
    public String getName()
    {
        return "help";
    }

    @Override
    public int execute(final Environment env, final OutputStream outputStream, final String... args) throws IOException
    {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("help-command.txt");
        if (stream == null)
            throw new IllegalStateException("Missing help-command.txt file");
        stream.transferTo(outputStream);
        return 0;
    }
}
