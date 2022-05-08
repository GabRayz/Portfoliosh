package fr.gabray.portfoliosh.ast;

import fr.gabray.portfoliosh.lexer.Token;

import java.util.List;

public record SimpleCommandAst(List<Token> words) implements Ast {

}
