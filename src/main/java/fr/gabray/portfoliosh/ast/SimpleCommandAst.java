package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.lexer.Token;

import java.io.OutputStream;
import java.util.List;

public record SimpleCommandAst(List<Token> words) implements Ast {

    @Override
    public int execute(final Environment env, final OutputStream outputStream)
    {
        return 0;
    }
}
