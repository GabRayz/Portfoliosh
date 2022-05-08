package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.command.Command;
import fr.gabray.portfoliosh.command.CommandManager;
import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.exception.CommandRuntimeException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public record SimpleCommandAst(List<String> words) implements Ast {

    @Override
    public int execute(final Environment env, final OutputStream outputStream)
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
            try
            {
                outputStream.write(("portfoliosh: " + commandName + ": command not found").getBytes(StandardCharsets.UTF_8));
                return 1;
            }
            catch (IOException e)
            {
                throw new CommandRuntimeException(e);
            }
        }
    }
}
