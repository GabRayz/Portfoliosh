package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;

import java.io.IOException;
import java.io.OutputStream;

public interface Ast {

    int execute(Environment env, OutputStream outputStream) throws IOException;
}
