package hr.fer.zemris.bf.model;

import hr.fer.zemris.bf.parser.Parser;

/**
 * Razred koji implementira sučelje {@link Node} te koji predstavlja završni
 * znak gramatike prilikom parsiranja primjerkom razreda {@link Parser}.
 * Konkretno razred predstavlja jednu konstantu unutar logičkog izraza. Razred
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
public class ConstantNode implements Node {

	/** Konstanta koju primjerak ovog razreda čuva. */
	private boolean value;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Prilikom
	 * inicijalizacije predana konstanta <b>value</b> sprema se unutar članske
	 * varijable koju ovaj primjerak razreda {@link ConstantNode} omata.
	 *
	 * @param value
	 *            konstanta koja se sprema unutar članske varijable koju ovaj
	 *            razred omata
	 */
	public ConstantNode(boolean value) {
		this.value = value;
	}

	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Metoda koja dohvaća konstantu koju omata primjerak ovog razreda.
	 *
	 * @return konstantu koju omata primjerak ovog razreda.
	 */
	public boolean getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value ? 1 : 0);
	}
}
