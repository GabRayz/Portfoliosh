package fr.gabray.portfoliosh.ast;

import java.util.List;

public record ListAst(List<SimpleCommandAst> commands) implements Ast {
}
