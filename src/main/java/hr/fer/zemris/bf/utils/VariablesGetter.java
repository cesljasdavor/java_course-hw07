package hr.fer.zemris.bf.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;
import hr.fer.zemris.bf.parser.Parser;

/**
 * Razred koji implementira sučelje {@link NodeVisitor}. Ovaj razred predstavlja
 * konkretnog posjetitelja. Ovaj posjetitelj će odrediti sve varijable koje se
 * spominju u izrazu koji parsiran primjerkom razreda {@link Parser} i koje će
 * vratiti kao listu naziva varijabli sortiranu leksikografski pozivom metode
 * {@link #getVariables()}. Naravno, elementi liste su jedinstveni.
 * 
 * Primjer uporabe:
 * 
 * <pre>
 * Parser parser = new Parser("(c + a) xor (a or b)"); 
 * VariablesGetter getter = new VariablesGetter(); 
 * parser.getExpression().accept(getter);
 * List<String> variables = getter.getVariables();
 * <pre>
 * 
 * Rezultat će biti lista ("A", "B", "C").
 * 
 * @see Parser
 * @see NodeVisitor
 * 
 * @author Davor Češljaš
 */
public class VariablesGetter implements NodeVisitor {

	/**
	 * Članska varijabla koja predstavlja {@link Set} primjeraka razreda
	 * {@link String}. Primjerci razreda {@link String} spremljeni unutar ovog
	 * {@link Set}a predstavljaju sve varijable spomenute unutar nekog izraza
	 * koji se parsira primjerkom razreda {@link Parser}. Ova članska varijabla
	 * koristi se prilikom obilaska stabla.
	 */
	private Set<String> uniqueVariables = new HashSet<>();
	/**
	 * Članska varijabla koja predstavlja {@link List} primjeraka razreda
	 * {@link String}. Primjerci razreda {@link String} spremljeni unutar ove
	 * {@link Liste} predstavljaju sve varijable spomenute unutar nekog izraza
	 * koji se parsira primjerkom razreda {@link Parser}. Ova članska varijabla
	 * je lista koja se vraća korisniku ovog razreda
	 */
	private List<String> variables;

	@Override
	public void visit(ConstantNode node) {
	}

	@Override
	public void visit(VariableNode node) {
		uniqueVariables.add(node.getName());
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		node.getChild().accept(this);
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		node.getChildren().forEach(child -> child.accept(this));
	}

	/**
	 * Metoda koja dohvaća {@link List} imena svih varijabli spomenutih unutar
	 * nekog izraza koji je parsiran primjerkom razreda {@link Parser}. Imena su
	 * unikatna unutar ove liste.
	 *
	 * @return {@link List} imena svih varijabli spomenutih unutar nekog izraza
	 *         koji je parsiran primjerkom razreda {@link Parser}. Imena su
	 *         unikatna unutar ove liste.
	 */
	public List<String> getVariables() {
		if (variables == null) {
			variables = new ArrayList<>(uniqueVariables);
			variables.sort(null);
		}
		return variables;
	}

}
