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
import java.util.LinkedHashMap;
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
        Map<String, Integer> widths = new LinkedHashMap<>();
        for (int i = 0; i < resultSet.columns().size(); i++)
        {
            String column = resultSet.columns().get(i);
            widths.put(column, computeColumnWidth(resultSet, column));
            outputStream.write(column.getBytes(StandardCharsets.UTF_8));
            if (i < resultSet.columns().size() - 1)
                printSpaces(outputStream, widths.get(column), column.length());
        }
        printHorizontalSeparator(outputStream, widths);
        for (final Map<String, DbData> row : resultSet.data())
        {
            for (int i = 0; i < resultSet.columns().size(); i++)
            {
                String column = resultSet.columns().get(i);
                String data = row.get(column).toString();
                outputStream.write(data.getBytes(StandardCharsets.UTF_8));
                if (i < resultSet.columns().size() - 1)
                    printSpaces(outputStream, widths.get(column), data.length());
            }
            outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        }
    }

    private void printHorizontalSeparator(OutputStream outputStream, Map<String, Integer> widths) throws IOException
    {
        int i = 0;
        outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
        for (final Integer width : widths.values())
        {
            outputStream.write("-".repeat(width).getBytes(StandardCharsets.UTF_8));
            if (i++ < widths.size() - 1)
                outputStream.write("-+-".getBytes(StandardCharsets.UTF_8));
        }
        outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
    }

    private void printSpaces(OutputStream outputStream, int totalWidth, int dataWidth) throws IOException
    {
        int missing = totalWidth + 1 - dataWidth;
        outputStream.write(" ".repeat(missing).getBytes(StandardCharsets.UTF_8));
        outputStream.write("| ".getBytes(StandardCharsets.UTF_8));
    }

    private int computeColumnWidth(ResultSet resultSet, String column)
    {
        int max = resultSet.data().stream()
                           .map(map -> map.get(column).toString())
                           .map(String::length)
                           .max(Integer::compareTo)
                           .orElse(3);
        return Math.max(max, column.length());
    }
}
