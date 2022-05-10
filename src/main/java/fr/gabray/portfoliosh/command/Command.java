package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.IOException;

public interface Command {

    String getName();

    int execute(Environment env, AutoWrapOutputStream outputStream, String... args) throws IOException;

    default int error(AutoWrapOutputStream outputStream, String message) throws IOException
    {
        String errorMessage = String.format("portfoliosh: %s: %s", getName(), message);
        outputStream.write(errorMessage);
        return 1;
    }
}
