package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public interface Command {

    String getName();

    int execute(Environment env, OutputStream outputStream, String... args) throws IOException;

    default int error(OutputStream outputStream, String message) throws IOException
    {
        String errorMessage = String.format("portfoliosh: %s: %s", getName(), message);
        outputStream.write(errorMessage.getBytes(StandardCharsets.UTF_8));
        return 1;
    }
}
