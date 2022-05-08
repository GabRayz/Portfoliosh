package fr.gabray.portfoliosh.env;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class FakeFile {

    @Getter
    private final FakeFile parent;
    @Getter
    private final Type type;
    @Getter
    private final String name;
    private final Map<String, FakeFile> children = new HashMap<>();

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

    public Map<String, FakeFile> getChildren()
    {
        return children;
    }
}
