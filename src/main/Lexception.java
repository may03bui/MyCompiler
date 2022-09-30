package main;

public class Lexception extends Exception {
    public Lexception() {
        super("Lexing failed: no pattern match found.");
    }
}
