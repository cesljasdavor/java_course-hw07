package hr.fer.zemris.bf.model;

import java.util.Objects;
import java.util.function.UnaryOperator;

import hr.fer.zemris.bf.parser.Parser;

/**
 * Razred koji implementira sučelje {@link Node} te koji predstavlja nezavršni
 * znak gramatike prilikom parsiranja primjerkom razreda {@link Parser}.
 * Konkretno razred predstavlja jednu unarnu operaciju logičkog izraza. Razred
 * također predstavlja konkretan element unutar
 * <a href="https://en.wikipedia.org/wiki/Visitor_pattern">oblikovnog obrasca
 * posjetitelj</a> te implementira metodu {@link Node#accept(NodeVisitor)}.
 * Važno je napomenuti da su su primjerci ovog razreda nepromijenjivi.
 *
 * @see Parser
 * @see Node
 * 
 * @author Davor Češljaš
 */
public class UnaryOperatorNode implements Node {

	/** Naziv operacije koju primjerak ovog razreda čuva. */
	private String name;

	/**
	 * Primjerak razreda koji implementiraju sučelje {@link Node}, a na koji je
	 * potrebno primjeniti ovu operaciju
	 */
	private Node child;

	/**
	 * Konkretna strategija koja implementira sučelje {@link UnaryOperator}. Ova
	 * strategija nad primjerkom razreda {@link Boolean} provodi logičku
	 * operaciju koju primjerak ovog razreda predstavlja.
	 */
	private UnaryOperator<Boolean> operator;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Prilikom
	 * inicijalizacije predana naziv binarne operacije <b>name</b> sprema se
	 * unutar članske varijable koju ovaj primjerak razreda
	 * {@link UnaryOperatorNode} omata. Isto se čini i za <b>children</b> te za
	 * <b>operator</b> parametre
	 *
	 * @param name
	 *            Naziv operacije koju primjerak ovog razreda treba čuvati
	 * @param child
	 *            primjerak razreda koji implementiraju sučelje {@link Node}, a
	 *            na koji je potrebno primjeniti ovu operaciju
	 * @param operator
	 *            Konkretna strategija koja implementira sučelje
	 *            {@link UnaryOperator}. Ova strategija nad primjerkom razreda
	 *            {@link Boolean} provodi logičku operaciju koju primjerak ovog
	 *            razreda predstavlja.
	 */
	public UnaryOperatorNode(String name, Node child, UnaryOperator<Boolean> operator) {
		this.name = Objects.requireNonNull(name);
		this.child = Objects.requireNonNull(child);
		this.operator = Objects.requireNonNull(operator);
	}

	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Metoda koja vraća naziv operacije koju primjerak ovog razreda čuva
	 *
	 * @return naziv operacije koju primjerak ovog razreda čuva
	 */
	public String getName() {
		return name;
	}

	/**
	 * Metoda koja vraća primjerak razreda, spremljenog unutar primjerka ovog
	 * razreda ,koji implementiraju sučelje {@link Node}, a na koje je potrebno
	 * primjeniti ovu operaciju
	 *
	 * @return primjerak razreda, spremljenog unutar primjerka ovog razreda
	 *         ,koji implementiraju sučelje {@link Node}, a na koje je potrebno
	 *         primjeniti ovu operaciju
	 */
	public Node getChild() {
		return child;
	}

	/**
	 * Metoda koja vraća konkretnu strategiju koja implementira sučelje
	 * {@link UnaryOperator}, a koja je omotana primjerkom ovog razreda
	 *
	 * @return konkretnu strategiju koja implementira sučelje
	 *         {@link UnaryOperator}, a koja je omotana primjerkom ovog razreda
	 */
	public UnaryOperator<Boolean> getOperator() {
		return operator;
	}

	@Override
	public String toString() {
		return name;
	}
}
