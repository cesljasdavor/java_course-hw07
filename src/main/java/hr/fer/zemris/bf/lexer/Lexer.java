package hr.fer.zemris.bf.lexer;

/**
 * Razred koji predstavlja leksički analizator koji se koristi prilikom
 * parsiranja primjerkom razreda{@link Parser}. Primjerak ovog razreda koristi
 * se za analizu predanog logičkog izraz. Logički izraz u sebi smije imati
 * operatore {@value #AND} (ili kraće {@value #AND_SIGN}), {@value #OR} (ili
 * kraće {@value #OR_SIGN}), {@value #XOR} (ili kraće {@value #XOR_SIGN}) ili
 * {@value #NOT} (ili kraće {@value #NOT_SIGN}). Važno je napomenuti da je
 * {@value #NOT} desno asocijativni unarni operator te će time negirati jedna
 * izraz, konstantu ili varijablu desno od sebe. Za konstantu se prihvaćaju
 * unosi {@value #TRUE} (ili kraće {@value #TRUE_NUMERIC}) te {@value #FALSE}
 * (ili kraće {@value #FALSE_NUMERIC}). Razred za dohvat tokena nudi metodu
 * {@link #nextToken()}. Tokeni koji izlaze iz ovog analizatora oblikovani su
 * razredom {@link Token}. Postoje konstantni tokeni koji se vračaju u slučaju
 * da leksički analizator naleti na neku izvedbu tog tokena u nizu:
 * <ul>
 * <li>{@link #AND_TOKEN}</li>
 * <li>{@link #OR_TOKEN}</li>
 * <li>{@link #XOR_TOKEN}</li>
 * <li>{@link #NOT_TOKEN}</li>
 * <li>{@link #OPEN_BRACKET_TOKEN}</li>
 * <li>{@link #CLOSED_BRACKET_TOKEN}</li>
 * <li>{@link #EOF_TOKEN}</li>
 * </ul>
 * 
 * Tokeni su eksternizirani u konstante zbog vrlo čestog korištenja, te se time
 * nalaze samo na jednom mjestu u memoriji, što za leksičku analizu puno izraza
 * primjercima ovog razreda čini veliku prednost
 * 
 * Primjer jednog izraza kojeg ovaj analizator može tokenizirati: <i>(a or b)
 * and not ((c :+: d) xor (f * g)) </i>
 * 
 * @see Token
 * 
 * @author Davor Češljaš
 */
public class Lexer {

	/** Predstavlja privatan konstantan niz znakova "and" */
	private static final String AND = "and";

	/** Predstavlja privatan konstantan niz znakova "not" */
	private static final String NOT = "not";

	/** Predstavlja privatan konstantan niz znakova "or" */
	private static final String OR = "or";

	/** Predstavlja privatan konstantan niz znakova "xor" */
	private static final String XOR = "xor";

	/**
	 * Predstavlja privatan konstantan niz znakova ":+:" (izvedenica od
	 * {@value #XOR} operatora)
	 */
	private static final String XOR_SIGN = ":+:";

	/** Predstavlja privatan konstantan niz znakova "true" */
	private static final String TRUE = "true";

	/**
	 * Predstavlja privatan konstantan niz znakova "1" (izvedenica od konstante
	 * {@value #TRUE})
	 */
	private static final String TRUE_NUMERIC = "1";

	/** Predstavlja privatan konstantan niz znakova "false" */
	private static final String FALSE = "false";

	/**
	 * Predstavlja privatan konstantan niz znakova "0" (izvedenica od konstante
	 * {@value #FALSE})
	 */
	private static final String FALSE_NUMERIC = "0";

	/** Predstavlja privatan konstantan znak znakova '_' */
	private static final char UNDERSCORE = '_';

	/** Predstavlja privatan konstantan znak znakova '(' */
	private static final Character OPEN_BRACKET = '(';

	/** Predstavlja privatan konstantan znak znakova ')' */
	private static final Character CLOSED_BRACKET = ')';

