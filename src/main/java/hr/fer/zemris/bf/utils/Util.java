package hr.fer.zemris.bf.utils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import hr.fer.zemris.bf.model.Node;

/**
 * Razred koji se ponaša kao biblioteka metoda. Razred sadrži isključivo
 * statičke metode, koje služe kao pomoćne prilikom rada sa logičkim izrazima.
 * Razred nudi sljedećih 5 metoda:
 * <ul>
 * <li>{@link #forEach(List, Consumer)}
 * <li>
 * <li>{@link #filterAssignments(List, Node, boolean)}
 * <li>
 * <li>{@link #booleanArrayToInt(boolean[])}
 * <li>
 * <li>{@link #toSumOfMinterms(List, Node)}
 * <li>
 * <li>{@link #toProductOfMaxterms(List, Node)}
 * <li>
 * </ul>
 * 
 * Opis pojedinih metoda možete vidjeti klikom na link
 * 
 * @see Node
 * 
 * @author Davor Češljaš
 */
public final class Util {

	/**
	 * Baza brojevnog sustava u kojoj rade metode
	 * {@link #booleanArrayToInt(boolean[])} i {@link #forEach(List, Consumer)}
	 */
	private static final int BASE = 2;

	/**
	 * Defaultni konstruktor koji brani stvaranje primjeraka ovog razreda. Ova
	 * operacija je zabranjena jer ovaj razred predstavlja biblioteku statičkih
	 * metoda.
	 * 
	 * @throws UnsupportedOperationException
	 *             ukoliko korisnik pokuša stvoriti primjerak ovog razreda
	 */
	public Util() {
		throw new UnsupportedOperationException("Nemožete stvorit primjerka razreda: " + this.getClass());
	}

	/**
	 * Metode koja ima zadaću da za predanu {@link List}u varijabli
	 * predstavljenih primjercima razreda {@link String} generira redom sve
	 * kombinacije vrijednosti (kao da generira tablicu istinitosti), te za
	 * svaku kombinaciju vrijednosti poziva definirani <b>consumer</b>.
	 * Vrijednosti se generiraju upravo redosljedom kojim biste ih slagali u
	 * tablici istinitosti: od svih nula (tj. <code>false</code>) prema svim
	 * jedinicama (tj. <code>true</code>), gdje je nabrže mijenja najdesnija
	 * varijabla (zadnji element liste).
	 *
	 * 
	 * @param variables
	 *            {@link List} predanih varijabli predstavljenih primjercima
	 *            razreda {@link String}
	 * @param consumer
	 *            Strategija koja se poziva nad svakom od kombinacija
	 */
	public static void forEach(List<String> variables, Consumer<boolean[]> consumer) {
		int numberOfBits = variables.size();
		int numberOfCombinatons = (int) Math.pow(BASE, variables.size());
		for (int i = 0; i < numberOfCombinatons; i++) {
			consumer.accept(getCombination(numberOfBits, i));
		}
	}

	/**
	 * Pomoćna metoda koja dohvaća jednu binarnu kombinaciju (spremljenu unutar
	 * polja boolean zastavica) ovisno o broju bitova <b>numberOfBits</b> i
	 * broju koji pretvaramo u binarni oblik <b>number</b>.
	 *
	 * @param numberOfBits
	 *            broj bitova koji mora sadržavati svaki binarni broj, ujedino i
	 *            veličina vraćenog polja boolean zastavica
	 * 
	 * @param number
	 *            broj koji pretvaramo u binarni oblik
	 * 
	 * @return polje boolean zastavica koje reprezrntira binarni oblik broja
	 *         <b>number</b>
	 */
	private static boolean[] getCombination(int numberOfBits, int number) {
		boolean[] combination = new boolean[numberOfBits];
		// testiranje svakog bita unutar number pomoću logičke operacije I
		for (int i = 0; i < numberOfBits; i++) {
			combination[numberOfBits - 1 - i] = ((1 << i) & number) != 0;
		}
		return combination;
	}

