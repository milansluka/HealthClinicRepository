package ehc.util;

import java.util.ArrayList;
import java.util.List;

public class TokenList {
	public class Token {
		public Token(String value) {
			this.value = value;
			this.upperCaseValue = value != null ? value.toUpperCase() : null;
		}

		String value;
		String upperCaseValue;

		public boolean isEqual(Token t) {
			return this.upperCaseValue.equals(t.upperCaseValue);
		}
		@Override
		public String toString() {

			StringBuffer b = new StringBuffer();
			b.append(value);
			b.append(":");
			b.append(upperCaseValue);

			return b.toString();
		}

	}
	public TokenList(String value) {
		this.initialValue = value;
		this.tokens = tokenize(value);
	}
	String initialValue;
	List<Token> tokens;

	private ArrayList<Token> tokenize(String string) {
		ArrayList<Token> answer = new ArrayList<Token>();
		if (string == null)
			return answer;
		int tokenStart = 0;
		int i = tokenStart;
		while (i < string.length()) {
			while (i < string.length() && Character.isLetter(string.charAt(i)))
				i++;
			if (i > tokenStart)
				answer.add(new Token(string.substring(tokenStart, i)));
			i++;
			tokenStart = i;
		}

		return answer;
	}

	public boolean isIncludedIn(TokenList tokensToBeSearched) {
		if (tokensToBeSearched == null || getTokens().isEmpty())
			return false;
		for (Token tokenToBeIncluded : getTokens()) {
			if (!tokensToBeSearched.isTokenIncluded(tokenToBeIncluded))
				return false;
		}
		return true;
	}

	public boolean isTokenIncluded(Token tokenToBeIncluded) {
		for (Token myToken : getTokens()) {
			if (myToken.isEqual(tokenToBeIncluded))
				return true;
		}
		return false;
	}

	public List<Token> getTokens() {
		return tokens;
	}
	@Override
	public String toString() {

		StringBuffer b = new StringBuffer();
		b.append("initial value: ");
		b.append(initialValue);
		b.append("\nTokens: ");
		b.append(tokens);
		return b.toString();
	}

}