	/**
	 * Predstavlja privatan konstantan znak znakova '*' (izvedenica od
	 * {@value #AND} operatora)
	 */
	private static final char AND_SIGN = '*';

	/**
	 * Predstavlja privatan konstantan znak znakova '+' (izvedenica od
	 * {@value #OR} operatora)
	 */
	private static final char OR_SIGN = '+';

	/**
	 * Predstavlja privatan konstantan znak znakova '!'(izvedenica od
	 * {@value #NOT} operatora)
	 */
	private static final char NOT_SIGN = '!';

	/**
	 * Predstavlja konstantan primjerak razreda {@link Token}. Tip ovog
	 * primjerka razreda je {@link TokenType#OPERATOR}, a vrijednost je
	 * {@link #AND}
	 */
	public static final Token AND_TOKEN = new Token(TokenType.OPERATOR, AND);

	/**
	 * Predstavlja konstantan primjerak razreda {@link Token}. Tip ovog
	 * primjerka razreda je {@link TokenType#OPERATOR}, a vrijednost je
	 * {@link #NOT}
	 */
	public static final Token NOT_TOKEN = new Token(TokenType.OPERATOR, NOT);

	/**
	 * Predstavlja konstantan primjerak razreda {@link Token}. Tip ovog
	 * primjerka razreda je {@link TokenType#OPERATOR}, a vrijednost je
	 * {@link #OR}
	 */
	public static final Token OR_TOKEN = new Token(TokenType.OPERATOR, OR);

	/**
	 * Predstavlja konstantan primjerak razreda {@link Token}. Tip ovog
	 * primjerka razreda je {@link TokenType#OPERATOR}, a vrijednost je
	 * {@link #XOR}
	 */
	public static final Token XOR_TOKEN = new Token(TokenType.OPERATOR, XOR);

	/**
	 * Predstavlja privatan konstantan primjerak razreda {@link Token}. Tip ovog
	 * primjerka razreda je {@link TokenType#CONSTANT}, a vrijednost je
	 * {@link Boolean#TRUE}
	 */
	private static final Token TRUE_TOKEN = new Token(TokenType.CONSTANT, Boolean.TRUE);

	/**
	 * Predstavlja privatan konstantan primjerak razreda {@link Token}. Tip ovog
	 * primjerka razreda je {@link TokenType#CONSTANT}, a vrijednost je
	 * {@link Boolean#FALSE}
	 */
	private static final Token FALSE_TOKEN = new Token(TokenType.CONSTANT, Boolean.FALSE);

	/**
	 * Predstavlja konstantan primjerak razreda {@link Token}. Tip ovog
	 * primjerka razreda je {@link TokenType#OPEN_BRACKET}, a vrijednost je
	 * {@link #OPEN_BRACKET}
	 */
	public static final Token OPEN_BRACKET_TOKEN = new Token(TokenType.OPEN_BRACKET, OPEN_BRACKET);

	/**
	 * Predstavlja konstantan primjerak razreda {@link Token}. Tip ovog
	 * primjerka razreda je {@link TokenType#CLOSED_BRACKET}, a vrijednost je
	 * {@link #CLOSED_BRACKET}
	 */
	public static final Token CLOSED_BRACKET_TOKEN = new Token(TokenType.CLOSED_BRACKET, CLOSED_BRACKET);;

	/**
	 * Predstavlja konstantan primjerak razreda {@link Token}. Tip ovog
	 * primjerka razreda je {@link TokenType#EOF}, a vrijednost je
	 * <code>null</code>
	 */
	public static final Token EOF_TOKEN = new Token(TokenType.EOF, null);

	/** Ulazni niz znakova koji se leksički analizira */
	private char[] data;

	/** Trenutni pozicija u ulaznom nizu znakova */
	private int currentIndex;

	/** Zadnje izvađeni token */
	private Token currentToken;

