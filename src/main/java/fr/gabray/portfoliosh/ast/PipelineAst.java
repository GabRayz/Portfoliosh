package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.IOException;
import java.util.List;

public record PipelineAst(List<CommandAst> commands) implements Ast {
    @Override
    public int execute(final Environment env, final AutoWrapOutputStream outputStream) throws IOException
    {
        for (final CommandAst command : commands)
            command.execute(env, outputStream);
        return 0;
    }
}
