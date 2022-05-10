package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.IOException;

public record CompleteCommandAst(ListAst ast) implements Ast {
    @Override
    public int execute(final Environment env, final AutoWrapOutputStream outputStream) throws IOException
    {
        return ast.execute(env, outputStream);
    }
}
