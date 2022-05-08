package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.env.Environment;

import java.io.IOException;
import java.io.OutputStream;

public interface Command {

    String getName();

    int execute(Environment env, OutputStream outputStream, String... args) throws IOException;
}
