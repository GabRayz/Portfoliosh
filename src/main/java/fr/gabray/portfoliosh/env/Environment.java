package fr.gabray.portfoliosh.env;

import fr.gabray.portfoliosh.exception.EnvironmentInitException;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

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
        URL fsUrl = environment.getClass().getClassLoader().getResource("fs");
        if (fsUrl == null)
            throw new IllegalStateException("Missing fs resource folder");
        File file;
        try
        {
            file = new File(fsUrl.toURI());
        }
        catch (URISyntaxException e)
        {
            throw new EnvironmentInitException(e);
        }

        addFilesToEnv(environment, file, environment.getRootFolder());

        return environment;
    }

    private static void addFilesToEnv(Environment environment, File file, FakeFile parent)
    {
        for (final File listFile : Objects.requireNonNull(file.listFiles()))
        {
            FakeFile.Type type = listFile.isDirectory()
                                 ? FakeFile.Type.FOLDER
                                 : FakeFile.Type.FILE;
            FakeFile created = environment.createFile(type, parent, listFile.getName());
            if (type == FakeFile.Type.FOLDER)
                addFilesToEnv(environment, listFile, created);
        }
    }
}
