package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.command.Command;
import fr.gabray.portfoliosh.command.CommandManager;
import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public record SimpleCommandAst(List<String> words) implements Ast {

    @Override
    public int execute(final Environment env, final AutoWrapOutputStream outputStream) throws IOException
    {
        if (words.isEmpty())
            throw new IllegalStateException("Should have at least one word");
        String commandName = words.get(0);
        Optional<Command> command = CommandManager.get(commandName);
        if (command.isPresent())
        {
            return command.get().execute(env, outputStream, words.toArray(new String[0]));
        }
        else
        {
            outputStream.write(("portfoliosh: " + commandName + ": command not found").getBytes(StandardCharsets.UTF_8));
            return 1;
        }
    }
}
