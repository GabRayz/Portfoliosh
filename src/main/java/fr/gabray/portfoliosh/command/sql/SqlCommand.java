package fr.gabray.portfoliosh.command.sql;

import fr.gabray.portfoliosh.command.Command;
import fr.gabray.portfoliosh.command.sql.parser.SqlParser;
import fr.gabray.portfoliosh.command.sql.statement.StatementBuilder;
import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.exception.ParsingException;
import fr.gabray.portfoliosh.exception.SqlException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public class SqlCommand implements Command {
    @Override
    public String getName()
    {
        return "sql";
    }

    @Override
    public int execute(final Environment env, final OutputStream outputStream, final String... args) throws IOException
    {
        StringJoiner joiner = new StringJoiner(" ");
        for (int i = 1; i < args.length; i++)
            joiner.add(args[i]);

        SqlParser sqlParser = new SqlParser(joiner.toString());
        try
        {
            StatementBuilder statement = sqlParser.parse(env.getDatabase());
            ResultSet resultSet = statement.execute(env.getDatabase());
            printResult(outputStream, resultSet);
        }
        catch (ParsingException e)
        {
            return error(outputStream, "Syntax error: " + e.getMessage());
        }
        catch (SqlException e)
        {
            return error(outputStream, e.getMessage());
        }

        return 0;
    }

    private void printResult(OutputStream outputStream, ResultSet resultSet) throws IOException
    {
        for (int i = 0; i < resultSet.columns().size(); i++)
        {
            outputStream.write(resultSet.columns().get(i).getBytes(StandardCharsets.UTF_8));
            if (i < resultSet.columns().size() - 1)
                outputStream.write("\t\t| ".getBytes(StandardCharsets.UTF_8));
        }
        outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        for (final Map<String, DbData> row : resultSet.data())
        {
            for (int i = 0; i < resultSet.columns().size(); i++)
            {
                String column = resultSet.columns().get(i);
                outputStream.write(row.get(column).toString().getBytes(StandardCharsets.UTF_8));
                if (i < resultSet.columns().size() - 1)
                    outputStream.write("\t\t| ".getBytes(StandardCharsets.UTF_8));
            }
            outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        }
    }
}
