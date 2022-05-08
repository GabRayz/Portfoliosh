package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PwdCommand implements Command {
    @Override
    public String getName()
    {
        return "pwd";
    }

    @Override
    public int execute(final Environment env, final OutputStream outputStream, final String... args) throws IOException
    {
        outputStream.write(env.getWorkingDirectory().computePath().getBytes(StandardCharsets.UTF_8));
        outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        return 0;
    }
}
