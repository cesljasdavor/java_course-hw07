package demo;

import java.util.List;
import java.util.stream.Collectors;

import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.utils.VariablesGetter;

/**
 * Razred koji predstavlja demonstracijski program koji demonstrira rad sa
 * primjerkom razreda {@link Parser} i primjerkom konkretnog posjetitelja
 * {@link VariablesGetter}
 * 
 * @see Parser
 * @see VariablesGetter
 * 
 * @author Davor Češljaš
 */
public class Problem3 {
	
	/**
	 * Metoda od koje započinje izvođenje ovog programa .
	 *
	 * @param args
	 *            argumenti naredbenog redka. Ovdje se ne koriste
	 */
	public static void main(String[] args) {
		Parser parser = new Parser("(c + a) xor (a or b)");
		VariablesGetter getter = new VariablesGetter();
		parser.getExpression().accept(getter);
		List<String> variables = getter.getVariables();
		System.out.println(variables.stream().collect(Collectors.joining(", ")));
	}
}