	/**
	 * Konstruktor koji iz ulaznog niza znakova koji predstavlja logički izraz
	 * inicijalizira ulazni niz znakova.
	 *
	 * @param expression
	 *            ulazni tekst koji je potrebno leksički analizirati
	 * @throws LexerException
	 *             ukoliko je kao <b>expression</b> predan <b>null</b>
	 * 
	 */
	public Lexer(String expression) {
		if (expression == null) {
			throw new LexerException("Lexer ne može leksički analizirati null");
		}

		this.data = expression.toCharArray();
	}

	/**
	 * Metoda koja vrši vađenje idućeg primjerka razreda {@link Token} iz
	 * predanog niza znakova, od trenutne poziciji koja još nije leksički
	 * analizirana. Metoda će interno spremiti zadnje izvađeni primjerak razreda
	 * {@link Token} radi provjere je leksička analiza došla do kraja.
	 *
	 * @return primjerak razreda {@link Token} iz preostalog niza znakova od
	 *         pozicije na kojoj je leksički analizator stao
	 * 
	 * @throws LexerException
	 *             ukoliko vađenje idućeg primjerka razreda {@link Token} ne
	 *             uspije
	 */
	public Token nextToken() {
		prepareExtraction();
		return currentToken;
	}

	/**
	 * Pomoćna metoda koja se koristi za pripremu preostalog dijela niza za
	 * leksičku analizu. Unutar ove metode provjerava se imali još uopće znakova
	 * te se poziva metoda {@link #skipWhitespaces()} ukoliko je ostalo još
	 * neanaliziranih znakova
	 * 
	 * @throws LexerException
	 *             ukoliko se metoda pozove nakon što je vraćen
	 *             {@link #EOF_TOKEN}
	 */
	private void prepareExtraction() {
		if (currentToken != null && currentToken.getTokenType() == TokenType.EOF) {
			throw new LexerException("Nemam više tokena!");
		}

		skipWhitespaces();

		if (isEOF()) {
			currentToken = EOF_TOKEN;
			return;
		}

		extractNextToken();
	}

	/**
	 * Pomoćna metoda koja ovisno o prvom pročitanom znaku do sada
	 * neanaliziranog niza odlučuje koji će tip primjerka razreda {@link Token}
	 * pokušati izvaditi.
	 * 
	 * @throws LexerException
	 *             ukoliko se pročitani znak ne može klasificirati.
	 */
	private void extractNextToken() {
		char c = data[currentIndex];
		if (Character.isLetter(c)) {
			extractIdentifier();
		} else if (Character.isDigit(c)) {
			extractConstant();
		} else if (isOperator(c)) {
			if (c == XOR_SIGN.charAt(0)) {
				extractXor();
			} else {
				extractAndOrNot();
			}
		} else if (c == OPEN_BRACKET || c == CLOSED_BRACKET) {
			extractBrackets();
		} else {
			throw new LexerException("Ne mogu klasificirati znak: " + c);
		}
	}

	/**
	 * Pomoćna metoda koja iz preostalog niza znakova od trenutne pozicije
	 * pokušava izvaditi identifikator. Identifikator je niz znakova koji
	 * započinje slovom ili znakom {@value #UNDERSCORE} te potom slijedi
	 * proizvoljan niz slova, brojeva ili znakova {@value #UNDERSCORE}. Budući
	 * da istom regularnom izrazu pripadaju i nizovi {@value #TRUE},
	 * {@value #FALSE} i operatori {@value #AND}, {@value #XOR}, {@value #XOR} i
	 * {@value #NOT} ukoliko je pročitan neki od tih nizova , metoda vraća
	 * prikladan primjerak razreda {@link Token}.
	 */
	private void extractIdentifier() {
		StringBuilder sb = new StringBuilder();
		char c;
		while (!isEOF() && (Character.isLetterOrDigit((c = data[currentIndex])) || c == UNDERSCORE)) {
			sb.append(c);
			currentIndex++;
		}
		String extracted = sb.toString();

		if (!extractOperator(extracted) && !extractBoolean(extracted)) {
			currentToken = new Token(TokenType.VARIABLE, extracted.toUpperCase());
		}
	}

