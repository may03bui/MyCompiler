package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

public class Lexer {

    private static final ArrayList<Character> digits =   // Definitely a better way to do this
            new ArrayList<>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));
    private static final ArrayList<Character> operations =  // One-char operations; all but 'cos'
            new ArrayList<>(Arrays.asList('+', '-', '^', '!'));

    private static class NumberMatch {
        int nextLexemeStart;
        Token token;

        private NumberMatch(int nextLexemeStart, Token token) {
            this.nextLexemeStart = nextLexemeStart;
            this.token = token;
        }
    }

    private static NumberMatch matchNumber(String input, int start) throws Lexception {
        String valueString = runNumberDFA(input, start);

        System.out.println(valueString); // TODO: remove

        NumberToken token = new NumberToken(Double.parseDouble(valueString));
        return new NumberMatch(start + valueString.length(), token);
    }

    private static String runNumberDFA(String input, int start) throws Lexception {
        int pointer = start;
        StringBuilder valString = new StringBuilder();
        int state = 0;

        while (pointer < input.length()) {
            char c = input.charAt(pointer);

            switch(state) {
                case 0:
                    if (c == '0') {
                        state = 1;
                    } else if (c == '+' || c == '-') {
                        state = 2;
                    } else if (digits.contains(c)) {
                        state = 4;
                    } else {
                        throw new Lexception();
                    }
                    break;

                case 1:
                    if (c == '.') {
                        state = 3;
                    } else if (c == 'e') {
                        state = 6;
                    } else {
                        throw new Lexception();
                    }
                    break;

                case 2:
                    if (c == '0') {
                        state = 1;
                    } else if (digits.contains(c)) {
                        state = 4;
                    } else {
                        throw new Lexception();
                    }
                    break;

                case 3:
                    if (digits.contains(c)) {
                        state = 5;
                    } else {
                        throw new Lexception();
                    }
                    break;

                case 4:
                    if (c == '.') {
                        state = 3;
                    } else if (c == 'e') {
                        state = 6;
                    } else if (!digits.contains(c)) {
                        return valString.toString();
                    }
                    break;

                case 5:
                    if (c == 'e') {
                        state = 6;
                    } else if (!digits.contains(c)) {
                        return valString.toString();
                    }
                    break;

                case 6:
                    if (c == '+' || c == '-') {
                        state = 7;
                    } else if (c != '0' && digits.contains(c)) {
                        state = 8;
                    } else {
                        throw new Lexception();
                    }
                    break;

                case 7:
                    if (c != '0' && digits.contains(c)) {
                        state = 8;
                    } else {
                        throw new Lexception();
                    }
                    break;

                case 8:
                    if (!digits.contains(c)) {
                        return valString.toString();
                    }
            }

            valString.append(input.charAt(pointer++));
        }

        throw new Lexception();
    }

    public static Queue<Token> lex(String input) throws Lexception {
        ArrayDeque<Token> tokens = new ArrayDeque<>();
        int pointer = 0;

        /* So that we can handle '+' and '-' symbols, we have a variable that we toggle each time we
           encounter those symbols. We begin by expecting a number when we encounter a '+' or '-';
           next time, we expect an op, etc.
           (This does, however, mean we are technically doing some parsing in the lexer...)       */
        boolean expectOp  = false;

        while (pointer < input.length()) {
            char c = input.charAt(pointer);

            if (c == '+' || c == '-') {
                if (expectOp) {
                    tokens.add(new Token(Character.toString(c)));
                    pointer++;
                } else {
                    NumberMatch match = matchNumber(input, pointer);
                    tokens.add(match.token);
                    pointer = match.nextLexemeStart;
                }
                expectOp = !expectOp;  // Can reduce keystrokes by using ^= ...

            } else if (digits.contains(c)) {
                System.out.println("D"); // TODO: remove
                NumberMatch match = matchNumber(input, pointer);
                tokens.add(match.token);
                pointer = match.nextLexemeStart;

            } else if (c == '^' || c == '!') {
                tokens.add(new Token(Character.toString(c)));
                pointer++;

            } else if (c == 'c') {
                // Must be a better conditional check than this...
                if (input.charAt(pointer + 1) == 'o' && input.charAt(pointer + 2) == 's') {
                    tokens.add(new Token("cos"));
                    pointer += 3;
                } else {
                    throw new Lexception();
                }
            }
        }

        return tokens;
    }
}
