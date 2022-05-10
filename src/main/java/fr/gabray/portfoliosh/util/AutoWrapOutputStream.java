package fr.gabray.portfoliosh.util;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class AutoWrapOutputStream extends ByteArrayOutputStream {

    private final int maxWidth;

    public AutoWrapOutputStream(int maxWidth)
    {
        this.maxWidth = maxWidth;
    }

    public void write(@NotNull final String input) throws IOException
    {
        String[] lines = input.split("\n");
        for (String line : lines)
        {
            while (line.length() > maxWidth)
            {
                String leading = line.substring(0, maxWidth);
                write((leading + '\n').getBytes(StandardCharsets.UTF_8));
                line = line.substring(maxWidth);
            }
            write((line + '\n').getBytes(StandardCharsets.UTF_8));
        }
    }
}
