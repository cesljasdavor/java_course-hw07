package hr.fer.zemris.bf.model;

/**
 * Sučelje predstavlja nepromjenjivi čvor generativnog stabla prilikom
 * parsiranja primjerkom razreda {@link Parser}. Formalno, svaki razred koji
 * nasljeđuje ovaj razred predstavlja nezavršni ili završni znak gramatike
 * spomenutog parsera. Sučelje također predstavlja "element" unutar
 * <a href="https://en.wikipedia.org/wiki/Visitor_pattern">oblikovnog obrasca
 * posjetitelj</a>. Pripadni posjetitelji oblikovani su sučeljem
 * {@link NodeVisitor}
 * 
 * @see Parser
 * @see NodeVisitor
 * 
 * @author Davor Češljaš
 */
public interface Node {

	/**
	 * Metoda koja poziva jednu od metoda primjerka razreda koji implementira
	 * sučelje {@link NodeVisitor} ovisno o tome unutar koje implementacije se
	 * nalazi.
	 * 
	 * @param primjerak
	 *            razreda koji implementira sučelje {@link NodeVisitor}.
	 * @see NodeVisitor
	 */
	void accept(NodeVisitor visitor);
}
