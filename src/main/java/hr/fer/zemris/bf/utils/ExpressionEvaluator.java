package hr.fer.zemris.bf.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.BinaryOperator;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * Razred koji implementira sučelje {@link NodeVisitor}. Ovaj razred predstavlja
 * konkretnog posjetitelja. Zadaća ovog posjetitelja jest izračun vrijednosti
 * izraza za zadanu kombinaciju ulaznih varijabli.
 * 
 * @see NodeVisitor
 * 
 * @author Davor Češljaš
 */
public class ExpressionEvaluator implements NodeVisitor {

	/**
	 * Članska vaijabla koja predstavlja polje booleovih vrijednosti čija
	 * veličina odgovara broju varijabli. Svaka varijabla ima vrijednost
	 * spremljenu unutar ovog polja
	 */
	private boolean[] values;

	/**
	 * Članska varijabla koja je {@link Map}a koja ime varijable preslikava u
	 * redni broj.
	 */
	private Map<String, Integer> positions;

	/**
	 * Članska varijabla u koju se spremaju i s koje ze vade rezultati obrade.
	 * Ukolikom je obrada bila uspješna na stogu bi se trebala nalaziti samo
	 * jedna vrijednost (rezultat)
	 */
	private Stack<Boolean> stack = new Stack<>();

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. U konstruktoru se
	 * stvara {@link Map} koji ime varijable preslikava u redni broj. Taj redni
	 * broj odgovara poziciji na kojoj se pojedina varijabla nalazi unutar
	 * predanog argumenta <b>variabes</b>
	 * 
	 * @param variables
	 *            {@link List} varijabli koji se nalaze unutar parsiranog izraza
	 */
	public ExpressionEvaluator(List<String> variables) {
		positions = new HashMap<>(variables.size());
		for (int i = 0, len = variables.size(); i < len; i++) {
			positions.put(variables.get(i), i);
		}
	}

	/**
	 * 
	 * Metoda koja služi za postavljanje niza vrijednosti koje varijable imaju
	 * unutar izraza. Ukoliko se preda različit broj vrijednosti u odnosu na
	 * broj varijabli metoda baca {@link IllegalStateException}. Metoda će
	 * iskopirati vrijednosti iz dobivenog polja u interno polje vrijednosti
	 * varijabli.
	 *
	 * 
	 * @param values
	 *            niza vrijednosti koje varijable imaju unutar izraza.
	 */
	public void setValues(boolean[] values) {
		if (values.length != positions.size()) {
			throw new IllegalArgumentException(
					String.format("Predali ste netočan broj booleovih vrijednosti. Tražio sam: %d dobio sam: %d",
							positions.size(), values.length));
		}

		start();
		this.values = Arrays.copyOf(values, values.length);
	}

	public void visit(ConstantNode node) {
		stack.push(node.getValue());
	}

	public void visit(VariableNode node) {
		Integer position = positions.get(node.getName());
		if (position == null) {
			throw new IllegalStateException("Unutar predanih varijabli ne postoji varijabla: " + node.getName());
		}

		stack.push(values[position]);
	}

	public void visit(UnaryOperatorNode node) {
		node.getChild().accept(this);
		stack.push(node.getOperator().apply(stack.pop()));
	}

	public void visit(BinaryOperatorNode node) {
		List<Node> children = node.getChildren();
		children.forEach(child -> child.accept(this));

		performBinaryOperation(node.getOperator(), children.size());
	}

	/**
	 * Pomoćna metoda metodi {@link #visit(BinaryOperatorNode)} koja sa stoga
	 * vadi <b>childrenCount</b> vrijednosti i računa rezultat koristeći
	 * <b>biOperator</b>
	 * 
	 * @param biOperator
	 *            strategija za izračun rezultata od primjerka razreda
	 *            {@link BinaryOperatorNode}
	 * @param childrenCount
	 *            broj čvorova djece koje primjerak razreda
	 *            {@link BinaryOperatorNode} sadrži
	 */
	private void performBinaryOperation(BinaryOperator<Boolean> biOperator, int childrenCount) {
		boolean result = stack.pop();
		for (int i = 1; i < childrenCount; i++) {
			result = biOperator.apply(result, stack.pop());
		}
		stack.push(result);
	}

	/**
	 * Metoda koja služi za reinicijalizaciju primjerka ovog razreda čime se
	 * interni stog briše i tako priprema za provođenje novog izračuna. Metodu
	 * poziva metoda {@link #setValues(boolean[])} prije početka izvođenja
	 */
	public void start() {
		stack.clear();
	}

	/**
	 * Metoda koja vraća rezultat provedenog obilaska korištenjem primjerka ovog
	 * razreda. Rezultat će biti vrijednosti izraza za zadanu kombinaciju
	 * ulaznih varijabli. Važno je napomenuti da se pozivom ove metode ne
	 * mijenja stanje primjerka ovog razreda te će time ova metoda vraćati isti
	 * rezultat sve do poziva metode {@link #start()} ili
	 * {@link #setValues(boolean[])}.
	 *
	 * @return vrijednosti izraza za zadanu kombinaciju ulaznih varijabli
	 */
	public boolean getResult() {
		if (stack.size() != 1) {
			throw new IllegalStateException(
					"Izračun nije uspio. Kao rezultat dobiveno je " + stack.size() + " elemenata");
		}
		return stack.peek();
	}
}
