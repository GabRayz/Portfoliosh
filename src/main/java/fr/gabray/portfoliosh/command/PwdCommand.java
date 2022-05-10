package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.IOException;

public class PwdCommand implements Command {
    @Override
    public String getName()
    {
        return "pwd";
    }

    @Override
    public int execute(final Environment env, final AutoWrapOutputStream outputStream, final String... args) throws IOException
    {
        outputStream.write(env.getWorkingDirectory().computePath());
        outputStream.write("\n");
        return 0;
    }
}
