package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;

import java.io.IOException;
import java.io.OutputStream;

public record CommandAst(Ast command) implements Ast {

    @Override
    public int execute(final Environment env, final OutputStream outputStream) throws IOException
    {
        return command.execute(env, outputStream);
    }
}
