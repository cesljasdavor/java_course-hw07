package hr.fer.zemris.bf.lexer;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Pomoćni razred koji se koristi prilikom leksičke analize ulaznog niza
 * primjerkom razreda {@link Lexer}. Razred predstavlja jedan token
 * (leksičku jedinku). Primjerci ovog razreda su nepromjenjivi.
 * 
 * @see Lexer
 * 
 * @author Davor Češljaš
 */
public class Token {
	
	/** Vrijednost leksičke jedinke */
	private Object tokenValue;
	
	/** Tip leksičke jedinke */
	private TokenType tokenType;
	
	/**
	 * Konstruktor koji inicijalizira atribute leksičke jedinke.
	 *
	 * @param tokenType
	 *            tip leksičke jedinke
	 * @param tokenValue
	 *            vrijednost leksičke jedinke
	 */
	public Token(TokenType tokenType, Object tokenValue) {
		this.tokenValue = tokenValue;
		this.tokenType = Objects.requireNonNull(tokenType);
	}
	
	/**
	 * Dohvaća tip leksičke jedinke
	 *
	 * @return tip leksičke jedinke
	 */
	public TokenType getTokenType() {
		return tokenType;
	}
	
	/**
	 * Dohvaća vrijednost leksičke jedinke
	 *
	 * @return vrijednost leksičke jedinke
	 */
	public Object getTokenValue(){
		return tokenValue;
	}

	public String toString(){
		StringJoiner sj = new StringJoiner(", ");
		sj.add("Tip: " + tokenType);
		sj.add("Vrijednost: " + tokenValue);
		if(tokenValue != null) {
			sj.add("Vrijednost je primjerak razreda: " + tokenValue.getClass());
		} 
		return sj.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokenType == null) ? 0 : tokenType.hashCode());
		result = prime * result + ((tokenValue == null) ? 0 : tokenValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (tokenType != other.tokenType)
			return false;
		if (tokenValue == null) {
			if (other.tokenValue != null)
				return false;
		} else if (!tokenValue.equals(other.tokenValue))
			return false;
		return true;
	}
}
