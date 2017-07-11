package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;
import hr.fer.zemris.bf.parser.Parser;

/**
 * Razred koji implementira sučelje {@link NodeVisitor}. Ovaj razred predstavlja
 * konkretnog posjetitelja. Zadaća ovog posjetitelja jest da na zaslon ispisuje
 * generativno stablo nastalo parsiranjem primjerkom razreda {@link Parser} uz
 * vođenje računa o indentaciji (svaki puta kada uđe u neki operator,
 * indentaciju treba povećati za {@value #AMPLIFIER})
 * 
 * @see Parser
 * @see NodeVisitor
 * 
 * @author Davor Češljaš
 */
public class ExpressionTreePrinter implements NodeVisitor {
	/**
	 * Predstavlja privatnu konstantu koja se koristi za povećanje indentacije
	 * unutar metoda {@link #visit(UnaryOperatorNode)} i
	 * {@link #visit(BinaryOperatorNode)}.
	 */
	private static final int AMPLIFIER = 2;

	/**
	 * Predstavlja privatnu konstantu koja predstavlja niz znakova koji se
	 * sastoji od točno jedne praznine
	 */
	private static final String WHITESPACE = " ";

	/**
	 * Trenutna razina u generativnom stablu na kojoj se nalazi primjerak ovog
	 * razreda. Na početku 0.
	 */
	private int level;

	@Override
	public void visit(ConstantNode node) {
		printOnCurrentLevel(String.valueOf(node));
	}

	@Override
	public void visit(VariableNode node) {
		printOnCurrentLevel(node.toString());
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		printOnCurrentLevel(node.toString());
		level += AMPLIFIER;
		node.getChild().accept(this);
		level -= AMPLIFIER;
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		printOnCurrentLevel(node.toString());
		level += AMPLIFIER;
		node.getChildren().forEach(child -> child.accept(this));
		level -= AMPLIFIER;
	}

	/**
	 * Pomoćna metoda koja se koristi za formatirani ispis ovisno o razini na
	 * kojoj se trenutno nalazi primjerak ovog razreda.
	 *
	 * @param str
	 *            primjerak razreda {@link String} koji se formatirano ispisuje
	 */
	private void printOnCurrentLevel(String str) {
		StringBuilder sb = new StringBuilder(level + str.length());
		for (int i = 0; i < level; i++) {
			sb.append(WHITESPACE);
		}
		System.out.println(sb.append(str).toString());
	}

}
