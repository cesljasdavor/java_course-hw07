package hr.fer.zemris.bf.model;

/**
 * Sučelje koje oblikuje Posjetitelja unutar
 * <a href="https://en.wikipedia.org/wiki/Visitor_pattern">oblikovnog obrasca
 * posjetitelj</a>. Implementator ovog sučelja dužan je ponuditi četiri metode
 * za konkretne elemente, koji su primjerci razreda koji implementira sučelje
 * {@link Node}. To su redom:
 * <ol>
 * <li>{@link #visit(BinaryOperatorNode)}</li>
 * <li>{@link #visit(ConstantNode)}</li>
 * <li>{@link #visit(UnaryOperatorNode)}</li>
 * <li>{@link #visit(VariableNode)}</li>
 * </ol>
 * 
 * @see Node
 * 
 * @author Davor Češljaš
 */
public interface NodeVisitor {

	/**
	 * Metoda za posjet čvoru koji je primjerak razreda {@link ConstantNode}.
	 *
	 * @param node
	 *            primjerak razreda {@link ConstantNode}
	 * 
	 * @see ConstantNode
	 */
	void visit(ConstantNode node);

	/**
	 * Metoda za posjet čvoru koji je primjerak razreda {@link VariableNode}.
	 *
	 * @param node
	 *            primjerak razreda {@link VariableNode}
	 * @see VariableNode
	 */
	void visit(VariableNode node);

	/**
	 * Metoda za posjet čvoru koji je primjerak razreda
	 * {@link UnaryOperatorNode}.
	 *
	 * @param node
	 *            primjerak razreda {@link UnaryOperatorNode}
	 * 
	 * @see UnaryOperatorNode
	 */
	void visit(UnaryOperatorNode node);

	/**
	 * Metoda za posjet čvoru koji je primjerak razreda
	 * {@link BinaryOperatorNode}.
	 *
	 * @param node
	 *            primjerak razreda {@link BinaryOperatorNode}
	 *            
	 * @see BinaryOperatorNode
	 */
	void visit(BinaryOperatorNode node);
}
