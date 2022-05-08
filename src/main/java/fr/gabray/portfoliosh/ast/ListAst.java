package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;

import java.io.OutputStream;
import java.util.List;

public record ListAst(List<SimpleCommandAst> commands) implements Ast {
    @Override
    public int execute(final Environment env, final OutputStream outputStream)
    {
        int res = 0;
        for (final SimpleCommandAst command : commands)
            res = command.execute(env, outputStream);
        return res;
    }
}
