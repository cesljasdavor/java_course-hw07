package demo;

import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.utils.ExpressionTreePrinter;

/**
 * Razred koji predstavlja demonstracijski program koji demonstrira rad sa
 * primjerkom razreda {@link Parser} i primjerkom konkretnog posjetitelja
 * {@link ExpressionTreePrinter}
 * 
 * @see Parser
 * @see ExpressionTreePrinter
 * 
 * @author Davor Češljaš
 */
public class ParserDemo {

	/**
	 * Metoda od koje započinje izvođenje ovog programa .
	 *
	 * @param args
	 *            argumenti naredbenog redka. Ovdje se ne koriste
	 */
	public static void main(String[] args) {
		Parser parser = new Parser("(d or b) xor not (a or c)");
		parser.getExpression().accept(new ExpressionTreePrinter());
	}

}
