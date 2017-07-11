package hr.fer.zemris.bf.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

import hr.fer.zemris.bf.lexer.Lexer;
import hr.fer.zemris.bf.lexer.LexerException;
import hr.fer.zemris.bf.lexer.Token;
import hr.fer.zemris.bf.lexer.TokenType;
import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Razred koji predstavlja sintaksni analizator. Razred sadrži primjerak razreda
 * {@link Lexer} te nad njim poziva {@link Lexer#nextToken()} i gradi
 * generativno stablo. Razred prilikom gradnje stabla koristi razred
 * {@link Node} i razrede izvedene iz njega kao čvorove (nezavršni znakovi
 * gramatike). Razred također koristi razrede {@link ConstantNode} i
 * {@link VariableNode} kao listove generativnog stabla (završni znakovi
 * gramatike). Razred započinje parsiranje prilikom inicijalizacije (poziva
 * konstruktora). Rezultat je moguće dohvatiti pomoću metode
 * {@link #getExpression()}. Parser za parsiranje koristi metodu rekurzivnog
 * spusta <a href= "https://hr.wikipedia.org/wiki/Lijeva_rekurzija">Lijeva
 * rekurzija</a>
 *
 * @see Lexer
 * @see Node
 * 
 * @author Davor Češljaš
 */
public class Parser {

	/**
	 * Predstavlja privatnu konstantu koja se za usporedbu koristi u metodi
	 * {@link #checkIsCorrectLength(List)}. Sama konstanta predstavlja minimalni
	 * broj elemenata unutar {@link List}e
	 */
	private static final int MINIMUM_LENGTH = 2;

	/** Vršni čvor generativnog stabla */
	private Node expression;

	/** Primjerak leksičkog analizatora koji se koristi prilikom parsiranja. */
	private Lexer lexer;

	/** Zadnje izvađeni token pozivom metode {@link Lexer#nextToken()} */
	private Token currentToken;

	/**
	 * Konstruktor koji stvara primjerak razreda {@link Lexer} i predaje mu
	 * predani izraz <b>expression</b>. Nakon uspješnog stvaranja leksičkog
	 * analizatora kreće sintaksna analiza unutar koje se gradi generativno
	 * stablo.
	 *
	 * @param expression
	 *            sadržaj logičkog izraza koji je potrebno parsirati u
	 *            generativno stablo
	 * 
	 * @throws ParserException
	 *             ukoliko iz predanog teksta <b>expression</b> nije moguće
	 *             stvoriti generativno stablo
	 */
	public Parser(String expression) {
		try {
			lexer = new Lexer(expression);
		} catch (LexerException e) {
			throw new ParserException("Leksička analiza: " + e.getMessage());
		}

		setCurrentToken();
		this.expression = s();
	}

	/**
	 * Pomoćna metoda koja predstavlja produkciju gramatike S -> E1. Od ove
	 * metode započinje rekurzivni spust.
	 *
	 * @return vršni čvor generativnog stabla
	 */
	private Node s() {
		Node node = e1();
		if (!currentToken.equals(Lexer.EOF_TOKEN)) {
			throw new ParserException("Nisam uspio parsirati sve znakove. Stao sam na: " + currentToken);
		}
		return node;
	}

	/**
	 * Pomoćna metoda koja predstavlja produkciju gramatike E1-> E2 (OR E2)*.
	 * Metoda ima dva scenarija ovisno o tome slijedi li nakon E2 operator "or"
	 * ili ne. Ukoliko slijedi, metoda služi za generiranje djece tog operatora
	 * i stvara se {@link BinaryOperatorNode}. Ukoliko ne poziva se metoda
	 * {@link #e2()}
	 * 
	 * @return čvor generativnog stabla
	 */
	private Node e1() {
		return binaryNode(Lexer.OR_TOKEN, () -> e2(), Boolean::logicalOr);
	}

	/**
	 * Pomoćna metoda koja predstavlja produkciju gramatike E2-> E3 (XOR E3)*.
	 * Metoda ima dva scenarija ovisno o tome slijedi li nakon E3 operator "xor"
	 * ili ne. Ukoliko slijedi, metoda služi za generiranje djece tog operatora
	 * i stvara se {@link BinaryOperatorNode}. Ukoliko ne poziva se metoda
	 * {@link #e3()}
	 * 
	 * @return čvor generativnog stabla
	 */
	private Node e2() {
		return binaryNode(Lexer.XOR_TOKEN, () -> e3(), Boolean::logicalXor);
	}

	/**
	 * Pomoćna metoda koja predstavlja produkciju gramatike E3-> E4 (AND E4)*.
	 * Metoda ima dva scenarija ovisno o tome slijedi li nakon E4 operator "and"
	 * ili ne. Ukoliko slijedi, metoda služi za generiranje djece tog operatora
	 * i stvara se {@link BinaryOperatorNode}. Ukoliko ne poziva se metoda
	 * {@link #e4()}
	 * 
	 * @return čvor generativnog stabla
	 */
	private Node e3() {
		return binaryNode(Lexer.AND_TOKEN, () -> e4(), Boolean::logicalAnd);
	}

	/**
	 * Pomoćna metoda koja izvodi rekurzivni spust za metode {@link #e1()},
	 * {@link #e2()} i {@link #e3()}. Kao argumenti se predaju očekivani
	 * primjerak razreda {@link Token} koji predstavlja operator između pojednih
	 * nezavršnih znakova produkcije, primjerak razreda koji implementira
	 * sučelje {@link Supplier}, a unutar čije se metode {@link Supplier#get()}
	 * poziva metoda koja je sljedeća u rekurzivnom spustu te primjerak razreda
	 * koji implementira sučelje {@link BinaryOperator} koji predstavlja
	 * konkretnu strategiju koja oblikuje operaciju među čvorovima djecom
	 * primjerka razreda {@link BinaryOperatorNode} jednom kada i ako ga
	 * stvorimo.
	 *
	 * @param expectedOperator
	 *            očekivani primjerak razreda {@link Token} koji predstavlja
	 *            operator između pojednih čvorova
	 * @param methodToCall
	 *            primjerak razreda koji implementira sučelje {@link Supplier},
	 *            a unutar čije se metode {@link Supplier#get()} poziva metoda
	 *            koja je sljedeća u rekurzivnom spustu
	 * @param operator
	 *            primjerak razreda koji implementira sučelje
	 *            {@link BinaryOperator} koji predstavlja konkretnu strategiju
	 *            koja oblikuje operaciju među čvorovima djecom primjerka
	 *            razreda {@link BinaryOperatorNode} jednom kada i ako ga
	 *            stvorimo
	 * @return čvor generativnog stabla
	 */
	private Node binaryNode(Token expectedOperator, Supplier<Node> methodToCall, BinaryOperator<Boolean> operator) {
		Node child = methodToCall.get();
		if (!currentToken.equals(expectedOperator)) {
			return child;
		}

		List<Node> children = new ArrayList<>();
		children.add(child);
		while (!isEOFToken() && currentToken.equals(expectedOperator)) {
			consumeAndSetToken();
			children.add(methodToCall.get());
		}
		checkIsCorrectLength(children);
		return new BinaryOperatorNode(expectedOperator.getTokenValue().toString(), children, operator);

	}

	/**
	 * Pomoćna metoda koja provjerava je li predana {@link List}a veličine barem
	 * {@value #MINIMUM_LENGTH}. Ukoliko nije metoda baca
	 * {@link ParserException}
	 *
	 * @param children
	 *            lista koja se provjerava
	 * 
	 * @throws ParserException
	 *             ukoliko veličina predane {@link List}e nije barem
	 *             {@value #MINIMUM_LENGTH}
	 */
	private void checkIsCorrectLength(List<Node> children) {
		if (children.size() < MINIMUM_LENGTH) {
			throw new ParserException("Mora postojati minimalno dvoje djece za binarni operator!");
		}
	}

	/**
	 * Pomoćna metoda koja predstavlja produkciju gramatike E4-> NOT E4 | E5.
	 * Metoda se koristi za generiranje operatora "not" proizvoljan broj puta.
	 * Metoda u tom scenariju stvara {@link UnaryOperatorNode}. Ako operator
	 * "not" ne postoji poziv metode {@link #e5()}.
	 * 
	 * @return čvor generativnog stabla
	 */
	private Node e4() {
		if (currentToken.equals(Lexer.NOT_TOKEN)) {
			return new UnaryOperatorNode(consumeAndSetToken().getTokenValue().toString(), e4(), bool -> !bool);
		}
		return e5();
	}

	/**
	 * Pomoćna metoda koja predstavlja produkciju gramatike E5-> KONST | VAR |
	 * '(' E1 ')'. Metoda ima tri scenarija:
	 * <ul>
	 * <li>Sljedeći token je tipa {@link TokenType#CONSTANT} -> generira se novi
	 * primjerak razreda {@link ConstantNode} s vrijednošću spremljenom u
	 * primjerku razreda {@link Token}</li>
	 * <li>Sljedeći token je tipa {@link TokenType#VARIABLE} -> generira se novi
	 * primjerak razreda {@link VariableNodeNode} s vrijednošću spremljenom u
	 * primjerku razreda {@link Token}</li>
	 * <li>Sljedeći token je tipa {@link TokenType#OPEN_BRACKET} -> poziva se
	 * metoda {@link #e1()} i po povratku se provjerava je li sljedeći primjerak
	 * razreda {@link Token} tipa {@link TokenType#CLOSED_BRACKET}. Ako nije
	 * baca se {@link ParserException}</li>
	 * </ul>
	 * 
	 * Ako sljedeći token nije niti jedan od navedenih baca se
	 * {@link ParserException}
	 * 
	 * @return čvor generativnog stabla
	 * 
	 * @throws ParserException
	 *             ukoliko u trećem scenariju prilikom povratka iz metode
	 *             {@link #e1()} sljedeći primjerak razreda {@link Token} nije
	 *             tipa {@link TokenType#CLOSED_BRACKET} ili je tokon provjere
	 *             došao neočekivani primjerak razreda {@link Token}
	 */
	private Node e5() {
		switch (currentToken.getTokenType()) {
		case VARIABLE:
			return new VariableNode(consumeAndSetToken().getTokenValue().toString());
		case CONSTANT:
			return new ConstantNode((Boolean) consumeAndSetToken().getTokenValue());
		case OPEN_BRACKET:
			consumeAndSetToken();
			Node node = e1();
			if (!currentToken.equals(Lexer.CLOSED_BRACKET_TOKEN)) {
				throw new ParserException("Očekivao sam zatvorenu zagradu, a dobio sam token: " + currentToken);
			}
			consumeAndSetToken();
			return node;
		default:
			throw new ParserException("Nisam očekivao token: " + currentToken);
		}
	}

	/**
	 * Metoda koja dohvaća vršni čvor generativnog stabla.
	 *
	 * @return vršni čvor generativnog stabla.
	 */
	public Node getExpression() {
		return expression;
	}

	/**
	 * Pomoćna metoda koja poziva metodu {@link Lexer#nextToken()} nad
	 * primjerkom razreda {@link Lexer}(atribut ovog razreda). Metoda propagira
	 * iznimku leksičkog analizatora kao {@link ParserException} sa
	 * odgovarajućom porukom.
	 * 
	 * @throws ParserException
	 *             ukoliko leksički analizator baci {@link LexerException}
	 */
	private void setCurrentToken() {
		try {
			currentToken = lexer.nextToken();
		} catch (LexerException e) {
			throw new ParserException("Leksički analizator: " + e.getMessage());
		}
	}

	/**
	 * Pomoćna metoda koja vraća primjerak razreda {@link Token} koji je
	 * trenutno unutar {@link #currentToken} te potom ukoliko
	 * {@link #isEOFToken()} vrati <b>false</b> poziva metodu
	 * {@link #setCurrentToken()}.
	 *
	 * @return primjerak razreda {@link Token} koji je trenutno unutar
	 *         {@link #currentToken}
	 */
	private Token consumeAndSetToken() {
		Token toReturn = currentToken;
		if (!isEOFToken()) {
			setCurrentToken();
		}
		return toReturn;
	}

	/**
	 * Pomoćna metoda koja provjerava je li trenutni primjerak razreda
	 * {@link Token} spremljen u {@link #currentToken} tipa jednak
	 * {@link Lexer#EOF_TOKEN}.
	 *
	 * @return <b>true</b> ako je trenutni primjerak razreda {@link Token}
	 *         spremljen u {@link #currentToken} tipa jednak
	 *         {@link Lexer#EOF_TOKEN}, <b>false</b> inače.
	 */
	private boolean isEOFToken() {
		return currentToken.equals(Lexer.EOF_TOKEN);
	}
}