	/**
	 * Metoda prima popis varijabli predstavljen sa {@link List} primjeraka
	 * razreda {@link String} i jedan izraz koji je primjerak razreda
	 * {@link Node} (vršni čvor generativnog stabla) te stvara sve kombinacije
	 * varijabli, uporabom posjetitelja {@link ExpressionEvaluator} računa
	 * vrijednost funkcije, i ako se ona podudara s vrijednosti koja je predana
	 * kao booleava zastavica <b>expressionValue</b> argument ove metode, dodaje
	 * tu kombinaciju u {@link Set} kombinacija koji na kraju vraća. Skup je
	 * tako podešen da mu iterator vraća elemente poretkom koji kombinacije
	 * imaju u tablici istinitosti.
	 *
	 * @param variables
	 *            popis varijabli predstavljen sa {@link List} primjeraka
	 *            razreda {@link String}
	 * @param expression
	 *            izraz koji je primjerak razreda {@link Node} (vršni čvor
	 *            generativnog stabla)
	 * @param expressionValue
	 *            vrijednosti koja je predana kao booleova zastavica, a s kojom
	 *            se moraju podudarati vrijednosti funkcije određene kombinacije
	 *            da bi ta kombinacija bila vraćena kroz povratnu vrijednost
	 * @return {@link Set} kombinacija čije se funkcije podudaraju sa booleovom
	 *         zastavicom <b>expressionValue</b>
	 */
	public static Set<boolean[]> filterAssignments(List<String> variables, Node expression, boolean expressionValue) {
		Set<boolean[]> matchedSet = new LinkedHashSet<>();
		ExpressionEvaluator eval = new ExpressionEvaluator(variables);
		forEach(variables, values -> {
			eval.setValues(values);
			expression.accept(eval);
			if (eval.getResult() == expressionValue) {
				matchedSet.add(values);
			}
		});

		return matchedSet;
	}

	/**
	 * Metoda prima polje booleovih vrijednosti <b>values</b> i pretvara ga u
	 * redni broj retka gdje se ta kombinacija nalazi u tablici istinitosti.
	 *
	 * @param values
	 *            polje booleovih vrijednosti
	 * @return redni broj retka gdje se ta kombinacija nalazi u tablici
	 *         istinitosti
	 */
	public static int booleanArrayToInt(boolean[] values) {
		int result = 0;
		for (int i = 0; i < values.length; i++) {
			result += values[i] ? Math.pow(BASE, values.length - i - 1) : 0;
		}

		return result;
	}

	/**
	 * Metoda vraća {@link Set} brojeva koji predstavljaju članove sume mintermi
	 * koje funkcija sadrži. Funkcija se sadrži od {@link List} varijabli
	 * <b>variables</b>, a sama funkcija parsirana je u primjerak razreda koji
	 * implementira sučelje {@link Node}, koji predstavlja vršni čvor
	 * generativnog stabla
	 *
	 * @param variables
	 *            {@link List} varijabli od kojih se funkcija sadrži
	 * @param expression
	 *            primjerak razreda koji implementira sučelje {@link Node}, koji
	 *            predstavlja vršni čvor generativnog stabla
	 * @return {@link Set} brojeva koji predstavljaju članove sume mintermi koje
	 *         funkcija sadrži.
	 */
	public static Set<Integer> toSumOfMinterms(List<String> variables, Node expression) {
		return performFilterAndConvert(variables, expression, true);
	}

	/**
	 * Metoda vraća {@link Set} brojeva koji predstavljaju članove produkta
	 * makstermi koje funkcija sadrži. Funkcija se sadrži od {@link List}
	 * varijabli <b>variables</b>, a sama funkcija parsirana je u primjerak
	 * razreda koji implementira sučelje {@link Node}, koji predstavlja vršni
	 * čvor generativnog stabla
	 *
	 * @param variables
	 *            {@link List} varijabli od kojih se funkcija sadrži
	 * @param expression
	 *            primjerak razreda koji implementira sučelje {@link Node}, koji
	 *            predstavlja vršni čvor generativnog stabla
	 * @return {@link Set} brojeva koji predstavljaju članove produkta makstermi
	 *         koje funkcija sadrži.
	 */
	public static Set<Integer> toProductOfMaxterms(List<String> variables, Node expression) {
		return performFilterAndConvert(variables, expression, false);
	}

	/**
	 * Pomoćna metoda koja vrši vađenje {@link Set}a brojeva koji predstavljaju
	 * članove produkta makstermi ili sume mintermi koje funkcija sadrži. O tome
	 * što se točno traži odlučuje predana booleova zastavica
	 * <b>expectedValue</b>. Kao pomoćne metode koriste se
	 * {@link #filterAssignments(List, Node, boolean)} te
	 * {@link #booleanArrayToInt(boolean[])}
	 * 
	 * @param variables
	 *            {@link List} varijabli od kojih se funkcija sadrži
	 * @param expression
	 *            primjerak razreda koji implementira sučelje {@link Node}, koji
	 *            predstavlja vršni čvor generativnog stabla
	 * @param expectedValue
	 *            ukoliko je ova zastavica <code>true</code> vadi se suma
	 *            mintermi, a ukoliko je <code>false</code> produkt makstermi
	 * @return {@link Set} brojeva koji predstavljaju članove produkta makstermi
	 *         koje funkcija sadrži.
	 */
	private static Set<Integer> performFilterAndConvert(List<String> variables, Node expression,
			boolean expectedValue) {
		return filterAssignments(variables, expression, expectedValue).stream().map(Util::booleanArrayToInt)
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}
}
