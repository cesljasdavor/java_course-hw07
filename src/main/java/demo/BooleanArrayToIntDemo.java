package demo;

import java.util.Arrays;

import hr.fer.zemris.bf.utils.Util;

/**
 * Razred koji predstavlja demonstracijski program koji poziva metodu
 * {@link Util#booleanArrayToInt(boolean[])}.
 * 
 * @see Util
 * @author Davor Češljaš
 */
public class BooleanArrayToIntDemo {

	/**
	 * Metoda od koje započinje izvođenje ovog programa 
	 *
	 * @param args
	 *            argumenti naredbenog redka. Ovdje se ne koriste
	 */
	public static void main(String[] args) {
		boolean[] values = new boolean[] { false, false, true, true };
		System.out.println(Arrays.toString(values) + " reprezentira dekadski: " + Util.booleanArrayToInt(values));
	}

}
