package fr.gabray.portfoliosh.env;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FakeFile {

    @Getter
    private final FakeFile parent;
    @Getter
    private final Type type;
    @Getter
    private final String name;
    private final Map<String, FakeFile> files = new HashMap<>();

    public FakeFile(final FakeFile parent, final Type type, final String name)
    {
        this.parent = parent;
        this.type = type;
        this.name = name;
    }

    public enum Type {
        FOLDER,
        FILE,
    }

    public Collection<FakeFile> getFiles()
    {
        if (type != Type.FOLDER)
            throw new UnsupportedOperationException();
        return files.values();
    }

    @Nullable
    public FakeFile getFile(String name)
    {
        if (type != Type.FOLDER)
            throw new UnsupportedOperationException();
        return files.get(name);
    }

    public FakeFile addFile(FakeFile file)
    {
        if (type != Type.FOLDER)
            throw new UnsupportedOperationException();
        files.put(file.name, file);
        return file;
    }

    public String computePath()
    {
        if (parent == null)
            return "/";
        String path = parent.computePath();
        return path + (path.endsWith("/") ? "" : "/") + name;
    }

    public String read() throws IOException
    {
        if (type != Type.FILE)
            throw new UnsupportedOperationException();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("fs" + computePath());
        if (stream == null)
            throw new IOException("File not found");
        return new String(stream.readAllBytes());
    }
}
