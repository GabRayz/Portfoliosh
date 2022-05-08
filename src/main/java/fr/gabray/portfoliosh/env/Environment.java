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

        FakeFile file = rootFolder;
        String[] paths = path.split("/");
        for (final String subpath : paths)
        {
            if (subpath.isBlank())
                continue;
            file = file.getFile(subpath);
            if (file == null)
                throw new FileNotFoundException();
        }
        return file;
    }
}
