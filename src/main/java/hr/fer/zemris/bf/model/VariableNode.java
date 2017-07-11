package hr.fer.zemris.bf.model;

import java.util.Objects;

import hr.fer.zemris.bf.parser.Parser;

/**
 * Razred koji implementira sučelje {@link Node} te koji predstavlja završni
 * znak gramatike prilikom parsiranja primjerkom razreda {@link Parser}.
 * Konkretno razred predstavlja jednu varijablu unutar logičkog izraza. Razred
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
public class VariableNode implements Node {

	/** Naziv varijable koju primjerak ovog razreda čuva */
	private String name;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Prilikom
	 * inicijalizacije predani naziv varijable <b>name</b> sprema se unutar
	 * članske varijable koju ovaj primjerak razreda {@link VariableNode} omata.
	 *
	 * @param name
	 *            naziv varijable koja se sprema unutar članske varijable koju
	 *            ovaj razred omata
	 */
	public VariableNode(String name) {
		this.name = Objects.requireNonNull(name);
	}

	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Metoda koja dohvaća naziv varijable koji omata primjerak ovog razreda.
	 *
	 * @return naziv varijable koji omata primjerak ovog razreda.
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
