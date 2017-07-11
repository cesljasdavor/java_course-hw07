package hr.fer.zemris.bf.lexer;

/**
 * Enumeracija koja predstavlja tip tokena koji je primjerak razreda
 * {@link SmartToken}. Moguće vrijednosti:
 * <ul>
 * <li>{@link TokenType#EOF}</li>
 * <li>{@link TokenType#VARIABLE}</li>
 * <li>{@link TokenType#CONSTANT}</li>
 * <li>{@link TokenType#OPERATOR}</li>
 * <li>{@link TokenType#OPEN_BRACKET}</li>
 * <li>{@link TokenType#CLOSED_BRACKET}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public enum TokenType {

	/** Predstavlja oznaku kraja izraza */
	EOF,

	/**
	 * Predstavlja varijablu unutar taga. Varijabla je niz znakova koja
	 * započinje slovom ili znakom '_' i nakon toga može sadržavati proizvoljan
	 * broj slova, brojki ili znakova '_'
	 */
	VARIABLE,

	/**
	 * Predstavlja cjelobrojnu konstantu. Cjelobrojna konstanta je sve što se
	 * sastoji od proizvoljnog broja znamenki
	 */
	CONSTANT,

	/**
	 * Jedan od znakova '+', '*', '!' ili niza ":+:". Također operatori su
	 * predstavljeni nizovima "and", "or", "xor" ili "not".
	 */
	OPERATOR,

	/** Predstavlja jedan znak '(' */
	OPEN_BRACKET,


	/** Predstavlja jedan znak ')' */
	CLOSED_BRACKET
}
