package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;

import java.io.OutputStream;

public record CompleteCommandAst(ListAst ast) implements Ast {
    @Override
    public int execute(final Environment env, final OutputStream outputStream)
    {
        return ast.execute(env, outputStream);
    }
}
