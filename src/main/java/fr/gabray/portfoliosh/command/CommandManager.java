package fr.gabray.portfoliosh.command;

import fr.gabray.portfoliosh.command.sql.SqlCommand;

import java.util.Map;
import java.util.Optional;

public final class CommandManager {

    private CommandManager()
    {
    }

    private static final Map<String, Command> commands = Map.of(
            "ls", new LsCommand(),
            "cd", new CdCommand(),
            "pwd", new PwdCommand(),
            "cat", new CatCommand(),
            "echo", new EchoCommand(),
            "sql", new SqlCommand(),
            "help", new HelpCommand(),
            "sh", new ShCommand()
    );

    public static Optional<Command> get(String name)
    {
        return Optional.ofNullable(commands.get(name));
    }
}
