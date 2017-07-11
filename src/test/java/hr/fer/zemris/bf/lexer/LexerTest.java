package hr.fer.zemris.bf.lexer;

import static org.junit.Assert.*;

import org.junit.Test;

public class LexerTest {

	@Test(expected = LexerException.class)
	public void predanNull() {
		new Lexer(null);
	}

	@Test
	public void predanPrazanString() {
		assertEquals(new Lexer("").nextToken(), new Token(TokenType.EOF, null));
	}

	@Test
	public void predanPrazanStringSRazmacima() {
		assertEquals(new Lexer("    \t\n   ").nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void predanaSamoKonstanta() {
		Lexer lexer = new Lexer("FaLse");
		assertEquals(lexer.nextToken(), new Token(TokenType.CONSTANT, Boolean.FALSE));
		assertEquals(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void predanaSamoNumerickaKonstanta() {
		Lexer lexer = new Lexer("1");
		assertEquals(lexer.nextToken(), new Token(TokenType.CONSTANT, Boolean.TRUE));
		assertEquals(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void predanOperatorIVarijabla() {
		Lexer lexer = new Lexer("not a");
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "not"));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "A"));
		assertEquals(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void predanSlozeniPrimjer1() {
		Lexer lexer = new Lexer("a xor b :+: c");
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "A"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "xor"));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "B"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "xor"));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "C"));
		assertEquals(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	
	@Test
	public void predanSlozeniPrimjer2() {
		Lexer lexer = new Lexer("A aNd b");
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "A"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "and"));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "B"));
		assertEquals(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	@Test
	public void predanSlozeniPrimjer3() {
		Lexer lexer = new Lexer("(a + b) xor (c or d)");
		assertEquals(lexer.nextToken(), new Token(TokenType.OPEN_BRACKET, '('));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "A"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "or"));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "B"));
		assertEquals(lexer.nextToken(), new Token(TokenType.CLOSED_BRACKET, ')'));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "xor"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPEN_BRACKET, '('));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "C"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "or"));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "D"));
		assertEquals(lexer.nextToken(), new Token(TokenType.CLOSED_BRACKET, ')'));
		assertEquals(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	
	@Test
	public void predanSlozeniPrimjer4() {
		Lexer lexer = new Lexer("(c or d) mor not (a or b)");
		assertEquals(lexer.nextToken(), new Token(TokenType.OPEN_BRACKET, '('));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "C"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "or"));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "D"));
		assertEquals(lexer.nextToken(), new Token(TokenType.CLOSED_BRACKET, ')'));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "MOR"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "not"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPEN_BRACKET, '('));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "A"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "or"));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "B"));
		assertEquals(lexer.nextToken(), new Token(TokenType.CLOSED_BRACKET, ')'));
		assertEquals(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	
	@Test
	public void predanSlozeniPrimjer5() {
		Lexer lexer = new Lexer("\n    not\n not\n \ta\t");
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "not"));
		assertEquals(lexer.nextToken(), new Token(TokenType.OPERATOR, "not"));
		assertEquals(lexer.nextToken(), new Token(TokenType.VARIABLE, "A"));
	}

	//pogreske
	@Test(expected = LexerException.class)
	public void predanPogresanPrimjer1() {
		Lexer lexer = new Lexer("_a");
		lexer.nextToken();
	}
	
	@Test(expected = LexerException.class)
	public void predanPogresanPrimjer2() {
		Lexer lexer = new Lexer("255");
		lexer.nextToken();
	}
	
	@Test(expected = LexerException.class)
	public void predanPogresanPrimjer3() {
		Lexer lexer = new Lexer("~");
		lexer.nextToken();
	}
	
	@Test(expected = LexerException.class)
	public void predanPogresanPrimjer4() {
		Lexer lexer = new Lexer("\f");
		lexer.nextToken();
	}
	
	@Test(expected = LexerException.class)
	public void predanPogresanPrimjer5() {
		Lexer lexer = new Lexer(":pero:");
		lexer.nextToken();
	}
	
	@Test(expected = LexerException.class)
	public void predanPogresanPrimjer6() {
		Lexer lexer = new Lexer("a and 10");
		lexer.nextToken();
		lexer.nextToken();
		lexer.nextToken();
	}
	
	
}
