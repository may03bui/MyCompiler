package main;

public class Token {
    // Tokens can represent the following:
    // cos, !, ^, +, -

    private String type;

    public Token(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
