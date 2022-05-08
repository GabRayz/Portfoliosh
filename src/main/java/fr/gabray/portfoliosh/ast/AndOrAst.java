package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.env.Environment;
import fr.gabray.portfoliosh.lexer.Operator;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;

public record AndOrAst(Ast left, @Nullable Ast right, @Nullable Operator operator) implements Ast {

    @Override
    public int execute(final Environment env, final OutputStream outputStream) throws IOException
    {
        if ((operator == null && right != null) || (operator != null && right == null))
            throw new IllegalStateException();
        if (right != null && operator == Operator.AND_IF)
            return (left.execute(env, outputStream) == 0 && right.execute(env, outputStream) == 0) ? 0 : 1;
        else if (right != null && operator == Operator.OR_IF)
            return (left.execute(env, outputStream) == 0 || right.execute(env, outputStream) == 0) ? 0 : 1;
        return left.execute(env, outputStream);
    }
}
