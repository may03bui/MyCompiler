package test;

import main.Lexception;
import main.Lexer;
import main.NumberToken;
import main.Token;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Queue;

public class LexerTest {

    @Test
    public void testEmptyInputReturnsEmptyQueue() throws Lexception {
        Queue<Token> q = Lexer.lex("");
        assertEquals(q.size(), 0);
    }

    @Test
    public void testNegativeReturnsNumberToken() throws Lexception {
        Queue<Token> q = Lexer.lex("-3487");
        NumberToken t = (NumberToken) q.poll();
        assertEquals(t.getValue(), -3487, 0.00001);
    }

    @Test
    public void testWhitespaceIsIgnored() throws Lexception {
        Queue<Token> q = Lexer.lex("-3487 +    4    - 3");

        NumberToken t1 = (NumberToken) q.poll();
        Token t2 = q.poll();
        NumberToken t3 = (NumberToken) q.poll();
        Token t4 = q.poll();
        NumberToken t5 = (NumberToken) q.poll();

        assertEquals(t1.getValue(), -3487, 0.00001);
        assertEquals(t2.getType(), "+");
        assertEquals(t3.getValue(), 4, 0.00001);
        assertEquals(t4.getType(), "-");
        assertEquals(t5.getValue(), 3, 0.00001);
    }

    @Test
    public void testIntegerReturnsNumberToken() throws Lexception {
        Queue<Token> q = Lexer.lex("3487");
        NumberToken t = (NumberToken) q.poll();
        assertEquals(t.getValue(), 3487, 0.00001);
    }

    @Test
    public void testFractionReturnsNumberToken() throws Lexception {
        Queue<Token> q = Lexer.lex("3.1");
        NumberToken t = (NumberToken) q.poll();
        assertEquals(t.getValue(), 3.1, 0.00001);
    }

    @Test
    public void testIntegerWithExpReturnsNumberToken() throws Lexception {
        Queue<Token> q = Lexer.lex("123e4");
        NumberToken t = (NumberToken) q.poll();
        assertEquals(t.getValue(), 123e4, 0.00001);
    }

    @Test
    public void testFractionWithExpReturnsNumberToken() throws Lexception {
        Queue<Token> q = Lexer.lex("123.4e5");
        NumberToken t = (NumberToken) q.poll();
        assertEquals(t.getValue(), 123.4e5, 0.00001);
    }

    @Test
    public void test0ReturnsNumberToken() throws Lexception {
        Queue<Token> q = Lexer.lex("0");
        NumberToken t = (NumberToken) q.poll();
        assertEquals(t.getValue(), 0, 0.00001);
    }

    @Test
    public void testCosReturnsToken() throws Lexception {
        Queue<Token> q = Lexer.lex("cos1");
        Token t1 = q.poll();
        NumberToken t2 = (NumberToken) q.poll();
        assertEquals(t1.getType(), "cos");
        assertEquals(t2.getValue(), 1, 0.00001);
    }

    @Test(expected = Lexception.class)
    public void testNumberEndingWithEThrowsException() throws Lexception {
        Queue<Token> q = Lexer.lex("123.4e");
    }

    @Test(expected = Lexception.class)
    public void testJuxtaposedOperationsThrowsException() throws Lexception {
        Queue<Token> q = Lexer.lex("++");
    }

    @Test(expected = Lexception.class)
    public void testEThrowsException() throws Lexception {
        Queue<Token> q = Lexer.lex("e");
    }

    @Test(expected = Lexception.class)
    public void testTwoDecimalPointsThrowsException() throws Lexception {
        Lexer.lex("3.1.6");
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

    @Test
    public void testMaximallyComplexCalculationReturnsCorrespondingTokenQueue() throws Lexception {
        Queue<Token> q = Lexer.lex("-123.4e5+-123.45e0-cos-3.2+4^3!");
        assertEquals(q.size(), 11);
    }
}
