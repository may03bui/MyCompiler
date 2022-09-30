package test;

import main.Lexception;
import main.Lexer;
import main.NumberToken;
import main.Token;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.Queue;

import static main.Lexer.lex;

public class LexerTest {

    @Test
    public void testEmptyInputReturnsEmptyQueue() throws Lexception {
        Queue<Token> q = Lexer.lex("");
        assertEquals(q.size(), 0);
    }

    @Test
    public void testIntegerReturnsNumberToken() throws Lexception {
        Queue<Token> q = Lexer.lex("3487");
        NumberToken t = (NumberToken) q.poll();
        assertEquals(t.getValue(), 3487d, 0.001);
    }

    @Test
    public void testNegativeReturnsNumberToken() throws Lexception {
        Queue<Token> q = Lexer.lex("-3487");
        NumberToken t = (NumberToken) q.poll();
        assertEquals(t.getValue(), -3487d, 0.00001);
    }

    @Test
    public void testAdditionReturnsNumberOpNumberQueue() throws Lexception {
        Queue<Token> q = Lexer.lex("3487+23948");
        NumberToken t1 = (NumberToken) q.poll();
        Token t2 = q.poll();
        NumberToken t3 = (NumberToken) q.poll();

        assertEquals(t1.getValue(), 3487d, 0.00001);
        assertEquals(t2.getType(), "+");
        assertEquals(t3.getValue(), 23948, 0.00001);
    }
}
