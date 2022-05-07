package fr.gabray.portfoliosh.lexer;

public record Token(TokenType type, String value) {
    public static Token EOI = new Token(TokenType.EOI, "\0");
}
