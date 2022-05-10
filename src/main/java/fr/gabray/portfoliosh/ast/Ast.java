package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.util.AutoWrapOutputStream;

import java.io.IOException;

public interface Ast {

    int execute(Environment env, AutoWrapOutputStream outputStream) throws IOException;
}