	/**
	 * Pomoćna metoda koja iz predanog niza znakova <b>extracted</b> , ukoliko
	 * je taj niz jednak jednom od nizova {@value #AND}, {@value #XOR},
	 * {@value #XOR} ili {@value #NOT}, postavlja trenutno izvađeni token na
	 * {@link #AND_TOKEN}, {@link #XOR_TOKEN}, {@link #OR_TOKEN} ili
	 * {@link #NOT_TOKEN}.
	 *
	 * @param extracted
	 *            niz znakova koji se provjerava je li jednak nizovima
	 *            {@value #AND}, {@value #XOR}, {@value #XOR} ili {@value #NOT}
	 * @return <b>true</b> ako je <b>extracted</b> jednak jednom od nizova
	 *         {@value #AND}, {@value #XOR}, {@value #XOR} ili {@value #NOT},
	 *         <b>false</b> inače
	 */
	private boolean extractOperator(String extracted) {
		if (extracted.equalsIgnoreCase(AND)) {
			currentToken = AND_TOKEN;
		} else if (extracted.equalsIgnoreCase(OR)) {
			currentToken = OR_TOKEN;
		} else if (extracted.equalsIgnoreCase(XOR)) {
			currentToken = XOR_TOKEN;
		} else if (extracted.equalsIgnoreCase(NOT)) {
			currentToken = NOT_TOKEN;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Pomoćna metoda koja iz predanog niza znakova <b>extracted</b> , ukoliko
	 * je taj niz jednak jednom od nizova {@value #TRUE} ili {@value #FALSE},
	 * postavlja trenutno izvađeni token na {@link #TRUE_TOKEN},
	 * {@link #FALSE_TOKEN}
	 * 
	 * @param extracted
	 *            niz znakova koji se provjerava je li jednak nizovima
	 *            {@value #TRUE} ili {@value #FALSE}
	 * @return <b>true</b> ako je <b>extracted</b> jednak jednom od nizova
	 *         {@value #TRUE} ili {@value #FALSE}, <b>false</b> inače
	 */
	private boolean extractBoolean(String extracted) {
		if (extracted.equalsIgnoreCase(TRUE) || extracted.equals(TRUE_NUMERIC) || extracted.equalsIgnoreCase(FALSE)
				|| extracted.equals(FALSE_NUMERIC)) {
			currentToken = extracted.equalsIgnoreCase(TRUE) || extracted.equals(TRUE_NUMERIC) ? TRUE_TOKEN
					: FALSE_TOKEN;
			return true;
		}
		return false;
	}

	/**
	 * Pomoćna metoda koja vadi jednu od konstanti {@link #TRUE_NUMERIC} ili
	 * {@link #FALSE_NUMERIC}. Općenito konstanta je niz znamenki, no token će
	 * stvoriti samo ove spomenuta dva niza.
	 * 
	 * @throws LexerException
	 *             ukoliko izvađeni niz znakova nije jednak
	 *             {@link #TRUE_NUMERIC} ili {@link #FALSE_NUMERIC}
	 */
	private void extractConstant() {
		StringBuilder sb = new StringBuilder();
		while (!isEOF() && Character.isDigit(data[currentIndex])) {
			sb.append(data[currentIndex++]);
		}
		String extracted = sb.toString();

		if (!extractBoolean(extracted)) {
			throw new LexerException("'" + extracted + "' nije valjana konstanta");
		}
	}

	/**
	 * Pomoćna metoda koja pokušava izvaditi niz {@value #XOR_SIGN}. Ukoliko
	 * uspije kao sljedeći primjerak razreda {@link Token} metoda
	 * {@link #nextToken()} vratiti će {@link #XOR_TOKEN}.
	 * 
	 * @throws LexerException
	 *             ukoliko vađenje {@value #XOR_SIGN} nije uspjelo
	 */
	private void extractXor() {
		String possibleXor = new String(data, currentIndex, XOR_SIGN.length());
		if (possibleXor.equals(XOR_SIGN)) {
			currentIndex += XOR_SIGN.length();
			currentToken = XOR_TOKEN;
		} else {
			throw new LexerException("Predani niz: " + possibleXor + " nije XOR operator");
		}
	}

	/**
	 * Pomoćna metoda koja pokušava izvaditi znakove {@value #AND_SIGN},
	 * {@value #OR_SIGN} ili {@value #NOT_SIGN}. Ukoliko uspije kao sljedeći
	 * primjerak razreda {@link Token} metoda {@link #nextToken()} vratiti će
	 * {@link #AND_TOKEN}, {@link #OR_TOKEN} ili {@link #NOT_TOKEN}.
	 * 
	 * @throws LexerException
	 *             ukoliko vađenje {@value #XOR_SIGN} nije uspjelo
	 */
	private void extractAndOrNot() {
		char c = data[currentIndex++];

		switch (c) {
		case AND_SIGN:
			currentToken = AND_TOKEN;
			break;
		case OR_SIGN:
			currentToken = OR_TOKEN;
			break;
		case NOT_SIGN:
			currentToken = NOT_TOKEN;
			break;
		default:
			throw new LexerException("Ne postoji operator: " + c);
		}
	}

	/**
	 * Pomoćna metoda koja provjerava je li znak početak nekog od operatora. To
	 * su znakovi:
	 * <ul>
	 * <li>{@value #AND_SIGN}</li>
	 * <li>{@value #OR_SIGN}</li>
	 * <li>{@value #NOT_SIGN}</li>
	 * <li>':'</li>
	 * </ul>
	 *
	 * @param c
	 *            znak koji se provjerava
	 * @return <b>true</b> ako je znak početak nekog od operatora, <b>false</b>
	 *         inače
	 */
	private boolean isOperator(char c) {
		return c == AND_SIGN || c == OR_SIGN || c == NOT_SIGN || c == XOR_SIGN.charAt(0);
	}

	/**
	 * Pomoćna metoda koja iz niza vadi znakove {@link #OPEN_BRACKET} ili
	 * {@link #CLOSED_BRACKET}, te kao sljedeći primjerak razreda {@link Token}
	 * koji će vratiti {@link #nextToken()} postavlja
	 * {@link #OPEN_BRACKET_TOKEN} ili {@link #CLOSED_BRACKET_TOKEN}.
	 */
	private void extractBrackets() {
		currentToken = data[currentIndex++] == OPEN_BRACKET ? OPEN_BRACKET_TOKEN : CLOSED_BRACKET_TOKEN;
	}

	/**
	 * Pomoćna metoda koja ispituje jesmo li došli do kraja ulaznog niza.
	 *
	 * @return <b>true</b> ukoliko smo došli do kraj niza <b>false</b> inače
	 */
	private boolean isEOF() {
		return currentIndex >= data.length;
	}

	/**
	 * Pomoćna metoda koja se koristi za preskakanje praznina u ulaznom nizu.
	 */
	private void skipWhitespaces() {
		while (!isEOF() && isWhitespace(data[currentIndex])) {
			currentIndex++;
		}
	}

	/**
	 * Pomoćna metoda koja ispituje je li predani znak praznina. Kao praznine se
	 * podrazumjevaju znakovi :
	 * <ul>
	 * <li>'\t'</li>
	 * <li>'\r'</li>
	 * <li>'\n'</li>
	 * <li>' '</li>
	 * </ul>
	 * 
	 *
	 * @param c
	 *            znak koji se provjerava
	 * @return <b>true </b> ukoliko je <b>c</b> praznina, inače vraća
	 *         <b>false</b>
	 */
	private boolean isWhitespace(char c) {
		return c == '\n' || c == '\t' || c == '\r' || c == ' ';
	}
}
