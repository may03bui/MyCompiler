package main;

public class NumberToken extends Token {

    private String type;
    private double value;

    public NumberToken(double value) {
        super("number");
        this.value = value;
    }

    public double getValue() { return this.value; }
}
