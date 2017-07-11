package hr.fer.zemris.bf.model;

import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;

import hr.fer.zemris.bf.parser.Parser;

/**
 * Razred koji implementira sučelje {@link Node} te koji predstavlja nezavršni
 * znak gramatike prilikom parsiranja primjerkom razreda {@link Parser}.
 * Konkretno razred predstavlja jednu binarnu operaciju logičkog izraza. Razred
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
public class BinaryOperatorNode implements Node {

	/** Naziv operacije koju primjerak ovog razreda čuva. */
	private String name;

	/**
	 * Svi primjerci razreda koji implementiraju sučelje {@link Node}, a na koje
	 * je potrebno primjeniti ovu operaciju
	 */
	private List<Node> children;

	/**
	 * Konkretna strategija koja implementira sučelje {@link BinaryOperator}.
	 * Ova strategija nad parom primjerak razreda {@link Boolean} provodi
	 * logičku operaciju koju primjerak ovog razreda predstavlja
	 */
	private BinaryOperator<Boolean> operator;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Prilikom
	 * inicijalizacije predana naziv binarne operacije <b>name</b> sprema se
	 * unutar članske varijable koju ovaj primjerak razreda
	 * {@link BinaryOperatorNode} omata. Isto se čini i za <b>children</b> te za
	 * <b>operator</b> parametre
	 *
	 * @param name
	 *            Naziv operacije koju primjerak ovog razreda treba čuvati
	 * @param children
	 *            {@link List} primjeraka razreda koji implementiraju sučelje
	 *            {@link Node}, a na koje je potrebno primjeniti ovu operaciju
	 * @param operator
	 *            Konkretna strategija koja implementira sučelje
	 *            {@link BinaryOperator}. Ova strategija nad parom primjerak
	 *            razreda {@link Boolean} provodi logičku operaciju koju
	 *            primjerak ovog razreda predstavlja
	 */
	public BinaryOperatorNode(String name, List<Node> children, BinaryOperator<Boolean> operator) {
		this.name = Objects.requireNonNull(name);
		this.children = Objects.requireNonNull(children);
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
	 * Metoda koja vraća {@link List} primjeraka razreda, spremljenih unutar
	 * primjerka ovog razreda ,koji implementiraju sučelje {@link Node}, a na
	 * koje je potrebno primjeniti ovu operaciju
	 *
	 * @return {@link List} primjeraka razreda, spremljenih unutar primjerka
	 *         ovog razreda ,koji implementiraju sučelje {@link Node}, a na koje
	 *         je potrebno primjeniti ovu operaciju
	 */
	public List<Node> getChildren() {
		return children;
	}

	/**
	 * Metoda koja vraća konkretnu strategiju koja implementira sučelje
	 * {@link BinaryOperator}, a koja je omotana primjerkom ovog razreda
	 *
	 * @return konkretnu strategiju koja implementira sučelje
	 *         {@link BinaryOperator}, a koja je omotana primjerkom ovog razreda
	 */
	public BinaryOperator<Boolean> getOperator() {
		return operator;
	}

	@Override
	public String toString() {
		return name;
	}

}
