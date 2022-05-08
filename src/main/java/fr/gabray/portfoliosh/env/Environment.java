package fr.gabray.portfoliosh.env;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;

public class Environment {

    @Getter
    private final FakeFile rootFolder;
    @Getter
    @Setter
    private FakeFile workingDirectory;

    public Environment()
    {
        rootFolder = new FakeFile(null, FakeFile.Type.FOLDER, "");
        workingDirectory = rootFolder;
    }

    public FakeFile createFile(@NotNull FakeFile.Type type, @NotNull FakeFile parent, @NotNull String name)
    {
        if (name.isBlank())
            throw new IllegalArgumentException("File name cannot be blank");

        return parent.addFile(new FakeFile(parent, type, name));
    }

    public FakeFile getFile(String path) throws FileNotFoundException
    {
        return path.startsWith("/") ? getFile(rootFolder, path) : getFile(workingDirectory, path);
    }

    public FakeFile getFile(FakeFile base, String path) throws FileNotFoundException
    {
        FakeFile file = base;
        String[] paths = path.split("/");
        for (final String subpath : paths)
        {
            if (subpath.isBlank() || subpath.equals("."))
                continue;
            if (subpath.equals(".."))
                file = file.getParent();
            else
                file = file.getFile(subpath);
            if (file == null)
                throw new FileNotFoundException();
        }
        return file;
    }

    public static Environment defaultEnv()
    {
        Environment environment = new Environment();
        environment.createFile(FakeFile.Type.FILE, environment.getRootFolder(), "foo");
        environment.createFile(FakeFile.Type.FILE, environment.getRootFolder(), "bar");
        environment.createFile(FakeFile.Type.FOLDER, environment.getRootFolder(), "dir");
        return environment;
    }
}
