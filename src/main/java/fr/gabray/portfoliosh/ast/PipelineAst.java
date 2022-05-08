package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public record PipelineAst(List<CommandAst> commands) implements Ast {
    @Override
    public int execute(final Environment env, final OutputStream outputStream) throws IOException
    {
        for (final CommandAst command : commands)
            command.execute(env, outputStream);
        return 0;
    }
}
