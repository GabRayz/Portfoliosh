package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.IOException;

public record CommandAst(Ast command) implements Ast {

    @Override
    public int execute(final Environment env, final AutoWrapOutputStream outputStream) throws IOException
    {
        return command.execute(env, outputStream);
    }
}
